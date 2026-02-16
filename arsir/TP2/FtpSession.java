
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

final class FtpSession implements Runnable {
    private final Socket socket;
    private final DataConnectionManager dataManager;
    private final FileSystem vfs;
    private final File baseDataDir;

    private String currentUser = null;
    private boolean awaitingPass = false;
    private boolean authenticated = false;

    FtpSession(Socket socket) {
        this.socket = socket;
        this.dataManager = new DataConnectionManager(socket.getLocalAddress());
        this.baseDataDir = new File("Data").getAbsoluteFile();
        this.vfs = new FileSystem(baseDataDir);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            send(out,
                    "200 Server Ready");

            String line;
            while ((line = in.readLine()) != null) {
                CommandParser.ParsedCommand parsed = CommandParser.parse(line);
                if (parsed.command.isEmpty())
                    continue;
                switch (parsed.command) {
                    case "USER":
                        handleUser(parsed.arg, out);
                        break;
                    case "PASS":
                        handlePass(parsed.arg, out);
                        break;
                    case "DECO":
                        handleDeco(out);
                        break;
                    case "PASV":
                        handlePasv(out);
                        break;
                    case "PORT":
                        handlePort(parsed.arg, out);
                        break;
                    case "CWD":
                        handleCwd(parsed.arg, out);
                        break;
                    case "LIST":
                        handleList(out);
                        break;
                    case "RETR":
                        handleRetr(parsed.arg, out);
                        break;
                    case "QUIT":
                        send(out, "200 Goodbye");
                        dataManager.closeAll();
                        socket.close();
                        return;
                    default:
                        send(out, "501 Syntax error: unknown command");
                        break;
                }
            }
        } catch (IOException e) {
            // connection closed or error
        } finally {
            dataManager.closeAll();
            try {
                socket.close();
            } catch (Exception ex) {
            }
        }
    }

    private void handleUser(String arg, BufferedWriter out) throws IOException {
        if (authenticated) {
            send(out, "530 Already authenticated: use DECO to disconnect before changing user");
            return;
        }
        if (arg.isEmpty()) {
            send(out, "501 Syntax error: USER <login>");
            return;
        }
        if (arg.equalsIgnoreCase("anonymous") || arg.equals("foo")) {
            currentUser = arg;
            awaitingPass = true;
            authenticated = false;
            send(out, "331 User recognized, password required");
            return;
        }
        currentUser = null;
        awaitingPass = false;
        authenticated = false;
        send(out, "430 Invalid username or password");
    }

    private void handlePass(String arg, BufferedWriter out) throws IOException {
        if (!awaitingPass || currentUser == null) {
            send(out, "501 Syntax error: PASS without USER");
            return;
        }
        if (currentUser.equalsIgnoreCase("anonymous")) {
            awaitingPass = false;
            authenticated = true;
            vfs.setRoot(new File(baseDataDir, "anonymous").getAbsoluteFile());
            send(out, "200 Anonymous login accepted. You are logged in as 'anonymous'.");
            return;
        }
        if (currentUser.equals("foo") && arg.equals("bar")) {
            awaitingPass = false;
            authenticated = true;
            vfs.setRoot(new File(baseDataDir, "foo").getAbsoluteFile());
            send(out, "200 Authentication successful. Welcome, foo!");
            return;
        }
        awaitingPass = false;
        authenticated = false;
        send(out, "430 Invalid username or password");
    }

    private void handleDeco(BufferedWriter out) throws IOException {
        if (!authenticated && currentUser == null) {
            send(out, "200 You are not connected. You can connect with USER <login>.");
            return;
        }
        dataManager.closeAll();
        currentUser = null;
        awaitingPass = false;
        authenticated = false;
        vfs.setRoot(baseDataDir);
        send(out, "200 Disconnected successfully.");
    }

    private void handlePasv(BufferedWriter out) throws IOException {
        if (!authenticated) {
            send(out, "530 Not authenticated: please log in first");
            return;
        }
        try {
            String addr = dataManager.openPassive();
            send(out, "227 " + addr);
        } catch (IOException ex) {
            send(out, "425 Can't open passive mode");
        }
    }

    private void handlePort(String arg, BufferedWriter out) throws IOException {
        if (!authenticated) {
            send(out, "530 Not authenticated: please log in first");
            return;
        }
        if (arg.isEmpty()) {
            send(out, "501 Syntax error: PORT a,b,c,d,e,f");
            return;
        }
        String[] nums = arg.split(",");
        if (nums.length != 6) {
            send(out, "501 Syntax error: PORT must have 6 numbers");
            return;
        }
        try {
            int a = Integer.parseInt(nums[0]);
            int b = Integer.parseInt(nums[1]);
            int c = Integer.parseInt(nums[2]);
            int d = Integer.parseInt(nums[3]);
            int e = Integer.parseInt(nums[4]);
            int f = Integer.parseInt(nums[5]);
            String host = String.format("%d.%d.%d.%d", a, b, c, d);
            int portData = e * 256 + f;
            dataManager.setActiveEndpoint(host, portData);
            send(out, "200 Active mode set: data connection will be established for transfer to " + host
                    + ":" + portData);
        } catch (NumberFormatException ex) {
            send(out, "501 Syntax error: PORT invalid numbers");
        }
    }

    private void handleCwd(String arg, BufferedWriter out) throws IOException {
        if (!authenticated) {
            send(out, "530 Not authenticated: please log in first");
            return;
        }
        if (arg.isEmpty()) {
            send(out, "501 Syntax error: CWD <directory>");
            return;
        }
        FileSystem.CwdResult result = vfs.changeDir(arg);
        if (result.success) {
            send(out, "250 CWD successful. Current directory: " + result.message);
        } else {
            send(out, "550 " + result.message);
        }
    }

    private void handleList(BufferedWriter out) throws IOException {
        if (!authenticated) {
            send(out, "530 Not authenticated: please log in first");
            return;
        }
        Socket ds = null;
        try {
            ds = dataManager.openDataSocket();
        } catch (IOException ex) {
            ds = null;
        }
        if (ds == null) {
            send(out, "425 No data connection");
            return;
        }
        send(out, "150 Opening connection");
        try (BufferedWriter dataOut = new BufferedWriter(new OutputStreamWriter(ds.getOutputStream()))) {
            for (String name : vfs.listEntries()) {
                dataOut.write(name + "\r\n");
            }
            dataOut.flush();
        } catch (IOException ex) {
            // ignore
        } finally {
            try {
                ds.close();
            } catch (Exception ex) {
            }
        }
        send(out, "226 Transfer complete. List sent successfully.");
    }

    private void handleRetr(String arg, BufferedWriter out) throws IOException {
        if (!authenticated) {
            send(out, "530 Not authenticated: please log in first");
            return;
        }
        if (arg.isEmpty()) {
            send(out, "501 Syntax error: RETR <filename>");
            return;
        }
        Socket ds = null;
        try {
            ds = dataManager.openDataSocket();
        } catch (IOException ex) {
            ds = null;
        }
        if (ds == null) {
            send(out, "425 No data connection");
            return;
        }

        try {
            File target = vfs.resolveLocalFile(arg);
            if (target == null) {
                send(out, "550 File not found: " + arg);
            } else {
                send(out, "150 Opening connection");
                try (OutputStream dataOut = ds.getOutputStream();
                        FileInputStream fis = new FileInputStream(target)) {
                    byte[] buf = new byte[8192];
                    int r;
                    while ((r = fis.read(buf)) != -1) {
                        dataOut.write(buf, 0, r);
                    }
                    dataOut.flush();
                } catch (IOException ex) {
                    // ignore
                }
                send(out, "226 Transfer complete. File sent successfully.");
            }
        } catch (IOException ex) {
            send(out, "550 RETR error: " + ex.getMessage());
        } finally {
            try {
                ds.close();
            } catch (Exception ex) {
            }
        }
    }

    private void send(BufferedWriter out, String msg) throws IOException {
        out.write(msg + "\r\n");
        out.flush();
    }
}
