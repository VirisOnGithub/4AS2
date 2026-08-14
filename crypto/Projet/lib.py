from __future__ import annotations

import math
import secrets


def _egcd(a: int, b: int) -> tuple[int, int, int]:
    """Extended GCD: returns (g, x, y) such that a*x + b*y = g = gcd(a,b)."""
    old_r, r = a, b
    old_s, s = 1, 0
    old_t, t = 0, 1
    while r != 0:
        q = old_r // r
        old_r, r = r, old_r - q * r
        old_s, s = s, old_s - q * s
        old_t, t = t, old_t - q * t
    return old_r, old_s, old_t


def _is_probable_prime(n: int, rounds: int = 40) -> bool:
    if n < 2:
        return False
    small_primes = (2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37)
    for p in small_primes:
        if n == p:
            return True
        if n % p == 0:
            return False

    if n % 2 == 0:
        return False

    d = n - 1
    s = 0
    while d % 2 == 0:
        s += 1
        d //= 2

    for _ in range(rounds):
        a = secrets.randbelow(n - 3) + 2
        x = pow(a, d, n)
        if x in (1, n - 1):
            continue
        for _ in range(s - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                break
        else:
            return False
    return True


def _generate_prime(bits: int, rounds: int = 40) -> int:
    if bits < 2:
        raise ValueError("bits doit être >= 2")
    while True:
        candidate = secrets.randbits(bits)
        candidate |= (1 << (bits - 1)) | 1
        if _is_probable_prime(candidate, rounds=rounds):
            return candidate

def gen_random_znz(n: int) -> int:
    """Génère un entier aléatoire dans [1, n-1]."""
    return secrets.randbelow(n - 1) + 1


class Paillier:
    @staticmethod
    def KeyGen(bits: int = 1024) -> tuple[int, int]:
        """
        Génère les clés Paillier.
        Retourne (n, phi_n) où :
          - n      = p*q          (clé publique)
          - phi_n  = (p-1)*(q-1)  (clé privée, avec mu = phi_n⁻¹ mod n calculé à la volée)
        """
        p = _generate_prime(bits // 2)
        q = _generate_prime(bits // 2)
        while q == p:
            q = _generate_prime(bits // 2)

        n = p * q
        phi_n = (p - 1) * (q - 1)
        return n, phi_n

    @staticmethod
    def Encrypt(message: int, public_key: int) -> int:
        """
        Chiffre `message` ∈ [0, n) avec la clé publique n.
        c = (1 + n)^m * r^n  mod n²
          = (1 + m*n) * r^n  mod n²   (par le binôme, car (1+n)^m ≡ 1+mn mod n²)
        """
        n = public_key
        n2 = n * n

        # Espace des messages : Z_n (supporte aussi les entiers négatifs via modulo n)
        message %= n

        # r ∈ Z_n^* (doit être inversible mod n, sinon les exposants négatifs échouent)
        while True:
            r = secrets.randbelow(n - 1) + 1
            if math.gcd(r, n) == 1:
                break

        # ✅ pow(..., mod) pour les deux exponentiations
        c = pow(1 + n, message, n2) * pow(r, n, n2) % n2
        return c

    @staticmethod
    def Decrypt(c: int, private_key: int, public_key: int) -> int:
        """
        Déchiffre c avec la clé privée phi_n et la clé publique n.

        Étapes (avec g = n+1, clé privée = phi_n) :
          1. u  = c^phi_n mod n²
          2. L(u) = (u - 1) // n          ← fonction L de Paillier
          3. mu = phi_n⁻¹ mod n
          4. m  = L(u) * mu mod n
        """
        n = public_key
        phi = private_key
        n2 = n * n
        
        u = pow(c, phi, n2)

        l_u = (u - 1) // n

        mu = pow(phi, -1, n)

        m = l_u * mu % n
        return m

    @staticmethod
    def DecodeSigned(m: int, public_key: int) -> int:
        """Interprète un élément de Z_n comme entier signé dans [-n/2, n/2)."""
        n = public_key
        return m - n if m > (n // 2) else m

    @staticmethod
    def DecryptSigned(c: int, private_key: int, public_key: int) -> int:
        """Déchiffre puis convertit en entier signé (utile si des soustractions ont eu lieu)."""
        return Paillier.DecodeSigned(Paillier.Decrypt(c, private_key, public_key), public_key)
    
    @staticmethod
    def DecryptPlus(X: int, private_key: int, public_key: int) -> tuple[int, int]:
        """
        Variante de déchiffrement qui récupère (x, r) depuis :
            X = (1 + x*n) * r^n  mod n²

        Args:
            X           : chiffré
            private_key : phi_n = (p-1)*(q-1)
            public_key  : n

        Returns:
            (x, r) : message et aléa d'origine
        """
        n   = public_key
        phi = private_key
        n2  = n * n

        # ── Étape 1 : retrouver x (identique à Decrypt) ──────────────────────
        u   = pow(X, phi, n2)
        l_u = (u - 1) // n
        mu  = pow(phi, -1, n)
        x   = l_u * mu % n

        g_x = (1 + x * n) % n2
        rn  = X * pow(g_x, -1, n2) % n2

        # ── Étape 3 : racine n-ième mod n² ───────────────────────────────────
        # phi(n²) = n * phi(n)
        # exposant de la racine : e = n^{-1} mod phi(n²)
        phi_n2 = n * phi
        e      = pow(n, -1, phi_n2)
        r      = pow(rn, e, n2)

        return x, r
    
    def Add(c1: int, c2: int, public_key: int) -> int:
        """
        Addition homomorphe : c1 * c2 mod n² est une encryption de m1 + m2
        """
        n = public_key
        n2 = n * n
        return (c1 * c2) % n2
    
    def MulConst(c: int, k: int, public_key: int) -> int:
        """
        Multiplication homomorphe par une constante : c^k mod n² est une encryption de k*m
        """
        n = public_key
        n2 = n * n
        if k >= 0:
            return pow(c, k, n2)

        # k < 0 : nécessite c inversible mod n² (assuré par Encrypt avec r copremier à n)
        c_inv = pow(c, -1, n2)
        return pow(c_inv, -k, n2)
            


if __name__ == "__main__":
    n, phi_n = Paillier.KeyGen(1024)
    m = 123456789
    c = Paillier.Encrypt(m, n)
    m2 = Paillier.Decrypt(c, phi_n, n)
    assert m == m2, "Erreur de déchiffrement !"
    print(f"Original : {m}\nChiffré  : {c}\nDéchiffré: {m2}")
    
__all__ = ["Paillier", "gen_random_znz"]