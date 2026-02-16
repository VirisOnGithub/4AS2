package exerciceCafe4;

import java.util.Scanner;

public class Distributeur {
    private final BoissonFactory boissonFactory;
    private final SupplementFactory supplementFactory;

    public Distributeur() {
        boissonFactory = BoissonFactory.getInstance();
        supplementFactory = SupplementFactory.getInstance();
    }

    public void commanderBoisson() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Quelle boisson voulez-vous? (deca/expresso/colombia)");
        String choix = sc.nextLine();

        Boisson boisson = boissonFactory.createBoisson(choix);

        if (boisson == null) {
            System.out.println("Choix invalide.");
            sc.close();
            return;
        }

        boolean addSupplement = true;

        while (addSupplement) {
            System.out.println("Quelle supplément voulez-vous? (sucre/lait/caramel/fini)");
            choix = sc.nextLine();

            boisson = supplementFactory.addSupplement(boisson, choix);

            if (choix.equals("fini")) {
                addSupplement = false;
            }
        }

        System.out.println("Vous avez choisi: " + boisson);
        System.out.println("recette de votre boisson : ");
        boisson.recette();
        System.out.println("Prix a payer: " + boisson.getPrix());

        sc.close();
    }

    public static void main(String[] args) {
        Distributeur distributeur = new Distributeur();
        distributeur.commanderBoisson();
    }
}
