/**
 * Programme principal — FICHIER COMPLET, ne pas modifier.
 * =========================================================
 * Lance les tests sur différentes expressions.
 *
 * Résultats attendus si tout est correctement implémenté :
 *
 *  --- Expression 1 : (3 + 5) * 2 ---
 *  Affichage    : ((3.0 + 5.0) * 2.0)
 *  Résultat     : 16.0
 *  Nb noeuds    : 5
 *
 *  --- Expression 2 : 10 - (-(4 / 2)) ---
 *  Affichage    : (10.0 - (-(4.0 / 2.0)))
 *  Résultat     : 12.0
 *  Nb noeuds    : 6
 *
 *  --- Expression 3 : (7 + 3) * (10 - (4 / 2)) ---
 *  Affichage    : ((7.0 + 3.0) * (10.0 - (4.0 / 2.0)))
 *  Résultat     : 80.0
 *  Nb noeuds    : 9
 */
public class Main {

    public static void main(String[] args) {

        // Expression 1 : (3 + 5) * 2
        NoeudExpression expr1 = new Multiplication(
                new Addition(new Nombre(3), new Nombre(5)),
                new Nombre(2)
        );

        // Expression 2 : 10 - (-(4 / 2))
        NoeudExpression expr2 = new Soustraction(
                new Nombre(10),
                new Negation(new Division(new Nombre(4), new Nombre(2)))
        );

        // Expression 3 : (7 + 3) * (10 - (4 / 2))
        NoeudExpression expr3 = new Multiplication(
                new Addition(new Nombre(7), new Nombre(3)),
                new Soustraction(
                        new Nombre(10),
                        new Division(new Nombre(4), new Nombre(2))
                )
        );

        tester("(3 + 5) * 2", expr1);
        tester("10 - (-(4 / 2))", expr2);
        tester("(7 + 3) * (10 - (4 / 2))", expr3);
    }

    private static void tester(String titre, NoeudExpression expression) {
        System.out.println("\n--- Expression : " + titre + " ---");

        AfficheurVisiteur afficheur = new AfficheurVisiteur();
        expression.accepter(afficheur);
        System.out.println("Affichage  : " + afficheur.getExpression());

        EvaluateurVisiteur evaluateur = new EvaluateurVisiteur();
        expression.accepter(evaluateur);
        System.out.println("Résultat   : " + evaluateur.getResultat());

        CompteurVisiteur compteur = new CompteurVisiteur();
        expression.accepter(compteur);
        System.out.println("Nb noeuds  : " + compteur.getNombreNoeuds());
    }
}
