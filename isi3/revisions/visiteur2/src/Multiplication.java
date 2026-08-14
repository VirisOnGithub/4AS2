/**
 * Noeud représentant une multiplication : gauche * droite
 * FICHIER COMPLET — ne pas modifier.
 */
public class Multiplication extends OperationBinaire {

    public Multiplication(NoeudExpression gauche, NoeudExpression droite) {
        super(gauche, droite);
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterMultiplication(this);
    }
}
