import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeClient {
    private static final int PORT = 12345;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void run(String host) throws IOException {
        InetAddress addr = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000);

            // Calcul du temps et envoi au serveur
            long t1 = System.currentTimeMillis();
            System.out.println("T1 sent to server = " + t1);
            byte[] sendBuf = String.valueOf(t1).getBytes(StandardCharsets.UTF_8);
            DatagramPacket req = new DatagramPacket(sendBuf, sendBuf.length, addr, PORT);
            socket.send(req);

            // Réception de la réponse
            byte[] recvBuf = new byte[1024];
            DatagramPacket resp = new DatagramPacket(recvBuf, recvBuf.length);
            socket.receive(resp);

            long t2 = System.currentTimeMillis();

            String payload = new String(resp.getData(), 0, resp.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = payload.split(";");
            if (parts.length < 3) {
                System.err.println("Réponse serveur invalide: " + payload);
                return;
            }

            long t01 = Long.parseLong(parts[1]);
            long t02 = Long.parseLong(parts[2]);

            System.out.println("δ = (T2 - T1) - (T'2 - T'1)");
            System.out.println("θ = ((T'1 + T'2) / 2) - ((T1 + T2) / 2)");

            System.out.println("T1 = " + formatMillis(t1));
            System.out.println("T'1 = " + formatMillis(t01));
            System.out.println("T'2 = " + formatMillis(t02));
            System.out.println("T2 = " + formatMillis(t2));

            long delta = (t2 - t1) - (t02 - t01);
            double theta = ((t01 + t02) / 2.0) - ((t1 + t2) / 2.0);

            System.out.println("Delay δ = " + delta + " ms");
            System.out.println("Gap θ = " + theta + " ms");

            long corrected = (long) (System.currentTimeMillis() + theta);
            java.time.ZonedDateTime zdt = java.time.ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(corrected),
                    java.time.ZoneId.systemDefault());
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
            System.out.println("Corrected clock: " + zdt.format(fmt));

        } catch (SocketTimeoutException e) {
            System.err.println("Timeout : pas de réponse du serveur");
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
