/**
 * EXERCICE — Pattern Décorateur
 * ==============================
 * Interface de base représentant un flux de texte.
 * FICHIER COMPLET — ne pas modifier.
 *
 * Toutes les classes de cet exercice (composants réels ET décorateurs)
 * implémentent cette interface.
 */
public interface Flux {

    /**
     * Lit et retourne le contenu complet du flux sous forme de String.
     * Retourne null si le flux est épuisé.
     */
    String lire();
}
