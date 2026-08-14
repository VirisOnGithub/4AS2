from sympy import randprime, mod_inverse, integer_nthroot, isprime
from itertools import combinations, permutations
import time

# ── Clés RSA 2048 bits ──────────────────────────────────────────
def KeyGen(bits=2048):
    p = randprime(2**(bits//2 - 1), 2**(bits//2))
    q = randprime(2**(bits//2 - 1), 2**(bits//2))
    while q == p:
        q = randprime(2**(bits//2 - 1), 2**(bits//2))
    n   = p * q
    phi = (p - 1) * (q - 1)
    e   = 65537
    d   = mod_inverse(e, phi)
    return (n, e), (n, d)

def encrypt(m, pub):  return pow(m, pub[1], pub[0])
def decrypt(c, prv):  return pow(c, prv[1], prv[0])

# ── Encodage : chaque numéro sur 2 chiffres, concaténés ─────────
def encode(nums):
    return int("".join(f"{x:02d}" for x in nums))

def decode(mc, k=6):
    s = str(mc).zfill(2 * k)
    return [int(s[i:i+2]) for i in range(0, 2*k, 2)]

# Exemple de la voyante
nums_secret = [33, 12, 24, 4, 8, 13]
pub, prv = KeyGen()

# Attaque dictionnaire (question 1)
def attaque_q1(ciphertexts, pub):
    """Retrouve chaque numéro en ≤ 49 essais."""
    dictionnaire = {encrypt(x, pub): x for x in range(1, 50)}
    return [dictionnaire[c] for c in ciphertexts]

# Simulation
c_list = [encrypt(x, pub) for x in nums_secret]
print("Retrouvés :", attaque_q1(c_list, pub))  # → [33, 12, 24, 4, 8, 13]

def attaque_q2_bruteforce(c_cible, pub, verbose=False):
    """Force brute sur toutes les 6-permutations de {1..49}."""
    n, e = pub
    compte = 0
    for perm in permutations(range(1, 50), 6):
        mc = encode(perm)
        compte += 1
        if pow(mc, e, n) == c_cible:
            if verbose:
                print(f"Trouvé après {compte} essais : {perm}")
            return perm
    return None

# Estimation théorique
from math import perm as mperm
nb_essais = mperm(49, 6)          # 10_068_347_520
temps_ms  = nb_essais             # 1 ms par essai
print(f"Permutations : {nb_essais:,}")
print(f"Temps moyen  : {temps_ms / 2 / 1000 / 3600:.1f} heures")
# ≈ 1 398 heure / 2 ≈ 58 jours en moyenne


def attaque_q3_trie(c_cible, pub, verbose=False):
    """Force brute sur C(49,6) combinaisons (numéros triés)."""
    n, e = pub
    compte = 0
    for combo in combinations(range(1, 50), 6):   # déjà trié
        mc = encode(combo)
        compte += 1
        if pow(mc, e, n) == c_cible:
            if verbose:
                print(f"Trouvé après {compte} essais : {combo}")
            return combo
    return None

# Exemple complet (petit RSA pour tester rapidement)
from sympy import randprime, mod_inverse
def KeyGen_small(bits=512):
    p = randprime(2**(bits//2-1), 2**(bits//2))
    q = randprime(2**(bits//2-1), 2**(bits//2))
    n = p*q; phi=(p-1)*(q-1); e=65537
    return (n,e), (n, mod_inverse(e,phi))

pub_s, prv_s = KeyGen_small()
nums_tries   = sorted(nums_secret)           # [4, 8, 12, 13, 24, 33]
mc_tries     = encode(nums_tries)
c_tries      = encrypt(mc_tries, pub_s)

t0 = time.perf_counter()
result = attaque_q3_trie(c_tries, pub_s, verbose=True)
print(f"Temps réel : {time.perf_counter()-t0:.2f}s")

# Estimation sur 2048 bits
from math import comb
nb_combos = comb(49, 6)
print(f"\nC(49,6) = {nb_combos:,}")
print(f"Temps moyen (2048 bits) ≈ {nb_combos/2/1000:.0f} s ≈ {nb_combos/2/1000/3600:.1f} h")

from sympy import integer_nthroot

def KeyGen_e17(bits=2048):
    p = randprime(2**(bits//2-1), 2**(bits//2))
    q = randprime(2**(bits//2-1), 2**(bits//2))
    while q == p:
        q = randprime(2**(bits//2-1), 2**(bits//2))
    n   = p * q
    phi = (p-1)*(q-1)
    e   = 17
    d   = mod_inverse(e, phi)
    return (n, e), (n, d)

def attaque_racine(c, e, pub):
    """
    Si m^e < n, alors c = m^e (pas de réduction mod n).
    On récupère m en calculant la racine e-ième entière de c.
    """
    m, exact = integer_nthroot(c, e)
    if exact:
        return m
    else:
        return None   # réduction modulaire a eu lieu → attaque échoue

# ── Démonstration ────────────────────────────────────────────────
pub17, prv17 = KeyGen_e17()
n, e = pub17

mc = encode(sorted(nums_secret))      # m_c = 040812132433
print(f"m_c          = {mc}")
print(f"m_c^17       ≈ 10^{len(str(mc**17))-1}")
print(f"n            ≈ 10^{len(str(n))-1}")
print(f"Réduction ?  {'NON' if mc**17 < n else 'OUI'}")

c17 = pow(mc, e, n)                   # = mc^17 (entier exact) => On divise par 17

t0 = time.perf_counter()
mc_retrouve = attaque_racine(c17, e, pub17)
dt = time.perf_counter() - t0

if mc_retrouve:
    print(f"\nAttaque réussie en {dt*1000:.3f} ms !")
    print(f"Numéros retrouvés : {decode(mc_retrouve)}")
else:
    print("Attaque échouée (m^e ≥ n)")