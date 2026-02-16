package exerciceCafe2;

import exerciceCafe1.Boisson;
import exerciceCafe1.Colombia;
import exerciceCafe1.Deca;
import exerciceCafe1.Expresso;

import java.util.Scanner;

public class Distributeur {
    public void commanderBoisson() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Quelle boisson voulez-vous? (deca/expresso/colombia)");
        String choix = sc.nextLine();

        Boisson boisson;
        switch (choix.toLowerCase()) {
            case "deca":
                boisson = new Deca();
                break;
            case "expresso":
                boisson = new Expresso();
                break;
            case "colombia":
                boisson = new Colombia();
                break;
            default:
                System.out.println("Choix invalide.");
                sc.close();
                return;
        }
        System.out.println("Vous avez choisi: " + boisson);

        double prix = boisson.getPrix();
        boolean addSupplement = true;

        while (addSupplement) {
            System.out.println("Quelle supplément voulez-vous? (sucre/lait/caramel/rien)");
            choix = sc.nextLine();

            switch (choix.toLowerCase()) {
                case "sucre":
                    System.out.println("Vous avez ajouté du sucre");
                    prix += 0.1;
                    break;
                case "lait":
                    System.out.println("Vous avez ajouté du lait");
                    prix += 0.3;
                    break;
                case "caramel":
                    System.out.println("Vous avez ajouté du caramel");
                    prix += 0.5;
                    break;
                case "rien":
                    addSupplement = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
                    sc.close();
                    return;
            }
        }

        System.out.println("recette de votre boisson : ");
        boisson.recette();
        System.out.println("Prix a payer: " + prix);
    }

    public static void main(String[] args) {
        Distributeur distributeur = new Distributeur();
        distributeur.commanderBoisson();
    }


}
