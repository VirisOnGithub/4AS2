import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimeClient {
    private static final int PORT = 12345;

    public static void run(String host) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, PORT), 3000);
            System.out.println("Connected " + host + ":" + PORT);

            try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), "UTF-8"))) {

                String input;
                System.out.println("Enter a command: DATE, HOUR, FULL, CLOSE");
                // Gestion de l'input en boucle
                while ((input = stdin.readLine()) != null) {
                    String cmd = input.trim();
                    if (cmd.isEmpty())
                        continue;
                    out.write(cmd);
                    out.newLine();
                    out.flush();

                    String resp = in.readLine();
                    if (resp == null) {
                        System.out.println("Server closed the connection");
                        break;
                    }
                    System.out.println("Response: " + resp);

                    // Fermeture si CLOSE
                    if ("CLOSE".equalsIgnoreCase(cmd)) {
                        System.out.println("Local closing after CLOSE");
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        run("localhost");
    }
}
