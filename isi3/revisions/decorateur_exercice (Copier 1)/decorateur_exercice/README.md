# Exercice — Pattern Décorateur : Flux de texte

## Contexte

Tu dois implémenter un système de **flux de texte décorable**, inspiré du
pattern Décorateur (comme `java.io.BufferedReader`, `InputStreamReader`, etc.).

L'idée : chaque décorateur **enveloppe** un `Flux` existant, délègue la lecture,
et y ajoute son propre comportement — sans modifier les autres classes.

```
FluxCompteurMots
  └── FluxMajuscules
        └── FluxSansPonctuation
              └── FluxTexte("Bonjour, monde!")
```

---

## Fichiers fournis

| Fichier | Statut | Description |
|---|---|---|
| `Flux.java` | ✅ complet | Interface de base |
| `FluxTexte.java` | ✅ complet | Source de texte brut (composant concret) |
| `FluxDecorateur.java` | ✏️ **à compléter** | Classe abstraite décorateur |
| `FluxMajuscules.java` | ✏️ **à implémenter** | Met le texte en majuscules |
| `FluxSansPonctuation.java` | ✏️ **à implémenter** | Supprime la ponctuation |
| `FluxCompteurMots.java` | ✏️ **à implémenter** | Compte les mots lus |
| `Main.java` | ✅ complet | 6 tests avec résultats attendus |

---

## Ce que tu dois faire

### Étape 1 — `FluxDecorateur.java`
C'est la pièce maîtresse. Complète la classe abstraite :
- Champ `protected Flux flux`
- Constructeur `FluxDecorateur(Flux flux)`
- `lire()` qui délègue à `flux.lire()`

### Étape 2 — `FluxMajuscules.java`
Étends `FluxDecorateur`, surcharge `lire()` pour mettre en majuscules.

### Étape 3 — `FluxSansPonctuation.java`
Étends `FluxDecorateur`, supprime les caractères de ponctuation avec une regex.

### Étape 4 — `FluxCompteurMots.java`
Étends `FluxDecorateur`. Après `lire()`, stocke le nombre de mots.
Expose ce résultat via `getNombreMots()`.

---

## Résultats attendus

```
--- Test 1 : FluxTexte seul ---
lu = "Bonjour, monde! Comment ça va?"

--- Test 2 : FluxMajuscules ---
lu = "BONJOUR, MONDE! COMMENT ÇA VA?"

--- Test 3 : FluxSansPonctuation ---
lu = "Bonjour monde Comment ça va"

--- Test 4 : FluxCompteurMots ---
lu = "Bonjour, monde! Comment ça va?"
nb mots = 4

--- Test 5 : chaîne Majuscules + SansPonctuation ---
lu = "BONJOUR MONDE COMMENT ÇA VA"

--- Test 6 : chaîne SansPonctuation + Majuscules + CompteurMots ---
lu = "BONJOUR MONDE COMMENT ÇA VA"
nb mots = 5
```

---

## Compilation & exécution

```bash
cd src
javac *.java
java Main
```

---

## Indices

<details>
<summary>Indice 1 — La structure clé de FluxDecorateur</summary>

```java
public abstract class FluxDecorateur implements Flux {
    protected Flux flux;

    public FluxDecorateur(Flux flux) {
        this.flux = flux;
    }

    @Override
    public String lire() {
        return flux.lire(); // simple délégation
    }
}
```
Les sous-classes surchargeront `lire()` pour enrichir ce comportement.
</details>

<details>
<summary>Indice 2 — Pourquoi l'ordre des décorateurs compte (Test 5 vs 6)</summary>

Le décorateur le plus **externe** s'applique en dernier.
- `SansPonctuation( Majuscules( source ) )` : d'abord majuscules, puis suppression
- `Majuscules( SansPonctuation( source ) )` : d'abord suppression, puis majuscules

Le résultat final est identique ici, mais ce n'est pas toujours le cas.
</details>

<details>
<summary>Indice 3 — Compter les mots dans FluxCompteurMots</summary>

```java
@Override
public String lire() {
    String contenu = flux.lire();
    if (contenu != null && !contenu.trim().isEmpty()) {
        nombreMots = contenu.trim().split("\\s+").length;
    }
    return contenu;
}
```
</details>
