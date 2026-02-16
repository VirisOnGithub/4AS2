import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final int PORT = 12345;
    private volatile boolean running = true;
    private volatile char mySymbol = '?';

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        new Client().run(host);
    }

    public void run(String host) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, PORT), 3000);
            System.out.println("Connected to  " + host + ":" + PORT);

            try (BufferedReader serverIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    BufferedWriter serverOut = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                    BufferedReader stdin = new BufferedReader(
                            new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

                Thread reader = new Thread(() -> {
                    try {
                        String line;
                        while ((line = serverIn.readLine()) != null) {
                            if (line.equals("BOARD")) {

                                while ((line = serverIn.readLine()) != null && !line.equals("END_BOARD")) {
                                    System.out.println(line);
                                }
                                System.out.println();
                                continue;
                            }

                            String up = line.trim();
                            if (up.startsWith("START")) {

                                String[] p = up.split("\\s+");
                                if (p.length >= 2) {
                                    mySymbol = p[1].charAt(0);
                                    System.out.println("Start of the game. You play '" + mySymbol + "'.");
                                }
                            } else if (up.equals("YOUR_TURN")) {
                                System.out.println(
                                        "It's your turn. Type a number 1-9 to play (or 'quit' to quit). ");
                            } else if (up.equals("OPPONENT_TURN")) {
                                System.out.println("Waiting for opponent's move...");
                            } else if (up.startsWith("MOVE")) {
                                System.out.println("Opponent: " + up.substring(5));
                            } else if (up.equals("OK")) {
                                System.out.println("Move accepted.");
                            } else if (up.equals("INVALID")) {
                                System.out.println("Invalid move, please try again.");
                            } else if (up.equals("NOT_YOUR_TURN")) {
                                System.out.println("It's not your turn.");
                            } else if (up.equals("WIN")) {
                                System.out.println("You won!");
                            } else if (up.equals("LOSE")) {
                                System.out.println("You lost.");
                            } else if (up.equals("TIE") || up.equals("DRAW")) {
                                System.out.println("Tie.");
                            } else if (up.equals("OPPONENT_DISCONNECTED")) {
                                System.out.println("Opponent disconnected. Game over.");
                            } else if (up.equals("BYE")) {
                                System.out.println("Game ended by server. Closing client.");
                                try {
                                    socket.close();
                                } catch (IOException ignored) {
                                }
                                running = false;
                                break;
                            } else if (up.equals("UNKNOWN")) {
                                System.out.println("Unknown message from server: " + up);
                            } else {

                                System.out.println(up);
                            }
                        }
                    } catch (IOException e) {
                        if (running)
                            System.err.println("Error reading from server: " + e.getMessage());
                    } finally {
                        running = false;
                    }
                }, "server-reader");
                reader.setDaemon(true);
                reader.start();

                while (running) {
                    String input = stdin.readLine();
                    if (input == null)
                        break;
                    input = input.trim();
                    if (input.isEmpty())
                        continue;
                    if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                        System.out.println("Closing client.");
                        break;
                    }

                    if (input.matches("^[1-9]$")) {
                        serverOut.write("MOVE " + input);
                        serverOut.newLine();
                        serverOut.flush();

                        continue;
                    }

                    if (input.toUpperCase().startsWith("MOVE")) {
                        serverOut.write(input);
                        serverOut.newLine();
                        serverOut.flush();
                        continue;
                    }

                    System.out.println("Unrecognized command. Type 1-9 or 'quit'.");
                }

                running = false;
            }
        }
    }
}
