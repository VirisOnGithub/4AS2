/**
 * Composant concret — source de texte brut.
 * FICHIER COMPLET — ne pas modifier.
 *
 * C'est le "vrai" flux, celui qu'on va décorer.
 * Il retourne son contenu une seule fois, puis null.
 */
public class FluxTexte implements Flux {

    private final String contenu;
    private boolean lu = false;

    public FluxTexte(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String lire() {
        if (lu) return null;
        lu = true;
        return contenu;
    }
}
