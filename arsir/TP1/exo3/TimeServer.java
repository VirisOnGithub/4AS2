import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServer {
    private static final int PORT = 12345;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void run() throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("TCP server listening on port " + PORT);
            while (true) {
                // réception d'une connexion client
                Socket client = server.accept();
                long now = System.currentTimeMillis();
                String payload = String.valueOf(now);
                byte[] data = payload.getBytes(StandardCharsets.UTF_8);

                System.out.println("Connection received from " + client.getRemoteSocketAddress()
                        + " \n\tdata sent: " + formatMillis(now));

                // envoi de la réponse au client²
                try (OutputStream out = client.getOutputStream()) {
                    out.write(data);
                    out.flush();
                } catch (IOException e) {
                    System.err.println("Error sending to client: " + e.getMessage());
                } finally {
                    client.close();
                }
            }
        }
    }

    private static String formatMillis(long epochMillis) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
        return zdt.format(TIME_FMT);
    }

    public static void main(String[] args) throws IOException {
        run();
    }
}
