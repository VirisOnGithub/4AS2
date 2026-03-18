import java.io.IOException;
import java.net.ServerSocket;

public class POPServer {
    private static final int PORT = 1110;
    private final ServerSocket socket;
    private final POPDB db;

    public POPServer() throws IOException {
        this.db = new POPDB();
        this.socket = new ServerSocket(PORT);
    }

    public void start() {
        System.out.println("POP3 server started on port " + PORT);
        while (true) {
            try {
                new Thread(new POPHandler(socket.accept(), db)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        POPServer server = new POPServer();
        server.start();
    }
}
