import random as rd


def euclide_etendu(a, b):
    if b == 0:
        return a, 1, 0
    pgcd, u1, v1 = euclide_etendu(b, a % b)
    u = v1
    v = u1 - (a // b) * v1
    return pgcd, u, v

def inverse_mod(a, p):
    pgcd, x, _ = euclide_etendu(a % p, p)
    if pgcd != 1:
        raise ValueError(f"{a} pas inversible modulo {p}")
    return x % p

def crible(n):
    p = [True]*(n+1) #ou n ?
    p[0] = False
    p[1] = False

    #on commence à i=2
    i=2
    while i*i <= n : #on s'arrête à la racine de n, car après la liste des diviseurs est symétrique
        if p[i]:
            for j in range(i*i, n+1, i):
                p[j] = False
        i+=1

    premiers = [i for i in range(n+1) if p[i]]
    return premiers

def est_premier(n):
    if n<2:
        return False
    if n==2:
        return True
    if n%2==0:
        return False

    i = 3
    while i*i <= n:
        if n%i == 0:
            return False
        i+=2 #on saute les pairs
    return True

def diviseur_premier(n):
    diviseurs = []

    if n%2==0:
        diviseurs.append(2)
        while n%2==0:
            n = n//2

    i=3
    while i*i <= n:
        if n%i == 0:
            diviseurs.append(i)
            while n%i == 0:
                n = n//i
        i+=2

    if n>1:
        diviseurs.append(n)
    return diviseurs

def est_generateur(g, p, fact_premier):
    """Verifie si g est un générateur de Z/pZ*"""
    for q in fact_premier: #où fact_premier est les facteurs premiers de p-1
        if pow(g, (p-1)//q, p)==1:
            return False
    return True

def find_gen(p):
    """Trouve un générateur de Z/pZ* par tirage aléatoire"""
    facteurs = diviseur_premier(p-1)
    while True:
        g=rd.randint(2,p-1)
        if est_generateur(g, p, facteurs):
            return g
        
def exp_mod(g, x,p):
    """calcul efficace de g^x mod p"""

    result = 1
    g = g%p #pour le cas où g>=p

    while x>0:
        if x%2==1:
            result = (result * g) % p
        g=(g*g)%p
        x=x//2

    return result