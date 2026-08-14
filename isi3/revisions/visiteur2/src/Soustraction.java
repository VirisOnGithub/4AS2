/**
 * Noeud représentant une soustraction : gauche - droite
 * FICHIER COMPLET — ne pas modifier.
 */
public class Soustraction extends OperationBinaire {

    public Soustraction(NoeudExpression gauche, NoeudExpression droite) {
        super(gauche, droite);
    }

    @Override
    public void accepter(Visiteur visiteur) {
        visiteur.visiterSoustraction(this);
    }
}
