import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 2121;

    public static void main(String[] args) {
        int port = PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Expected number for port, using default " + PORT);
            }
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server initialized on port " + port);

            ExecutorService pool = Executors.newCachedThreadPool();

            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(new FtpSession(client));
            }
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }

    }
}
