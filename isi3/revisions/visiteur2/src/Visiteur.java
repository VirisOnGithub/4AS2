/**
 * Interface Visiteur — À COMPLÉTER
 * =================================
 * Déclare une méthode visiter() pour chaque type de noeud de l'arbre.
 *
 * Types de noeuds existants :
 *   - Nombre        (feuille, contient une valeur double)
 *   - Addition      (noeud binaire : gauche + droite)
 *   - Soustraction  (noeud binaire : gauche - droite)
 *   - Multiplication(noeud binaire : gauche * droite)
 *   - Division      (noeud binaire : gauche / droite)
 *   - Negation      (noeud unaire  : -operande)
 *
 * TODO : ajouter ici les 6 signatures de méthodes visiter().
 */
public interface Visiteur {

    // TODO — déclare les méthodes visiter() pour chaque type de noeud

    public void visiterNombre(Nombre n);
    public void visiterAddition(Addition a);
    public void visiterSoustraction(Soustraction s);
    public void visiterMultiplication(Multiplication m);
    public void visiterDivision(Division d);
    public void visiterNegation(Negation n);
}
