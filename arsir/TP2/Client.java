import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 2121;
        if (args.length > 0)
            host = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);

        ClientSession session = new ClientSession(host, port);
        session.run();
    }
}
