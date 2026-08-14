# Exercice 79

2. $e$ doit être premier avec $\phi(n)$ pour assurer l'existence de $d$ tel que $ed \equiv 1 \mod \phi(n)$. Si cette condition n'est pas remplie, alors $d$ n'existe pas et le système de chiffrement RSA ne peut pas fonctionner correctement.

3. e ne peut pas être pair (sauf e = 2, qui est premier). Mais si e = 2, alors comme φ(n) = (p−1)(q−1) est toujours pair (p, q impairs > 2), on aurait gcd(2, φ(n)) = 2 ≠ 1 → l'inverse n'existe pas. Donc e est toujours impair.
d peut-il être pair ? Oui, rien ne l'interdit. d = e⁻¹ mod φ(n) est déterminé par e et φ(n) ; sa parité n'est pas contrainte à priori. En pratique, d est souvent grand et de parité quelconque.

4. Choisir e petit (typiquement $e = 3$ ou $e = 65537 = 2^{16} + 1$) accélère le chiffrement (et la vérification de signature), car $pow(m, e, n)$ est rapide avec peu de bits à 1.
    
    $e = 3$ : seulement 2 multiplications modulo n
    $e = 65537$ : bon compromis vitesse / sécurité (17 bits dont 2 seulement à 1 → exponentiation rapide par carrés)

5. Sur ma machine : 
   - KeyGen  : 0.641 s
   - Encrypt : 0.006 ms
   - Decrypt : 16.567 ms

    Pour 1Go de données, le temps estimé est de 50s environ.

6. RSA ne possède pas la sécurité sémantique : avec un message $m$ et une clé publique $(n, e)$ fixés, on obtient toujours le même chiffré $c = m^e \mod n$. Ainsi, un attaquant qui possède un chiffré, deux clairs et un oracle peut juste chiffrer les deux clairs et comparer les résultats au chiffré.

# Exercice 81

1. RSA est déterministe. Ainsi, en connaissant la clé publique, un attaquant peut chiffrer les 50 numéros possibles et comparer avec ceux voulus.
2. 