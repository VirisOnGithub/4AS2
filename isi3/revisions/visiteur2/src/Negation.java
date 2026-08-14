/**
 * Noeud représentant une négation unaire : -operande
 * (opérateur unaire — un seul enfant)
 * FICHIER COMPLET — ne pas modifier.
 */
public class Negation implements NoeudExpression {

    public final NoeudExpression operande;

    public Negation(NoeudExpression operande) {
        this.operande = operande;
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterNegation(this);
    }
}
