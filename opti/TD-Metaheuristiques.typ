#import "../template/polytech.typ": *

#show: conf(doctitle: "TD - Métaheuristiques", subject: "Optimisation discrète", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 1

  1. Comme on veut une probabilité de 0.5 d'accepter la transformation, on veut :
  $
    t_0 & = (- Delta f) / ln(0.5) \
        & = (Delta f) / ln(2)
  $

  Au départ, le cycle $X = "ABCDEF"$ a une valeur $f(X) = 21$. On pose $Y = "ABEDCF"$, on a $f(Y) = 21$, ainsi :
  $
    t_0 & = (f(Y) - f(X)) / ln(2) \
        & = 9 / ln(2) \
        & approx 13.0°
  $

  2. $forall k, "on a" t_k = t_0 mu^k$

  $
    n_1 & = ln((-Delta f)/(t_0 ln(0.001)))/ln(µ) \
        & "Pour " mu = 0.85 \
        & = ln(-9 / (13 ln(0.001)))/ln(0.85) \
        & approx 14 \
        & "Pour " mu = 0.9 \
        & = ln(-9 / (13 ln(0.001)))/ln(0.9) \
        & approx 22 \
        & "Pour " mu = 0.95 \
        & = ln(-9 / (13 ln(0.001)))/ln(0.95) \
        & approx 45 \
        & "Pour " mu = 0.99 \
        & = ln(-9 / (13 ln(0.001)))/ln(0.99) \
        & approx 229
  $

  Dans chaque cas, le rapport temp finale / temp initiale vaut $t_n_1 / t_0 = mu^(n_1)$

  Pour $mu = 0.85$, on a $t_n_1 / t_0 = 0.85^14 approx 10%$

  Pour $mu = 0.9$, on a $t_n_1 / t_0 = 0.9^22 approx 10%$

  Pour $mu = 0.95$, on a $t_n_1 / t_0 = 0.95^45 approx 10%$

  Pour $mu = 0.99$, on a $t_n_1 / t_0 = 0.99^229 approx 10%$

  = Exercice 2

  on note $s_i$ le shift du $i$-ème bit.

  == Liste de taille 1

  On commence avec 1111, le meilleur voisin est 1110, la liste est vide.

  On a 1110, la liste vaut {s4}, le meilleur voisin est 1010

  On a 1010, la liste vaut {s2}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s4}, le meilleur voisin est 1111. => On a amélioré la solution ! on ne change pas la liste tabou.

  On a 1111, la liste vaut {s4}, le meilleur voisin est 1101.

  On a 1101, la liste vaut {s3}, le meilleur voisin est 0101.

  => C'est bon !

  == Liste de taille 2

  On commence avec 1111, la liste est vide, le meilleur voisin est 1110.

  On a 1110, la liste vaut {s4}, le meilleur voisin est 1010.

  On a 1010, la liste vaut {s2, s4}, le meilleur voisin est 1000.

  On a 1000, la liste vaut {s3, s2}, le meilleur voisin est 0000.

  On a 0000, la liste vaut {s1, s3}, le meilleur voisin est 0100.

  On a 0100, la liste vaut {s1, s3}, le meilleur voisin est 0101. => La liste ne change pas car on améliore la solution.

  => C'est bon !

  == Liste de taille 3

  On commence avec 1111, la liste est vide, le meilleur voisin est 1110.

  On a 1110, la liste vaut {s4}, le meilleur voisin est 1010.

  On a 1010, la liste vaut {s2, s4}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s3, s2, s4}, le meilleur voisin est 0011.

  On a 0011, la liste vaut {s3, s2, s4}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s1, s3, s2}, le meilleur voisin est 1010.

  On a 1010, la liste vaut {s1, s3, s2}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s4, s1, s3}, le meilleur voisin est 1111.

  On a 1111, la liste vaut {s4, s1, s3}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s2, s4, s1}, le meilleur voisin est 1001. (\*)

  On a 1001, la liste vaut {s3, s2, s4}, le meilleur voisin est 0001.

  On a 0001, la liste vaut {s3, s2, s4}, le meilleur voisin est 1001.

  On a 1001, la liste vaut {s1, s3, s2}, le meilleur voisin est 1000.

  On a 1000, la liste vaut {s1, s3, s2}, le meilleur voisin est 1001.

  On a 1001, la liste vaut {s4, s1, s3}, le meilleur voisin est 1101.

  On a 1101, la liste vaut {s4, s1, s3}, le meilleur voisin est 1001.

  On a 1001, la liste vaut {s2, s4, s1}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s2, s4, s1}. => On est déjà arrivé sur la même configuration !! (\*)

  == Liste de taille 4

  On commence avec 1111, la liste est vide, le meilleur voisin est 1110.

  On a 1110, la liste vaut {s4}, le meilleur voisin est 1010.

  On a 1010, la liste vaut {s2, s4}, le meilleur voisin est 1011.

  On a 1011, la liste vaut {s3, s2, s4}, le meilleur voisin est 0011.

  On a 0011, la liste vaut {s3, s2, s4, s1}. On ne peut plus aller nulle part, on est bloqué.

  = Exercice 3

  1.
  $
    f(X_0) & = sum_(i=1)^5 sum_(j=i+1)^5 "nc"(i, j) "dist"(p(i), p(j)) \
           & = 12 * 3 + 4 * 2 + 3 * 1 + 7 * 2 + 6 * 2 + 1 * 1 + 0 * 3 + 2 * 2 + 0 * 1 \
           & = 36 + 8 + 3 + 14 + 12 + 1 + 0 + 4 + 0 \
           & = 78
  $

  2. Le voisinage de $X_0$ correspond à toutes les positions accessibles en permutant deux éléments dans $X_0$.

  Ainsi le voisinage est :

  ```
  43152
  12453
  13542
  13425
  ```

  3.

  $
    f([1, 3, 4, 5, 2]) = 78 \
    f([4, 3, 1, 5, 2]) = 69 \
    f([1, 3, 5, 4, 2]) = 71 \
    f([1, 3, 4, 2, 5]) = 65 \
    f([1, 2, 4, 5, 3]) = 75 \
  $

  4. $ X_1 = 13425 $

  5. $ T_0 = (- Delta f)/ln(0.5) = -(90 - 78)/ln(0.5) = 12/ln(0.5) approx -17.31 $

  6. On met intuitivement les plus grands nombres de connexions dans les positions les plus proches :

  #rounded-table(
    ("Connexions", "Nombre de co", "Distance minimale"),
    (
      ("1 - 2", "12", "1"),
      ("1 - 5", "7", "6"),
      ("2 - 4", "6", "1"),
      ("1 - 3", "4", "1"),
      ("1 - 4", "3", "2"),
      ("3 - 5", "2", "2"),
      ("2 - 5", "1", "2"),
    ),
  )

  7. Avec le meilleur des cas on trouve $X = 43215$, avec le tableau suivant :

  #rounded-table(
    ("Connexions", "Nombre de co", "Distance minimale"),
    (
      ("1 - 2", "12", "1"),
      ("1 - 5", "7", "6"),
      ("2 - 4", "6", "1"),
      ("1 - 3", "4", text("2", red)),
      ("1 - 4", "3", "2"),
      ("3 - 5", "2", text("1", red)),
      ("2 - 5", "1", "2"),
    ),
  )
]
