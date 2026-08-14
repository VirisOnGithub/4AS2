from sympy import isprime, mod_inverse, randprime
import time

def KeyGen(bits=1024):
    p = randprime(2**(bits-1), 2**bits)
    q = randprime(2**(bits-1), 2**bits)
    while q == p:
        q = randprime(2**(bits-1), 2**bits)

    n = p * q
    phi_n = (p - 1) * (q - 1)

    e = 2
    while True:
        if isprime(e) and phi_n % e != 0:
            break
        e += 1

    d = mod_inverse(e, phi_n)

    return (n, e), (n, d)

def Encrypt(message: int, public_key: tuple) -> int:
    """Chiffre un entier m avec la clé publique (n, e)."""
    n, e = public_key
    assert 0 <= message < n, "Le message doit être dans [0, n)"
    return pow(message, e, n)

def Decrypt(ciphertext: int, private_key: tuple) -> int:
    """Déchiffre un entier c avec la clé privée (n, d)."""
    n, d = private_key
    return pow(ciphertext, d, n)


# --- Test ---
pub, priv = KeyGen(1024)
m = 34567890
c = Encrypt(m, pub)
m2 = Decrypt(c, priv)
assert m == m2, "Erreur de déchiffrement !"
print(f"Original : {m}\nChiffré  : {c}\nDéchiffré: {m2}")