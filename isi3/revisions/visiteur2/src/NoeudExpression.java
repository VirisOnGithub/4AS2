/**
 * EXERCICE — Pattern Visiteur (niveau intermédiaire)
 * ===================================================
 * Contexte : tu dois construire un évaluateur d'expressions arithmétiques
 * représentées sous forme d'arbre (AST — Abstract Syntax Tree).
 *
 * Un arbre d'expression pour "  (3 + 5) * 2  " ressemble à :
 *
 *          Multiplication
 *         /              \
 *      Addition          Nombre(2)
 *      /      \
 * Nombre(3)  Nombre(5)
 *
 * TON OBJECTIF :
 *  1. Compléter l'interface Visiteur (voir Visiteur.java)
 *  2. Implémenter EvaluateurVisiteur  → calcule la valeur de l'expression
 *  3. Implémenter AfficheurVisiteur   → affiche l'expression en notation infixe
 *                                        ex: "((3 + 5) * 2)"
 *  4. Implémenter CompteurVisiteur    → compte le nombre de noeuds dans l'arbre
 *
 * INTERFACE À NE PAS MODIFIER.
 */
public interface NoeudExpression {

    /**
     * Méthode clé du pattern Visiteur.
     * Chaque noeud doit appeler la bonne méthode du visiteur.
     */
    void accepter(Visiteur visiteur);
}
