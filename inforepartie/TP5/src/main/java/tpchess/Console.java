package tpchess;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        CasseTete jeu = new CasseTete();
        while ( !jeu.estFini() ) {
            System.out.println(jeu);
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if ( !jeu.jouer(i) ) {
                System.out.println("Coup invalide");
            }
        }
        if (jeu.gagne()) {
            System.out.println("Bravo, vous avez gagné !");
        } else {
            System.out.println("Dommage, vous avez perdu !");
        }
    }
}
