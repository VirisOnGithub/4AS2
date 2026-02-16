#import "../template/polytech.typ": *

#show "=>": $=>$

// #show text:

#show: conf(doctitle: "ADM - Cours", subject: "ADM", theme: rgb("#9b6d00"))[
  #titlepage(authors: ("Clément RENIERS", ""))

  = Définitions

  #block-left(title: "Donnée")[
    Description élémentaire d'un objet, une observation, une transaction, un texte, un signal, une image ...
  ]

  #block-left(title: "Information")[
    Moyen pour un individu de connaître son environnement.
  ]

  #block-left(title: "Connaissance")[
    Etat de transistion de la donnée.
  ]

  == Types de données

  #block-left(title: "Quantitatives")[
    $X in RR^p$
  ]

  #block-left(title: "Qualitatives")[
    Données par exemple binaires : $X in {0, 1}^p$
  ]

  #block-left(title: "Mixtes")[
    Un sous-espace quantitatif et un sous-espace qualitatif
  ]

  #block-left(title: "Complexes")[
    N'ont pas de représentation vectorielle (ex: les graphes, les signaux)
  ]

  == Proximité entre les données

  #block-full(title: "Distance")[
    - Quantitaives : $X in RR^p, Y in RR^p$
      + Euclidienne :
    $ d(X, Y) = sqrt(sum_(i=1)^p (X_i - Y_i)^2) $
    + Manhattan :
    $ d(X, Y) = sum_(i=1)^p |X_i - Y_i| $
    #colbreak()
    #v(0.5em)
    - Qualitatives (binaires) : $X in {0, 1}^p, Y in {0, 1}^p$
      1. Hamming (Nombre de bits différents) :
      $ d(X, Y) = sum_(i=1)^p 1_{X_i != Y_i} $
      2. Distance de Matching simple :
      #align(center)[#table(
        columns: 3,
        "X\Y", "1", "0",
        "1", "a", "b",
        "0", "c", "d",
      )]
      $ d(X, Y) = sum_(i=1)^p 1_{X_i != Y_i} / sum_(i=1)^p 1_{X_i != Y_i} $
    - Données à base de variables nominales => Codage binaire
  ]

  = Extraction des connaissances élémentaires

  #block-left(title: "Centre de gravité des données")[
    Soit $E = {a_1, a_2, ..., a_n}$ un ensemble de données dans $RR^p$. Chaque $a_i$ est associé à une masse (pertinence, importance) $m_i$. Le centre de gravité de $E$ est défini par :
    $ g(E) = (sum_(i=1)^n m_i a_i) / (sum_(i=1)^n m_i) $
  ]

  #block-left(title: "Inertie des données")[
    $ I_b (E) = sum_(i=1)^n m_i dot d^2(a_i, b) $

    #underline("Cas particulier") : $ I(E) = sum_(i=1)^n m_i dot d^2(a_i, g(E)) $
    Si $m_i = 1/n$ pour tout $i$, on obtient la variance.
  ]

  #block-left(title: "Matrice d'inertie totale")[
    - $hat(X)$ : Une matrice de données centrée
    - $D$ : Matrice diagonale de dimensions $n times n$ dont le $i$-ème termen diagonal vaut $m_i$

    La matrice d'inertie totale est définie par :
    $ T = M(E) = hat(X)^T D hat(X) $
  ]

  #block-left(title: "Inertie relative à une partition")[
    Soit une partition $P = {A_1, A_2, ..., A_q}$ de l'ensemble de données $E$. Pour chaque groupe $A_k$, on peut calculer
    - Son centre de gravité $g_A_k$
    - Son inertie $I_A_k = I(A_k)$
    - Sa masse $m_A_k$
    => Inertie Intra-groupes : $ I_W (P) = sum_(k=1)^q I(A_k) $
    => Intertie Inter-groupes : $ I_B (P) = sum_(k=1)^q m_A_k dot d^2(g_A_k, g(E)) $
  ]

  #block-left(title: "Matrice d'inertie intra-groupes")[
    $ W = sum_(k=1)^q M(A_k) $
  ]

  #block-left(title: "Matrice d'inertie inter-groupes")[
    - Ingrédients :
      - $G$ matrice à $q$ lignes, des centres de gravité des groupes
      - $D_g$ : matrice diagonale dont le $k$-ème terme diagonal vaut $m_A_k$

    $ B = hat(G)^T D_g hat(G) $
  ]

  #block-left(title: "Inertie suivant les directions")[
    Soit $u$ un vecteur de $RR^p$ de norme 1. ($||u|| = 1$) \
    Si l'on projette l'ensemble des données sur $u$ :

    - L'inertie totale sur $u$ vaut : $u^t T u$
    - L'inertie intra-groupes sur $u$ vaut : $u^t W u$
    - L'inertie inter-groupes sur $u$ vaut : $u^t B u$

    => Le pouvoir de discrimination $ p = (u^t B u) / (u^t W u) in [0, 1] $
  ]

  = Visualisation des données multidimensionnelles

  == Analyse factorielle

  #block-left(title: [Analyse en Composantes Principales (*ACP*)])[
    Méthode descriptive qui consiste à :
    - Remplacer les variables réelles par des variables synthétiques, tout en conservant un maximum d'information.
    - Réduire les dimensions tout en fournissant un support visuel.
  ]

  #block-full(title: "Algorithme")[
    =Entrée= : la matrice de donnée $X_((n times p))$
    On note :
    - $C_j$ le vecteur correspondant à la $j$-ème colonne de $X$
    - $L_i$ le vecteur correspondant à la $i$-ème ligne de $X$
    => Etude dans $RR^p$.P Elle consiste à construire une variable synthétique $F$ telle que la distance des lignes $L_i$ à l'origine soit la mieux conservée possible
    - Construction de $F_p$ :
    Soit $alpha = mat(alpha_1; dots.v; alpha_p)$ tel que $||alpha|| = 1$.
    $ F = mat(phi_1; dots.v; phi_p), phi_i = sum_(j=1)^p alpha_j X_(i j) = L_i alpha $
    - Choix de la direction du vecteur $alpha$ :
    #align(center, image("projete.png", width: 100pt))
    Soit $p_i$ le projeté orthogonal de $L_i$ sur $alpha$. D'après le théorème de Pythagore, $ underbrace(d^2(0, L_i), "=constante=") = d^2(L_i, p_i) + d^2(0, p_i) $
    => On veut maximiser $d^2(0, p_i)$
    Or :
    $
      sum_(i=1)^n d^2(0, p_i) & = alpha^T T alpha \
                              & = alpha^T hat(X)^T D hat(X) alpha \
                              & = alpha^T X^T X alpha \
    $
    #colbreak()
    #v(0.5em)
    Or $X^T X$ est symétrique positive, donc elle est diagonalisable, et admet $p$ valeurs propres positives $lambda_1 <= lambda_2 <= ... <= lambda_p <= 0$ associés à des vecteurs propres $u_1, u_2, ..., u_p$.

    - On trouve les valeurs propres
    $ det(X^T X - lambda I) = 0 $
    - On trouve les vecteurs propres
    $ (X^T X - lambda I) u = 0 $
    => alpha corespondan au vecteur propre $u_1$ associé à la plus grande valeur propre $lambda_1$.
    => Calculer $F$
    On calcule une deuxième dimension $F'$ qui correspond à l'axe des ordonnées. C'est le vecteur propre $u_2$ associé à la deuxième plus grande valeur propre $lambda_2$.

    > =Sortie= :
    $X_((n times p)) #table(
      columns: 2,
      $U_1$, $U_2$,
    ) = #table(
      columns: 2,
      $F$, $F'$,
    )$

    => Perte d'information :
    $ 1 - (lambda_1 + lambda_2) / sum_(j=1)^p lambda_j $

    => Contribution des données
    $ C_k(i) = phi_(i x)^2 / lambda_k $

    $phi_(i k)$ est la composante de la donnée $i$ sur l'axe $k$

    => Qualité de la représentation
    $ cos^2(u_k, i) = phi_(i k)^2 / sum_j phi_(i j)^2 $
    Réprésente la qualité de la représentation de la donnée $i$ sur l'axe $k$
  ]

  #block-left(title: "Types d'ACP")[
    + ACP générale => ce qu'on a fait
    + ACP centrée : $X -> hat(X) -> "ACP générale" equiv "Diagonalisation de la matrice de covariance"$
    + ACP normée : $X -> hat(X) -> X'', X''_(i j) = (X_(i j) - g_j) / (sqrt(n) sigma_j)$
    $ "corr"(i,j) = "cov"(i,j) / (sigma_i sigma_j) $
    $ "cov"(i, j) = 1/n sum_(k=1)^n (X_(k i) - g_i)(X_(k j) - g_j) $
  ]
]
