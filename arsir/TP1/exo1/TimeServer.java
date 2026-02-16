import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class TimeServer {
    private static final int PORT = 12345;

    public static void run() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Serveur UDP écoute sur le port " + PORT);
            byte[] buf = new byte[1];
            while (true) {
                DatagramPacket req = new DatagramPacket(buf, buf.length);
                socket.receive(req);
                long epochMillis = System.currentTimeMillis(); // Récupération du timestamp courant
                byte[] resp = String.valueOf(epochMillis).getBytes(StandardCharsets.UTF_8);

                // Envoi de la réponse
                DatagramPacket reply = new DatagramPacket(resp, resp.length, req.getAddress(), req.getPort());
                socket.send(reply);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        run();
    }
}