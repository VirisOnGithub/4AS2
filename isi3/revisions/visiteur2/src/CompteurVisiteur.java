/**
 * Visiteur COMPTEUR — À IMPLÉMENTER
 * ====================================
 * Ce visiteur compte le nombre TOTAL de noeuds dans l'arbre
 * (feuilles ET noeuds internes).
 *
 * Exemple :
 *   Multiplication(
 *     Addition(Nombre(3), Nombre(5)),
 *     Nombre(2))
 *
 *   → 5 noeuds  (Multiplication, Addition, Nombre(3), Nombre(5), Nombre(2))
 *
 * STRATÉGIE : pas besoin de pile ici. Un simple compteur entier suffit.
 * Chaque méthode visiter() incrémente le compteur ET visite
 * récursivement les enfants.
 *
 * Appelle getNombreNoeuds() après la visite pour obtenir le résultat.
 *
 * TODO : implémenter toutes les méthodes visiter().
 */
public class CompteurVisiteur implements Visiteur {

    private int compteur = 0;

    /**
     * Retourne le nombre de noeuds comptés.
     */
    public int getNombreNoeuds() {
        return compteur;
    }

    @Override
    public void visiterNombre(Nombre n) {
        compteur++;
    }

    @Override
    public void visiterAddition(Addition a) {
        a.gauche.accepter(this);
        a.droite.accepter(this);
        compteur++;
    }

    @Override
    public void visiterSoustraction(Soustraction s) {
        s.gauche.accepter(this);
        s.droite.accepter(this);
        compteur++;
    }

    @Override
    public void visiterMultiplication(Multiplication m) {
        m.gauche.accepter(this);
        m.droite.accepter(this);
        compteur++;
    }

    @Override
    public void visiterDivision(Division d) {
        d.gauche.accepter(this);
        d.droite.accepter(this);
        compteur++;
    }

    @Override
    public void visiterNegation(Negation n) {
        n.operande.accepter(this);
        compteur++;
    }

    // TODO — implémenter les méthodes visiter() de l'interface Visiteur

}
