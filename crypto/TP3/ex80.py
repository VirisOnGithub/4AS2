"""
Cryptosystème de Paillier
=========================
Exercice 80 — Implémentation complète :
  1. KeyGen / Encrypt / Decrypt
  2. Démonstration expérimentale de l'homomorphisme additif
  3. Application : vote électronique chiffré
"""

import random
import math


# ─────────────────────────────────────────────
#  Utilitaires arithmétiques
# ─────────────────────────────────────────────

def is_prime_miller_rabin(n: int, k: int = 20) -> bool:
    """Test de primalité de Miller-Rabin (k tours)."""
    if n < 2:
        return False
    if n == 2 or n == 3:
        return True
    if n % 2 == 0:
        return False

    # Écriture n-1 = 2^r * d
    r, d = 0, n - 1
    while d % 2 == 0:
        r += 1
        d //= 2

    for _ in range(k):
        a = random.randrange(2, n - 1)
        x = pow(a, d, n)
        if x == 1 or x == n - 1:
            continue
        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                break
        else:
            return False
    return True


def generate_prime(bits: int) -> int:
    """Génère un entier premier aléatoire de `bits` bits."""
    while True:
        candidate = random.getrandbits(bits) | (1 << (bits - 1)) | 1  # impair, MSB=1
        if is_prime_miller_rabin(candidate):
            return candidate


def lcm(a: int, b: int) -> int:
    return a * b // math.gcd(a, b)


def mod_inverse(a: int, m: int) -> int:
    """Inverse modulaire via l'algorithme d'Euclide étendu."""
    g, x, _ = extended_gcd(a, m)
    if g != 1:
        raise ValueError(f"Pas d'inverse : gcd({a}, {m}) = {g}")
    return x % m


def extended_gcd(a: int, b: int):
    if a == 0:
        return b, 0, 1
    g, x, y = extended_gcd(b % a, a)
    return g, y - (b // a) * x, x


def L(u: int, n: int) -> int:
    """Fonction L du cryptosystème de Paillier : L(u) = (u-1)/n."""
    return (u - 1) // n

def keygen(bits: int = 1024):
    """
    Génère une paire de clés Paillier.

    Paramètres
    ----------
    bits : taille en bits de p et q (1024 recommandé)

    Retourne
    --------
    pk = (n, g)   clé publique
    sk = (λ, μ)   clé secrète
    """
    # Générer p et q distincts et premiers
    while True:
        p = generate_prime(bits)
        q = generate_prime(bits)
        if p != q:
            break

    n = p * q
    n2 = n * n

    # λ = lcm(p-1, q-1)
    lam = lcm(p - 1, q - 1)

    # g = n + 1 est un choix classique qui simplifie le calcul
    g = n + 1

    # μ = (L(g^λ mod n²))^{-1} mod n
    gl = pow(g, lam, n2)
    mu = mod_inverse(L(gl, n), n)

    pk = (n, g)
    sk = (lam, mu)
    return pk, sk


def encrypt(pk, plaintext: int) -> int:
    """
    Chiffre un message m ∈ [0, n).

    Chiffré : c = g^m · r^n  mod n²
    avec r ∈ [1, n) aléatoire et pgcd(r, n) = 1
    """
    n, g = pk
    n2 = n * n

    if not (0 <= plaintext < n):
        raise ValueError(f"Le message doit être dans [0, n). Reçu : {plaintext}")

    # Choix de r aléatoire, copremier avec n
    while True:
        r = random.randrange(1, n)
        if math.gcd(r, n) == 1:
            break

    c = (pow(g, plaintext, n2) * pow(r, n, n2)) % n2
    return c


def decrypt(pk, sk, ciphertext: int) -> int:
    """
    Déchiffre un chiffré c.

    Message : m = L(c^λ mod n²) · μ  mod n
    """
    n, g = pk
    lam, mu = sk
    n2 = n * n

    cl = pow(ciphertext, lam, n2)
    m = (L(cl, n) * mu) % n
    return m

def homomorphic_add(pk, c1: int, c2: int) -> int:
    """
    Addition homomorphe : Enc(x) · Enc(y) mod n² = Enc(x + y)
    """
    n, _ = pk
    return (c1 * c2) % (n * n)

def homomorphic_scalar_mult(pk, c: int, k: int) -> int:
    """
    Multiplication homomorphe par un scalaire : Enc(x)^k mod n² = Enc(kx)
    """
    n, _ = pk
    return pow(c, k, n * n)

def homomorphic_negation(pk, c: int) -> int:
    """
    Négation homomorphe : Enc(x)^{-1} mod n² = Enc(-x)
    """
    n, _ = pk
    return pow(c, -1, n * n)


def demo_homomorphism(pk, sk, x: int, y: int):
    """
    Vérifie expérimentalement :
      Decrypt(sk, Encrypt(pk, x) × Encrypt(pk, y) mod n²) == (x + y) mod n
    """
    print("=" * 60)
    print("2. Démonstration de l'homomorphisme additif")
    print("=" * 60)
    n, _ = pk

    cx = encrypt(pk, x)
    cy = encrypt(pk, y)

    # Produit des chiffrés dans Z_{n²}
    c_sum = homomorphic_add(pk, cx, cy)

    # Déchiffrement du produit
    decrypted = decrypt(pk, sk, c_sum)
    expected  = (x + y) % n

    print(f"  x          = {x}")
    print(f"  y          = {y}")
    print(f"  (x+y) mod n= {expected}")
    print(f"  Decrypt(Enc(x)·Enc(y) mod n²) = {decrypted}")
    print(f"  Égalité    : {'✓ OUI' if decrypted == expected else '✗ NON'}")
    print()
    
    

def encrypted_vote_demo(pk, sk):
    """
    Vote électronique simplifié.

    Chaque électeur chiffre son vote (0 = NON, 1 = OUI).
    Le serveur additionne les chiffrés sans jamais voir les votes individuels.
    Le résultat (nombre total de OUI) est obtenu en déchiffrant une seule fois.
    """
    print("=" * 60)
    print("3. Application : Vote électronique chiffré")
    print("=" * 60)

    # Simulation de votes individuels
    votes_clairs = [1, 0, 1, 1, 0, 1, 0, 1, 1, 0]   # 6 OUI, 4 NON
    print(f"  Votes individuels (secrets) : {votes_clairs}")
    print(f"  Total OUI attendu           : {sum(votes_clairs)}")
    print()

    # Chiffrement de chaque vote
    votes_chiffres = [encrypt(pk, v) for v in votes_clairs]
    print("  Chiffrements individuels générés (les premiers chiffres):")
    for i, c in enumerate(votes_chiffres):
        print(f"    Électeur {i+1}: {str(c)[:40]}...")
    print()

    # Agrégation homomorphe — le serveur ne déchiffre jamais les votes individuels
    n, _ = pk
    n2 = n * n
    total_chiffre = votes_chiffres[0]
    for c in votes_chiffres[1:]:
        total_chiffre = homomorphic_add(pk, total_chiffre, c)

    # Déchiffrement unique du total
    total_oui = decrypt(pk, sk, total_chiffre)

    print(f"  Total OUI (déchiffré une seule fois) : {total_oui}")
    print(f"  Résultat correct : {'✓ OUI' if total_oui == sum(votes_clairs) else '✗ NON'}")
    print()
    print("  Propriété clé : le serveur de vote n'a jamais accès")
    print("  aux votes individuels — seul le total est révélé.")
    print("=" * 60)

if __name__ == "__main__":
    print("=" * 60)
    print("   Cryptosystème de Paillier — Exercice 80")
    print("=" * 60)

    # Utilisation de clés 512 bits pour la rapidité de la démo
    # (remplacer par 1024 pour la sécurité réelle)
    BITS = 512
    print(f"\n1. Génération des clés ({BITS} bits)… ", end="", flush=True)
    pk, sk = keygen(bits=BITS)
    n, g = pk
    lam, mu = sk
    print("OK")
    print(f"   n  (premiers chiffres) : {str(n)[:40]}…")
    print(f"   g  (premiers chiffres) : {str(g)[:40]}…")
    print()

    # Test chiffrement / déchiffrement basique
    print("── Test Encrypt / Decrypt ──────────────────────")
    for m in [0, 1, 42, 2**30, n - 1]:
        c  = encrypt(pk, m)
        m2 = decrypt(pk, sk, c)
        status = "✓" if m2 == m else "✗"
        print(f"   {status}  m = {m}  →  Decrypt(Encrypt(m)) = {m2}")
    print()

    # Démonstration de l'homomorphisme
    demo_homomorphism(pk, sk, x=12345, y=67890)

    # Application : vote chiffré
    encrypted_vote_demo(pk, sk)