import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServer {
    private static final int PORT = 12345;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void run() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP server waiting at port:" + PORT);
            byte[] buf = new byte[1024];

            while (true) {
                DatagramPacket req = new DatagramPacket(buf, buf.length);
                socket.receive(req);

                // Calcul du timestamp actuel (T'1)
                long t01 = System.currentTimeMillis();

                // Parse du timestamp client reçu (T1)
                String reqStr = new String(req.getData(), 0, req.getLength(), StandardCharsets.UTF_8).trim();
                long t1;
                try {
                    t1 = Long.parseLong(reqStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid request: '" + reqStr + "'");
                    continue;
                }
                long t02 = System.currentTimeMillis();
                String resp = t1 + ";" + t01 + ";" + t02;
                byte[] respBytes = resp.getBytes(StandardCharsets.UTF_8);

                // Envoi de la réponse
                DatagramPacket reply = new DatagramPacket(respBytes, respBytes.length, req.getAddress(), req.getPort());
                socket.send(reply);

                System.out.println("Message sent to client: " + resp);
                System.out.println("Translated into hours " + formatMillis(t1) + ";" + formatMillis(t01) + ";"
                        + formatMillis(t02));
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
