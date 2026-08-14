package metier;

import dao.JoueurDAO;

public class AfficherJoueur {
    public static void main(String[] args) throws ClassNotFoundException {
        if ( args.length < 1) {
            System.out.println("Usage: java AfficherJoueur <jno>");
            return;
        }

        int jnoInput = Integer.parseInt(args[0]);

        Joueur j = JoueurDAO.findById(jnoInput);
        System.out.println(j);
    }
}
