/**
 * Décorateur SANS PONCTUATION — À IMPLÉMENTER
 * ==============================================
 * Supprime tous les caractères de ponctuation du contenu lu.
 * Ponctuation à supprimer : . , ; : ! ? ' " ( ) - _
 *
 * Exemple :
 *   FluxTexte source = new FluxTexte("Bonjour, monde! Comment ça va?");
 *   Flux f = new FluxSansPonctuation(source);
 *   f.lire() → "Bonjour monde Comment ça va"
 *
 * CONSEIL : utilise String.replaceAll() avec une regex.
 * La regex  [.,;:!?'"()\\-_]  cible ces caractères.
 *
 * TODO :
 *   - Étends FluxDecorateur
 *   - Écris le constructeur
 *   - Surcharge lire() en supprimant la ponctuation
 */
public class FluxSansPonctuation extends FluxDecorateur {
    public FluxSansPonctuation(Flux flux) {
        super(flux);
    }

    @Override
    public String lire() {
        return flux.lire().trim().replaceAll("\\p{Punct}", "");
    }

    // TODO

}
