package etatcalculette;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Saisissez l'operateur");
        String c = sc.nextLine();
        System.out.println("Saisissez le 1er nombre");// saisir avec , et pas .
        Float c1 = sc.nextFloat();
        System.out.println("Saisissez le 2nd nombre");
        Float c2 = sc.nextFloat();
        Calculette calc = new Calculette();
        if (c.equals("+")) {
            calc.setEtat(new EtatAddition());
        } else if (c.equals("-")) {
            calc.setEtat(new EtatSoustraction());
        } else {
            System.out.println("Opérateur non supporté");
            sc.close();
            return;
        }
        float resultat = calc.calculate(c1, c2);
        System.out.println("Résultat: " + resultat);
        sc.close();
    }
}
