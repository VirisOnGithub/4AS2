import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Serveur {
    private static final int PORT = 12345;
    private Joueur playerX;
    private Joueur playerO;
    private Jeu jeu;
    private boolean firstBoardSent = false;

    private boolean handlingDisconnection = false;

    public Serveur() {
        this.jeu = new Jeu();
    }

    public static void main(String[] args) throws IOException {
        Serveur s = new Serveur();
        s.run();
    }

    public void run() throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Tic Tac Toe server listening on port " + PORT);

            while (true) {
                System.out.println("Waiting for two players...");
                Socket s1 = server.accept();
                System.out.println("Player 1 connected: " + s1.getRemoteSocketAddress());
                playerX = new Joueur(s1, 'X', this);

                Socket s2 = server.accept();
                System.out.println("Player 2 connected: " + s2.getRemoteSocketAddress());
                playerO = new Joueur(s2, 'O', this);

                playerX.start();
                playerO.start();

                firstBoardSent = false;
                jeu.reset();

                startGame();

                cleanupPlayers();

            }
        }
    }

    private void startGame() {

        Random rnd = new Random();
        boolean xStarts = rnd.nextBoolean();
        this.xStarting = xStarts;
        Joueur current = xStarts ? playerX : playerO;
        Joueur other = xStarts ? playerO : playerX;

        if (playerX != null)
            playerX.send("START X");
        if (playerO != null)
            playerO.send("START O");

        sendBoardToBoth();

        if (current != null)
            current.send("YOUR_TURN");
        if (other != null)
            other.send("OPPONENT_TURN");

        while (true) {

            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    /* ignore */ }
            }

            char winner = jeu.checkWinner();
            if (winner == 'X' || winner == 'O') {

                if (winner == 'X') {
                    if (playerX != null)
                        playerX.send("WIN");
                    if (playerO != null)
                        playerO.send("LOSE");
                } else {
                    if (playerO != null)
                        playerO.send("WIN");
                    if (playerX != null)
                        playerX.send("LOSE");
                }
                sendBoardToBoth();

                if (playerX != null)
                    playerX.send("BYE");
                if (playerO != null)
                    playerO.send("BYE");
                if (playerX != null)
                    playerX.stopPlayer();
                if (playerO != null)
                    playerO.stopPlayer();
                break;
            } else if (winner == 'T') {
                if (playerX != null)
                    playerX.send("TIE");
                if (playerO != null)
                    playerO.send("TIE");
                sendBoardToBoth();

                if (playerX != null)
                    playerX.send("BYE");
                if (playerO != null)
                    playerO.send("BYE");
                if (playerX != null)
                    playerX.stopPlayer();
                if (playerO != null)
                    playerO.stopPlayer();
                break;
            } else {

            }

            if (playerX == null || playerO == null) {
                System.out.println("A player has disconnected. Ending game.");
                break;
            }
        }
    }

    public synchronized void onMessageFromPlayer(Joueur j, String msg) {

        if (msg == null || msg.isEmpty())
            return;
        String[] parts = msg.split("\\s+");
        if (parts.length >= 2 && "MOVE".equalsIgnoreCase(parts[0])) {
            int pos;
            try {
                pos = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                j.send("INVALID");
                return;
            }

            char expected = j.getSymbol();

            int movesPlayed = countMoves();
            char turnSymbol = (movesPlayed % 2 == 0) ? (isXStarting() ? 'X' : 'O') : (isXStarting() ? 'O' : 'X');
            if (expected != turnSymbol) {
                j.send("NOT_YOUR_TURN");
                return;
            }

            boolean ok = jeu.applyMove(pos, expected);
            if (!ok) {
                j.send("INVALID");
                return;
            }

            Joueur other = (j == playerX) ? playerO : playerX;
            j.send("OK");
            if (other != null)
                other.send("MOVE " + pos);
            sendBoardToBoth();

            if (other != null) {
                other.send("YOUR_TURN");
                j.send("OPPONENT_TURN");
            }

            this.notifyAll();
        } else {
            j.send("UNKNOWN");
        }
    }

    public synchronized void onPlayerDisconnected(Joueur j) {
        if (handlingDisconnection)
            return;
        handlingDisconnection = true;
        try {
            System.out.println("Server: player disconnected: " + j.getSymbol());

            if (j == playerX)
                playerX = null;
            else if (j == playerO)
                playerO = null;
            this.notifyAll();
        } finally {
            handlingDisconnection = false;
        }
    }

    private void sendBoardToBoth() {
        // Envoie le plateau avec les nombres pour permettre aux joueurs de faire leur
        // choix
        String pretty = jeu.prettyBoard(!firstBoardSent);
        firstBoardSent = true;
        String[] lines = pretty.split("\n");
        if (playerX != null) {
            playerX.send("BOARD");
            for (String l : lines)
                playerX.send(l);
            playerX.send("END_BOARD");
        }
        if (playerO != null) {
            playerO.send("BOARD");
            for (String l : lines)
                playerO.send(l);
            playerO.send("END_BOARD");
        }
    }

    private int countMoves() {
        String b = jeu.boardString();
        int cnt = 0;
        for (char ch : b.toCharArray())
            if (ch == 'X' || ch == 'O')
                cnt++;
        return cnt;
    }

    private boolean xStarting = true;

    private boolean isXStarting() {
        return xStarting;
    }

    private void cleanupPlayers() {
        if (playerX != null) {
            playerX.stopPlayer();
        }
        if (playerO != null) {
            playerO.stopPlayer();
        }
        try {
            if (playerX != null)
                playerX.join(1000);
        } catch (InterruptedException ignored) {
        }
        try {
            if (playerO != null)
                playerO.join(1000);
        } catch (InterruptedException ignored) {
        }
        playerX = null;
        playerO = null;
    }
}
