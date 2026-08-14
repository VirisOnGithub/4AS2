/**
 * Décorateur abstrait — À COMPLÉTER
 * ===================================
 * C'est la pièce centrale du pattern Décorateur.
 *
 * Cette classe :
 *   1. Implémente Flux (elle EST un Flux)
 *   2. Contient une référence vers un autre Flux (elle EN A UN)
 *
 * Ce double rôle ("est-un" ET "a-un") est la signature du pattern.
 *
 * TODO :
 *   - Ajoute un champ protégé `flux` de type Flux
 *   - Écris un constructeur qui prend un Flux en paramètre
 *   - Implémente lire() en déléguant simplement à flux.lire()
 *     (les sous-classes surchargeront ce comportement)
 */
public abstract class FluxDecorateur implements Flux {

    protected Flux flux;

    public FluxDecorateur(Flux flux) {
        this.flux = flux;
    }

    @Override
    public String lire() {
        return flux.lire();
    }



    // TODO — champ protégé vers le Flux enveloppé

    // TODO — constructeur

    // TODO — implémentation de lire() par délégation

}
