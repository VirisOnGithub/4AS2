import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeClient {
    private static final int PORT = 12345;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void run(String host) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, PORT), 3000);

            try (InputStream in = new BufferedInputStream(socket.getInputStream())) {
                // Lit la réponse du serveur
                byte[] buf = new byte[1024];
                int read = in.read(buf);
                if (read <= 0) {
                    System.err.println("No data received from server");
                    return;
                }

                // parse du timestamp reçu
                String payload = new String(buf, 0, read, StandardCharsets.UTF_8).trim();
                long epochMillis;
                try {
                    epochMillis = Long.parseLong(payload);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid response from server: " + payload);
                    return;
                }

                // Affiche le timestamp reçu au format demandé
                System.out.println("Received epoch millis: " + epochMillis + " -> " + formatMillis(epochMillis));
            }
        }
    }

    private static String formatMillis(long epochMillis) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
        return zdt.format(TIME_FMT);
    }

    public static void main(String[] args) throws IOException {
        run("localhost");
    }
}
