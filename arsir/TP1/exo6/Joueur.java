import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Joueur extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final char symbol;
    private final Serveur serveur;
    private volatile boolean running = true;

    public Joueur(Socket socket, char symbol, Serveur serveur) throws IOException {
        this.socket = socket;
        this.symbol = symbol;
        this.serveur = serveur;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        try {

            while (running) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                serveur.onMessageFromPlayer(this, line.trim());
            }
        } catch (IOException e) {

        } finally {
            running = false;
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            serveur.onPlayerDisconnected(this);
        }
    }

    public char getSymbol() {
        return symbol;
    }

    public void send(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {

            running = false;
            try {
                socket.close();
            } catch (IOException ignored) {
            }

        }
    }

    public void stopPlayer() {
        running = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }

        this.interrupt();
    }
}
