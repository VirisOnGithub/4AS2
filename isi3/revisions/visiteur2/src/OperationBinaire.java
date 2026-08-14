/**
 * Classe abstraite pour les opérations BINAIRES (deux opérandes).
 * FICHIER COMPLET — ne pas modifier.
 */
public abstract class OperationBinaire implements NoeudExpression {

    public final NoeudExpression gauche;
    public final NoeudExpression droite;

    public OperationBinaire(NoeudExpression gauche, NoeudExpression droite) {
        this.gauche = gauche;
        this.droite = droite;
    }
}
