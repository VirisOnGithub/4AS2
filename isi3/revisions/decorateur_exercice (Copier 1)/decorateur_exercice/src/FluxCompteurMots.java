/**
 * Décorateur COMPTEUR DE MOTS — À IMPLÉMENTER
 * ==============================================
 * Après avoir lu le contenu, mémorise le nombre de mots qu'il contient.
 * Un "mot" est une séquence de caractères non-blancs séparés par des espaces.
 *
 * Exemple :
 *   FluxTexte source = new FluxTexte("le chat est sur le tapis");
 *   FluxCompteurMots f = new FluxCompteurMots(source);
 *   f.lire()          → "le chat est sur le tapis"  (contenu inchangé)
 *   f.getNombreMots() → 6
 *
 * IMPORTANT : lire() retourne le contenu SANS le modifier.
 * C'est getNombreMots() qui expose la nouvelle fonctionnalité.
 *
 * CONSEIL : String.trim().split("\\s+") découpe une chaîne en mots.
 * Attention au cas où la chaîne est vide ou null.
 *
 * TODO :
 *   - Étends FluxDecorateur
 *   - Ajoute un champ privé pour stocker le nombre de mots
 *   - Écris le constructeur
 *   - Surcharge lire() : délègue, compte les mots, retourne le contenu intact
 *   - Implémente getNombreMots()
 */
public class FluxCompteurMots extends FluxDecorateur {

    // TODO

    public int getNombreMots() {
        // TODO
        return 0;
    }
}
