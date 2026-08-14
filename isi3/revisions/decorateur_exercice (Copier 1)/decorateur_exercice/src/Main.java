/**
 * Programme principal — FICHIER COMPLET, ne pas modifier.
 * =========================================================
 * Teste les décorateurs seuls ET en chaîne.
 *
 * Résultats attendus :
 *
 *  --- Test 1 : FluxTexte seul ---
 *  lu = "Bonjour, monde! Comment ça va?"
 *
 *  --- Test 2 : FluxMajuscules ---
 *  lu = "BONJOUR, MONDE! COMMENT ÇA VA?"
 *
 *  --- Test 3 : FluxSansPonctuation ---
 *  lu = "Bonjour monde Comment ça va"
 *
 *  --- Test 4 : FluxCompteurMots ---
 *  lu = "Bonjour, monde! Comment ça va?"
 *  nb mots = 4
 *
 *  --- Test 5 : chaîne Majuscules + SansPonctuation ---
 *  lu = "BONJOUR MONDE COMMENT ÇA VA"
 *
 *  --- Test 6 : chaîne SansPonctuation + Majuscules + CompteurMots ---
 *  lu = "BONJOUR MONDE COMMENT ÇA VA"
 *  nb mots = 5
 */
public class Main {

    static final String TEXTE = "Bonjour, monde! Comment ça va?";

    public static void main(String[] args) {

        System.out.println("--- Test 1 : FluxTexte seul ---");
        Flux f1 = new FluxTexte(TEXTE);
        System.out.println("lu = \"" + f1.lire() + "\"");

        System.out.println("\n--- Test 2 : FluxMajuscules ---");
        Flux f2 = new FluxMajuscules(new FluxTexte(TEXTE));
        System.out.println("lu = \"" + f2.lire() + "\"");

        System.out.println("\n--- Test 3 : FluxSansPonctuation ---");
        Flux f3 = new FluxSansPonctuation(new FluxTexte(TEXTE));
        System.out.println("lu = \"" + f3.lire() + "\"");

        System.out.println("\n--- Test 4 : FluxCompteurMots ---");
        FluxCompteurMots f4 = new FluxCompteurMots(new FluxTexte(TEXTE));
        System.out.println("lu = \"" + f4.lire() + "\"");
        System.out.println("nb mots = " + f4.getNombreMots());

        System.out.println("\n--- Test 5 : chaîne Majuscules + SansPonctuation ---");
        // Lis attentivement : quel décorateur enveloppe lequel ?
        // L'ordre a de l'importance !
        Flux f5 = new FluxSansPonctuation(
                      new FluxMajuscules(
                          new FluxTexte(TEXTE)));
        System.out.println("lu = \"" + f5.lire() + "\"");

        System.out.println("\n--- Test 6 : chaîne SansPonctuation + Majuscules + CompteurMots ---");
        FluxCompteurMots f6 = new FluxCompteurMots(
                                  new FluxMajuscules(
                                      new FluxSansPonctuation(
                                          new FluxTexte(TEXTE))));
        System.out.println("lu = \"" + f6.lire() + "\"");
        System.out.println("nb mots = " + f6.getNombreMots());
    }
}
