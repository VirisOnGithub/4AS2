import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    private static final int PORT = 12345;

    public static void run() throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Capitalization server listening on port " + PORT);
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
                String up = line.toUpperCase();
                out.write(up);
                out.newLine();
                out.flush();
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

    public static void main(String[] args) throws IOException {
        run();
    }
}
