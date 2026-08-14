#import "@local/polytech:1.0.0": *
#state("theme").update(blue)
#block-full(title: "RSA")[
  #block-left(title: "Création des clés")[
    - Choisir $p$ et $q$ deux nombres premiers _distincts_
    - Calculer $n = p q$
    - Calculer $phi(n) = (p-1)(q-1)$
    - Choisir $e < phi(n)$ avec $gcd(e, phi(n)) = 1$
    - Calculer $d$ tel que $d e equiv 1 mod phi(n)$

    #c[
      Clé publique: $(n, e)$ \
      Clé privée: $(n, d)$
    ]
  ]

  #block-left(title: "Chiffrement")[
    $
      C equiv M^e mod n
    $
  ]

  #block-left(title: "Déchiffrement")[
    $
      M equiv C^d mod n
    $
  ]
]

#block-full(title: "Paillier")[
  #block-left(title: "Création des clés")[
    - Choisir $p$ et $q$ deux nombres premiers aléatoires indépendants
    - Calculer $N = p q$ et $phi(N) = (p-1)(q-1)$

    #c[
      Clé publique: $n$ \
      Clé privée: $rho = n^(-1) mod phi(N)$
    ]
  ]

  #block-left(title: "Chiffrement")[
    - Choisir $r$ aléatoire dans $ZZ \/ n ZZ$

    $
      c equiv (1 + m n) r^n mod n^2
    $
  ]

  #block-left(title: "Déchiffrement")[
    On a $r = c^(rho) mod n$

    Donc :

    $
      m = ((c dot r^(-n) mod n^2) - 1) / n
    $
  ]
]
