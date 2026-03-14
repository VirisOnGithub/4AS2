import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class POPHandler implements Runnable {
    private Socket socket;
    private POPDB db;
    private PrintWriter writer;
    private BufferedReader reader;
    private String pendingUser;

    public POPHandler(Socket serverSocket, POPDB db) throws IOException {
        this.socket = serverSocket;
        this.db = db;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            sendMessage("+OK POP3 server ready");

            String line;
            while ((line = reader.readLine()) != null) {
                Command command;
                try {
                    processCommand(line);
                } catch (IllegalArgumentException e) {
                    sendMessage("-ERR " + e.getMessage());
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processCommand(String line) {
        Command command = Command.parseCommand(line);
        switch (command.getType()) {
            case USER -> handleUser(command.getArgs());
            case PASS -> handlePass(command.getArgs());
            case STAT -> handleStat();
            case LIST -> handleList(command.getArgs());
            case RETR -> handleRetr(command.getArgs());
            case TOP -> handleTop(command.getArgs());
            case DELE -> handleDele(command.getArgs());
            case RSET -> handleRset();
            case NOOP -> handleNoop();
            case QUIT -> handleQuit();
        }
    }

    private void handleList(ArrayList<String> args) {
    }

    private void handleDele(ArrayList<String> args) {
    }

    private void handleQuit() {
    }

    private void handleNoop() {
    }

    private void handleRset() {

    }

    private void handleTop(ArrayList<String> args) {

    }

    private void handleRetr(ArrayList<String> args) {

    }

    private void handleStat() {

    }

    private void handlePass(ArrayList<String> args) {
        if (pendingUser == null) {
            sendMessage("-ERR PASS command requires USER command first");
            return;
        }
        if (args.size() != 1) {
            sendMessage("-ERR PASS command requires exactly 1 argument");
            return;
        }
        String password = args.get(0);
        String authenticatedUser = db.authenticate(pendingUser, password);
        if (authenticatedUser != null) {
            sendMessage("+OK Authentication successful, welcome " + authenticatedUser);
        } else {
            sendMessage("-ERR Authentication failed");
        }
    }

    private void handleUser(ArrayList<String> args) {
        if (args.size() != 1) {
            sendMessage("-ERR USER command requires exactly 1 argument");
            return;
        }
        pendingUser = args.get(0);
        sendMessage("+OK User name accepted, sned PASS command");
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
