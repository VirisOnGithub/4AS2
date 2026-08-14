/**
 * Noeud représentant une addition : gauche + droite
 * FICHIER COMPLET — ne pas modifier.
 */
public class Addition extends OperationBinaire {

    public Addition(NoeudExpression gauche, NoeudExpression droite) {
        super(gauche, droite);
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterAddition(this);
    }
}
