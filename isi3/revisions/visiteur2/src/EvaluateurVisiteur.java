/**
 * Visiteur ÉVALUATEUR — À IMPLÉMENTER
 * =====================================
 * Ce visiteur parcourt l'arbre d'expression et calcule
 * la valeur numérique du résultat.
 *
 * STRATÉGIE : utilise une pile (Deque<Double>) pour stocker
 * les résultats intermédiaires.
 *
 * Principe pour un noeud binaire (ex: Addition) :
 *   1. Visiter récursivement gauche  → empile son résultat
 *   2. Visiter récursivement droite  → empile son résultat
 *   3. Dépiler les deux valeurs, les additionner, empiler le résultat
 *
 * Après avoir appelé visiteur.accepter(racine), appelle getResultat()
 * pour obtenir la valeur finale.
 *
 * TODO : implémenter toutes les méthodes visiter().
 */
import java.util.ArrayDeque;
import java.util.Deque;

public class EvaluateurVisiteur implements Visiteur {

    private final Deque<Double> pile = new ArrayDeque<>();

    /**
     * Retourne le résultat du calcul après la visite.
     * Lève une exception si la pile est vide ou mal formée.
     */
    public double getResultat() {
        if (pile.size() != 1) {
            throw new IllegalStateException("La pile devrait contenir exactement 1 élément, contient : " + pile.size());
        }
        return pile.pop();
    }

    @Override
    public void visiterNombre(Nombre n) {
        pile.add(n.valeur);
    }

    @Override
    public void visiterAddition(Addition a) {
        a.gauche.accepter(this);
        a.droite.accepter(this);
        var gauche = pile.pop();
        var droite = pile.pop();
        pile.add(gauche + droite);
    }

    @Override
    public void visiterSoustraction(Soustraction s) {
        s.gauche.accepter(this);
        s.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche - droite);
    }

    @Override
    public void visiterMultiplication(Multiplication m) {
        m.gauche.accepter(this);
        m.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche * droite);
    }

    @Override
    public void visiterDivision(Division d) {
        d.gauche.accepter(this);
        d.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche / droite);
    }

    @Override
    public void visiterNegation(Negation n) {
        n.operande.accepter(this);
        var op = pile.pop();
        pile.add(-op);
    }

    // TODO — implémenter les méthodes visiter() de l'interface Visiteur

}
