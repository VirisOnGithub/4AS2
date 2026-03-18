#import "../template/src/polytech.typ": *

#show: conf(doctitle: "TD - Métaheuristiques", subject: "Optimisation discrète", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 4

  Codage : On prend un tableau de taille 8, où on stocke la position de chaque reine sur sa ligne.

  Fitness : On minimise le nombre de conflits entre les reines.

  = Exercice 5

  On note :

  $
    x_i = cases(0 "si" i in P_1, 1 "si" i in P_2)
  $

  Il faut donc minimiser :

  $
    f(x) = abs(36 - sum i * x_i) + abs(36 - product i * x_i)
  $
]
