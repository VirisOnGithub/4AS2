import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServer {
    private static final int PORT = 12345;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void run() throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("TimeServer (TCP) listening on port " + PORT);
            while (true) {
                Socket client = server.accept();
                Thread t = new Thread(() -> handleClient(client));
                t.setDaemon(true);
                t.start();
            }
        }
    }

    private static void handleClient(Socket client) {
        String remote = client.getRemoteSocketAddress().toString();
        System.out.println("Client connected: " + remote);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"))) {

            String line;
            while ((line = in.readLine()) != null) {
                String cmd = line.trim().toUpperCase();
                if (cmd.isEmpty())
                    continue;
                System.out.println("Received from " + remote + ": '" + cmd + "'");
                String response;
                switch (cmd) {
                    case "DATE":
                        response = nowDate();
                        break;
                    case "HOUR":
                        response = nowTime();
                        break;
                    case "FULL":
                        response = nowDate() + " " + nowTime();
                        break;
                    case "CLOSE":
                        response = "BYE";
                        break;
                    default:
                        response = "UNKNOWN COMMAND";
                        break;
                }
                out.write(response);
                out.newLine();
                out.flush();
                if ("CLOSE".equals(cmd)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error with " + remote + ": " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {
            }
            System.out.println("Client disconnected: " + remote);
        }
    }

    private static String nowDate() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(DATE_FMT);
    }

    private static String nowTime() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(TIME_FMT);
    }

    public static void main(String[] args) throws IOException {
        run();
    }
}
