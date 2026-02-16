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

    public static void run(String host) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            // On attend la réponse pour 2s max
            socket.setSoTimeout(2000);
            // On envoie le plus petit paquet possible
            byte[] sendBuf = new byte[1];
            InetAddress addr = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, addr, PORT);
            socket.send(packet);

            // Réception du paquet
            byte[] recvBuf = new byte[1024];
            DatagramPacket resp = new DatagramPacket(recvBuf, recvBuf.length);
            socket.receive(resp);
            String payload = new String(resp.getData(), 0, resp.getLength(), StandardCharsets.UTF_8).trim();

            // Parse du timestamp envoyé par le serveur
            long epochMillis;
            try {
                epochMillis = Long.parseLong(payload);
            } catch (NumberFormatException e) {
                System.err.println("Invalid response from server: " + payload);
                return;
            }
            Instant instant = Instant.ofEpochMilli(epochMillis);
            ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Affichage
            System.out.println(zdt.format(fmt));
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: no response from server");
        }
    }

    public static void main(String[] args) throws IOException {
        // Lance le client sur le loopback
        run("localhost");
    }
}