/**
 * Noeud représentant une division : gauche / droite
 * FICHIER COMPLET — ne pas modifier.
 */
public class Division extends OperationBinaire {

    public Division(NoeudExpression gauche, NoeudExpression droite) {
        super(gauche, droite);
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterDivision(this);
    }
}
