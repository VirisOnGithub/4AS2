package exerciceCafe1;


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
        System.out.println("recette de votre boisson : ");
        boisson.recette();
        System.out.println("Prix a payer: " + boisson.getPrix());
    }

    public static void main(String[] args) {
        Distributeur distributeur = new Distributeur();
        distributeur.commanderBoisson();
    }
}
