public class Jeu {
    private final char[][] board = new char[3][3];

    public Jeu() {
        reset();
    }

    public synchronized void reset() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = ' ';
            }
        }
    }

    // Convertit une position 1-9 en coordonnées (ligne, colonne)
    private int[] posToRC(int pos) {
        if (pos < 1 || pos > 9)
            return null;
        int p = pos - 1;
        return new int[] { p / 3, p % 3 };
    }

    // Vérifie si un coup est valide
    public synchronized boolean isValidMove(int pos) {
        int[] rc = posToRC(pos);
        if (rc == null)
            return false;
        return board[rc[0]][rc[1]] == ' ';
    }

    // Applique un coup sur le plateau
    public synchronized boolean applyMove(int pos, char symbol) {
        int[] rc = posToRC(pos);
        if (rc == null)
            return false;
        if (board[rc[0]][rc[1]] != ' ')
            return false;
        board[rc[0]][rc[1]] = symbol;
        return true;
    }

    // Vérifie si un joueur a gagné ou si la partie est nulle
    public synchronized char checkWinner() {

        for (int r = 0; r < 3; r++) {
            if (board[r][0] != ' ' && board[r][0] == board[r][1] && board[r][1] == board[r][2])
                return board[r][0];
        }

        for (int c = 0; c < 3; c++) {
            if (board[0][c] != ' ' && board[0][c] == board[1][c] && board[1][c] == board[2][c])
                return board[0][c];
        }

        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0];
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2];

        if (isFull())
            return 'T';
        return '\0';
    }

    public synchronized boolean isFull() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == ' ')
                    return false;
        return true;
    }

    // Représentation du plateau sous forme de string
    public synchronized String boardString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                sb.append(board[r][c]);
            }
            if (r < 2)
                sb.append('/');
        }
        return sb.toString();
    }

    public synchronized String prettyBoard() {
        return prettyBoard(false);
    }

    // Représentation du plateau sous forme lisible
    public synchronized String prettyBoard(boolean showNumbers) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 3; r++) {
            sb.append(" ");
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    if (showNumbers) {
                        sb.append(String.valueOf(r * 3 + c + 1));
                    } else {
                        sb.append("-");
                    }
                } else {
                    sb.append(String.valueOf(board[r][c]));
                }
                if (c < 2)
                    sb.append(" | ");
            }
            sb.append('\n');
            if (r < 2)
                sb.append("---+---+---\n");
        }
        return sb.toString();
    }
}
