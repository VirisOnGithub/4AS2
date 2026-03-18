#import "../../template/src/polytech.typ": *;

#show: conf(doctitle: "TD1", subject: "ADM")[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 1

  1. On choisit le texte chiffré "A", l'oracle nous donne la lettre $m$ ($m$ étant une lettre de l'alphabet) => pour chaque lettre, on prend $26 - m$ comme clé.

  2. On choisit le texte chiffré "A", l'oracle nous donne la lettre $m$ ($m$ étant une lettre de l'alphabet) => pour chaque lettre, on prend $m$ comme clé.

  3. $(m_1, ..., m_n) -> (c_1, ..., c_n)$ => On prend $(c_1-m_1 (mod 26))$ comme clé.

  4. Brute force (que 26 possibilités).

  = Exercice 2

  1. Pour chiffrer un message, Arielle utilise la clé publique de Bob.
  2. Pour déchiffrer un message, Arielle utilise la clé privée de Arielle.
  3. Pour signer un message, Arielle utilise la clé privée de Arielle
  sur un hash du message.
  4. Pour authentifier un message, Arielle déchiffre le hash avec la clé publique
  de Bob et compare au hash original.

  = Exercice 4
  1. $p = 719, g = 3, a = 16$
  ON a donc $g^a mod p = 3^16 mod 719 = 191$

  2. On a $g^b equiv 534 [p]$
  => On cherche $543^16 [719] = 40$

  = Exercice 5

  $
    b = 1/a [p] & => b "tq" b a = 1 [p] \
                & <=> b a = 1 + p u \
                & <=> b a + p (-u) = 1 \
                & <=> "pgcd"(a, p) = 1
  $
  => Algo d'Euclide c'est ez
]
