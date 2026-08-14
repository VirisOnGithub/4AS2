/**
 * Visiteur AFFICHEUR — À IMPLÉMENTER
 * =====================================
 * Ce visiteur parcourt l'arbre et construit une représentation
 * textuelle de l'expression en notation infixe COMPLÈTEMENT PARENTHÉSÉE.
 *
 * Exemples de sorties attendues :
 *   Nombre(42)                          →  "42.0"
 *   Addition(Nombre(3), Nombre(5))       →  "(3.0 + 5.0)"
 *   Negation(Nombre(7))                  →  "(-7.0)"
 *   Multiplication(
 *     Addition(Nombre(3), Nombre(5)),
 *     Nombre(2))                         →  "((3.0 + 5.0) * 2.0)"
 *
 * STRATÉGIE : utilise un Deque<String> de la même manière que
 * l'EvaluateurVisiteur, mais en empilant des chaînes de caractères.
 *
 * Après la visite, appelle getExpression() pour récupérer la chaîne.
 *
 * TODO : implémenter toutes les méthodes visiter().
 */
import java.util.ArrayDeque;
import java.util.Deque;

public class AfficheurVisiteur implements Visiteur {

    private final Deque<String> pile = new ArrayDeque<>();

    /**
     * Retourne la représentation textuelle de l'expression.
     */
    public String getExpression() {
        if (pile.size() != 1) {
            throw new IllegalStateException("La pile devrait contenir exactement 1 élément, contient : " + pile.size());
        }
        return pile.pop();
    }

    @Override
    public void visiterNombre(Nombre n) {
        pile.add(String.valueOf(n.valeur));
    }

    @Override
    public void visiterAddition(Addition a) {
        a.gauche.accepter(this);
        a.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche + " + " + droite);
    }

    @Override
    public void visiterSoustraction(Soustraction s) {
        s.gauche.accepter(this);
        s.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche + " - " + droite);
    }

    @Override
    public void visiterMultiplication(Multiplication m) {
        m.gauche.accepter(this);
        m.droite.accepter(this);
        var droite = pile.pop();
        var gauche = pile.pop();
        pile.add(gauche + " * " + droite);
    }

    @Override
    public void visiterDivision(Division d) {
        d.gauche.accepter(this);
        d.droite.accepter(this);
        var gauche = pile.pop();
        var droite = pile.pop();
        pile.add(gauche + " / " + droite);
    }

    @Override
    public void visiterNegation(Negation n) {
        n.operande.accepter(this);
        var op = pile.pop();
        pile.add("-" + op);
    }

    // TODO — implémenter les méthodes visiter() de l'interface Visiteur

}
