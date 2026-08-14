/**
 * Noeud feuille représentant une valeur numérique.
 * FICHIER COMPLET — ne pas modifier.
 */
public class Nombre implements NoeudExpression {

    public final double valeur;

    public Nombre(double valeur) {
        this.valeur = valeur;
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterNombre(this);
    }
}
