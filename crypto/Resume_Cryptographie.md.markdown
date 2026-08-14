# 📚 Résumé — Cours de Cryptographie
> UCBL · Polytech · 5A Informatique · Gerald Gavin

---

## Chapitre 1 — Rappels d'Arithmétique

### Fonction d'Euler φ(n)
- **Définition** : φ(n) = nombre d'entiers dans {1, …, n−1} premiers avec n
- **Formule** : si n = p1^e1 · … · pr^er, alors φ(n) = ∏ (pᵢ − 1) · pᵢ^(eᵢ−1)
- **Cas importants** :
  - φ(p) = p − 1 (p premier)
  - φ(pq) = (p−1)(q−1)

### Théorème de Bézout
> pgcd(a, b) = 1 ⟺ ∃ u, v ∈ ℤ tels que au + bv = 1

- Les entiers u, v s'appellent **coefficients de Bézout**
- Calcul efficace via l'**algorithme d'Euclide étendu**

### Congruences
- a ≡ b (mod n) ⟺ n | (a − b)
- Compatible avec + et × : si a ≡ a' et b ≡ b' (mod n), alors a+b ≡ a'+b' et ab ≡ a'b' (mod n)
- (a + b) mod n = ((a mod n) + (b mod n)) mod n — idem pour ×

### Théorème de Fermat-Euler ⭐
> Pour tout x premier avec n : **x^φ(n) ≡ 1 (mod n)**

- Petit théorème de Fermat : si n est premier, x^n ≡ x (mod n)
- Conséquence fondamentale : x^(1 + k·φ(n)) ≡ x (mod n)
- Utilisé dans **RSA, tests de primalité, exponentiation modulaire**

### Test de Fermat (primalité)
```
TestFermat(n) → retourner (2^(n−1) mod n == 1)
```
- Efficace mais pas parfait (faux positifs rares)
- Probabilité d'erreur < 10⁻⁶⁰ pour des nombres de 300 chiffres

### Arithmétique modulaire ℤ/nℤ
- Anneau de n classes d'équivalence : {0, 1, …, n−1}
- **Éléments inversibles** : x est inversible ⟺ pgcd(x, n) = 1
- Cardinal de (ℤ/nℤ)* = φ(n)
- **Calcul de l'inverse** : via Euclide étendu → x⁻¹ = a mod n

---

## Chapitre 2 — Algorithmique

### Complexité polynomiale
- Algorithme **efficace** ⟺ complexité polynomiale en la **taille de l'entrée** (nombre de bits)
- ⚠️ La taille de n est log₂(n), pas n lui-même !
- Un algorithme en O(n) sur l'entrée n est **exponentiel** en la taille de l'entrée

### Paramètre de sécurité λ
- Un schéma est sûr si toute attaque polynomiale ne réussit qu'avec proba O(2^−λ)
- Actuellement : λ ≈ 100 (un attaquant ne peut pas faire 2^100 opérations)

### Factorisation
- **Pas d'algorithme polynomial connu** pour factoriser n = pq
- Algorithme naïf : O(2^tₙ) — exponentiel
- **Conjecture fondamentale** : 95% de la sécurité internet repose sur la difficulté de factoriser

### Exponentiation modulaire rapide ⭐
```
ExpMod(a, b = (bₙ…b₀)₂, c)
  e = 1
  pour k ← n à 0
    e = e² mod c
    si bₖ = 1 : e ← (e × a) mod c
  retourner e
```
- Complexité : **O(log₂b × log²c)** — polynomiale !
- Idée : a^16 = ((a²)²)² → 4 multiplications au lieu de 15

### Réduction polynomiale
- PA ≤ PB : « si on sait résoudre B efficacement, on sait résoudre A »
- **Contraposée utile** : si A est difficile, alors B est difficile
- Exemple : trouver φ(n) à partir de n est aussi difficile que factoriser n

### Informatique quantique ⚠️
- L'ordinateur quantique résoudrait la factorisation en temps polynomial → RSA cassé
- Alternatives post-quantiques : SIS, LWE, SVP (réseaux euclidiens)

---

## Chapitre 3 — Cryptographie Symétrique

### Principes fondamentaux
- **Conclusion 1** : la sécurité ne peut pas reposer sur le secret de l'algorithme
- **Conclusion 2** : la clé doit être secrète et le nombre de clés doit être grand
- **Conclusion 4** : un crypto est sûr si un attaquant ne retrouve le message qu'avec proba négligeable (< 10⁻³⁰)

### Code de César → trop faible
- Seulement 25 clés possibles → attaque par force brute triviale
- Extension aux permutations (26! clés) : toujours cassé par **analyse fréquentielle**

### Crypto symétrique moderne (AES, DES…)
- Message converti en binaire, découpé en blocs
- Deux opérations alternées : **masquer** (XOR) + **permuter** les bits
- Après plusieurs tours → chiffré indistinguable d'un message aléatoire
- ✅ Très efficace
- ❌ Problème : il faut **partager une clé secrète au préalable**

### Sécurité CPA (Chosen Plaintext Attack)
- L'attaquant peut choisir des messages et obtenir leurs encryptions
- Un bon cryptosystème doit résister à ce type d'attaque

---

## Chapitre 4 — Chiffrement Asymétrique

### Principe
- **KeyGen** → paire (pk publique, sk privée)
- **Encrypt(pk, m)** → chiffré c (tout le monde peut chiffrer)
- **Decrypt(sk, c)** → message m (seul le possesseur de sk peut déchiffrer)
- La clé privée **ne transite jamais** sur le réseau

### Niveaux de sécurité
| Niveau | Description |
|--------|-------------|
| **Sécurité sens unique** | L'attaquant sans a priori ne retrouve pas m |
| **Sécurité sémantique** | L'attaquant choisit m₀, m₁ ; ne peut pas distinguer leur chiffré (proba ≤ 1/2 + négligeable) |

- La sécurité sémantique implique que **Encrypt doit être probabiliste**

### RSA ⭐
```
KeyGen(λ) :
  p, q grands premiers → n = pq, φ = (p−1)(q−1)
  choisir e tel que pgcd(e, φ) = 1
  d = e⁻¹ mod φ
  pk = (n, e) ;  sk = d

Encrypt(pk, m) : c = mᵉ mod n
Decrypt(sk, c) : m = cᵈ mod n
```

**Preuve de correction** : (mᵉ)ᵈ = m^(ed) = m^(ed mod φ(n)) = m¹ = m

**Failles connues** :
- RSA de base **n'est pas sémantiquement sûr** (déterministe)
- e ne peut pas valoir 2 (pgcd(2, φ) = 2 ≠ 1)
- Si m < n^(1/3) et e = 3 → retrouver m depuis c = m³ (pas de réduction mod n)
- Réutiliser des nombres premiers dans plusieurs clés → dangereux (pgcd trivial)

---

## Chapitre 5 — Signature Numérique

### Propriétés requises
1. **Authentique** — identité du signataire vérifiable
2. **Infalsifiable** — impossible de se faire passer pour un autre
3. **Non réutilisable** — liée au document signé
4. **Inaltérable** — document signé non modifiable
5. **Irrévocable** — le signataire ne peut pas nier

> ⚠️ La signature doit dépendre à la fois de **l'expéditeur ET du message**

### Signature RSA (avec fonction de hachage H)
```
sigₛₖ(m) : h ← H(m) ; retourner σ = hᵈ mod n
verpₖ(m, σ) : vrai si σᵉ = H(m) mod n
```

### Résistance aux collisions (nécessaire)
- **Faiblement résistante** : difficile de trouver m' ≠ m avec H(m') = H(m)
- **Fortement résistante** : difficile de trouver (m, m') avec H(m) = H(m')
- La résistance forte est **nécessaire** pour la sécurité de la signature

---

## Chapitre 6 — Cryptosystèmes Homomorphes

### Définition
Un cryptosystème est **homomorphe** s'il permet d'effectuer des calculs sur des données chiffrées sans les déchiffrer.

| Type | Opération | Propriété |
|------|-----------|-----------|
| Additif (⊕) | c ⊕ c' | Decrypt(c ⊕ c') = m + m' |
| Multiplicatif (⊗) | c ⊗ c' | Decrypt(c ⊗ c') = m × m' |
| **Pleinement homomorphe** | ⊕ et ⊗ | Toute fonction évaluable |

### Applications
- **Cloud sécurisé** : calculs sur données chiffrées sans les déchiffrer
- **Vote électronique** : cᵢ = Encrypt(0 ou 1) → somme homomorphe → résultat sans révéler les votes individuels
- **Requête secrète** : interroger une base de données sans révéler la requête
- **Machine Learning** : classifieurs sur données distribuées et confidentielles

### Cryptosystème de Paillier ⭐
```
KeyGen : n = pq, ρ = n⁻¹ mod φ(n) ; pk = n, sk = ρ
Encrypt(m) : choisir r aléatoire → c = (1 + mn)·rⁿ mod n²
Decrypt(c) : r = c^ρ mod n → m = (c·r⁻ⁿ mod n² − 1) / n
```

- **Additif homomorphe** : c ⊕ c' = c·c' mod n² → Decrypt = m + m'
- **Sémantiquement sûr** (probabiliste, basé sur la n-residuosity)
- Permet la **randomisation des encryptions** et les **preuves de décryption**

### Cryptosystème de Van Dijk (2010)
- Chiffre un bit m ∈ {0,1} : c = kp + 2e + m (p secret, e petit)
- Déchiffrement : (c mod p) mod 2
- Pleinement homomorphe (+ et ×) sous contrainte sur la taille des erreurs

---

## Chapitre 7 — Calcul Multi-Parties Sécurisé (MPC)

### Objectif
T parties avec données secrètes x₁,…,xT souhaitent calculer f(x₁,…,xT) sans révéler leurs données individuelles.

### Cas idéal
Existence d'une **tierce partie de confiance T** qui :
1. Reçoit toutes les entrées
2. Calcule le résultat
3. Distribue les sorties

### Niveaux de malhonnêteté
- **Passive** : respecte le protocole mais essaie d'extraire des infos
- **Active** : dévie arbitrairement du protocole

### Protocoles importants

#### Somme (avec Paillier)
1. Alice envoie Encrypt(xA) à Bob
2. Bob envoie xB à Alice
3. Alice envoie (r, xA) à Bob pour vérification
4. Bob vérifie et retourne xA + xB

#### Multiplication homomorphe
- Si Π est additif mais pas multiplicatif :
  1. Bob masque x,y avec des aléas r,s → envoie Encrypt(x+r), Encrypt(y+s)
  2. Alice décrypte, multiplie, re-chiffre (x+r)(y+s)
  3. Bob retire les masques → obtient Encrypt(xy)

#### Produit scalaire (ProdScal avec Paillier)
- Alice chiffre chaque composante uᵢ
- Bob calcule homomorphiquement ∑ vᵢ · Encrypt(uᵢ)
- Alice déchiffre et partage le résultat

#### Transfert Inconscient (TI)
- Bob obtient la réponse à la question i sans révéler i
- Alice ne sait pas quelle réponse Bob a obtenue

### Théorème fondamental
> Toute fonctionnalité peut être implémentée de manière sécurisée, quel que soit le comportement de la partie malhonnête.

---

## ⚡ Points Clés à Retenir

| Concept | Résumé |
|---------|--------|
| **φ(pq)** | = (p−1)(q−1) |
| **Fermat-Euler** | x^φ(n) ≡ 1 (mod n) si pgcd(x,n)=1 |
| **ExpMod** | Calcule aᵇ mod c en O(log b · log²c) |
| **Factorisation** | Pas d'algorithme polynomial connu → base de RSA |
| **RSA** | pk=(n,e), sk=d ; Enc=mᵉ mod n ; Dec=cᵈ mod n |
| **Sécurité sémantique** | Encrypt doit être probabiliste |
| **Paillier** | Additif homomorphe + sémantiquement sûr |
| **Signature** | Hacher avant de signer ; H doit être fortement résistante aux collisions |
| **MPC** | Calcul collectif sans révéler les entrées individuelles |
| **Post-quantique** | LWE, SIS, SVP résistent aux ordinateurs quantiques |

---

## 🔑 Formules Essentielles

```
φ(n) = ∏ (pᵢ−1)·pᵢ^(eᵢ−1)          Fonction d'Euler
x^φ(n) ≡ 1 (mod n)                    Fermat-Euler
ed ≡ 1 (mod φ(n))                     Relation clés RSA
RSA.Encrypt(m) = mᵉ mod n
RSA.Decrypt(c) = cᵈ mod n
Paillier.Encrypt(m) = (1+mn)·rⁿ mod n²
c ⊕ c' = c·c' mod n²                  Homomorphie Paillier
```
