#import "@local/polytech:1.0.0": *;

#show: conf(doctitle: "ADM - TD2", subject: "ADM", theme: rgb(150, 150, 30))[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 1
  1.
  On met chacun des points dans une classe $A_i$.

  =Matrice de distance=

  $
    mat(
      , a_1, a_2, a_3, a_4, a_5, a_6;
      a_1, 0, 1, 9, 2, 9, 10;
      a_2, , 0, 4, 1, 10, 9;
      a_3, , , 0, 5, 18, 13;
      a_4, , , , 0, 5, 4;
      a_5, , , , , 0, 1;
      a_6, , , , , , 0
    )
  $

  On entoure le minimum (on prend celui qu'on veut)

  $
    mat(
      , a_1, a_2, a_3, a_4, a_5, a_6, A_7;
      a_1, 0, #circle("1"), 9, 2, 9, 10;
      a_2, , 0, 4, 1, 10, 9;
      a_3, , , 0, 5, 18, 13;
      a_4, , , , 0, 5, 4;
      a_5, , , , , 0, 1;
      a_6, , , , , , 0;
      A_7, , , , , , , 0
    )
  $

  #place(line(length: 140pt), dx: 155pt, dy: -113pt)
  #place(line(length: 120pt, angle: 90deg), dx: 203pt, dy: -130pt)


  On fait le saut maximal : Quand on veut rejoindre $a_3$ depuis $A_7$, on regarde les distances $a_1 => a_3$ et $a_2 => a_3$. Comme on utilise la stratégie du saut maximal, on choisit le point qui a la distance maximale.
  Ici $a_1 => a_3 = 9$, $a_2 => a_3 = 4$, donc on choisit $9$.

  On continue jusqu'à avoir tout barré : on obtient :

  $
    mat(
      , a_1, a_2, a_3, a_4, a_5, a_6, A_7, A_8, A_9, A_(10);
      a_1, 0, 1, 9, 2, 9, 10;
      a_2, , 0, 4, 1, 10, 9;
      a_3, , , 0, 5, 18, 13, 9, 18, 9;
      a_4, , , , 0, 5, 4, 2, 5;
      a_5, , , , , 0, 1, 10;
      a_6, , , , , , 0, 10;
      A_7, , , , , , , 0, 10;
      A_8, , , , , , , , 0, 10, 18;
      A_9, , , , , , , , , 0;
      A_(10), , , , , , , , , , 0;
    )
  $

  2.
    #rounded-image(image("dendro.png"), size: 200pt)

    La ligne rouge correspond à la séparation en 2 classes, la ligne verte correspond à la séparation en 3 classes.
  3.

  On a d'après la question précédente :

  $
    cal(P)_3 = underbrace({a_1, a_2, a_4}, C_1); underbrace({a_3}, C_2); underbrace({a_5, a_6}, C_3)
  $


  =Calcul des dissimilarités intra=

  La matrice de dissidence de $C_1$ est :

  $
    mat(
      , a_1, a_2, a_4;
      a_1, 0, 1, 2;
      a_2, , 0, 1;
      a_4, , , 0
    )
  $

  $
    s_a (C_1) & = 1/(3*2) * 2 *(1 + 2 + 1) approx 1.33
  $

  Pour $C_2$, quand une classe ne contient qu'un seul élément, on considère que sa dissidence est nulle.

  $ s_a(C_2) & = 0 $

  Pour $C_3$ :

  $
    mat(
      , a_5, a_6;
      a_5, 0, 1;
      a_6, , 0
    )
  $

  $
    s_a (C_3) & = 1/(2*1) * 2 * 1 = 1
  $

  =Calcul des dissimilarités inter=

  Entre $C_1$ et $C_3$, on crée la matrice de dissidence suivante :
  $
    mat(
      , a_5, a_6;
      a_1, 9, 10;
      a_2, 10, 9;
      a_4, 5, 4
    )
  $

  Ainsi :

  $
    d_a (C_1, C_3) & = 1/(3 * 2) * (9 + 10 + 10 + 9 + 5 + 4) approx 7.83
  $

  Entre $C_2$ et $C_3$ :

  $
    mat(
      , a_5, a_6;
      a_3, 18, 13
    )
  $

  $
    d_a (C_2, C_3) & = 1/(1 * 2) * (18 + 13) = 15.5
  $

  Entre $C_1$ et $C_2$ :

  $
    mat(
      , a_3;
      a_1, 9;
      a_2, 4;
      a_4, 5
    )
  $

  $
    d_a (C_1, C_2) & = 1/(3 * 1) * (9 + 4 + 5) = 6
  $

  On entoure le minimum : $d_a (C_1, C_2) = 6$

  =Dunn généralisé=

  $
    "Dunn"_G (P) = (d_a (C_1, C_2)) / (s_a (C_1)) = 6 / 1.33 approx 4.47
  $


  POUR LA PARTITION À 2 CLASSES

  $
    cal(P)_2 = underbrace({a_1, a_2, a_3, a_4}, C_1); underbrace({a_5, a_6}, C_2)
  $

  =Calcul des dissimilarités intra=

  La matrice de dissidence de $C_1$ est :

  $
    mat(
      , a_1, a_2, a_3, a_4;
      a_1, 0, 1, 9, 2;
      a_2, , 0, 4, 1;
      a_3, , , 0, 5;
      a_4, , , , 0
    )
  $

  $
    s_a (C_1) & = 1/(4*3) * 2 *(1 + 9 + 2 + 4 + 1 + 5) approx 3.67
  $

  #pagebreak()

  Pour $C_2$ :
  $
    mat(
      , a_5, a_6;
      a_5, 0, 1;
      a_6, , 0
    )
  $

  $
    s_a (C_2) & = 1/(2*1) * 2 * 1 = 1
  $

  =Calcul des dissimilarités inter=

  Entre $C_1$ et $C_2$ :

  $
    mat(
      , a_5, a_6;
      a_1, 9, 10;
      a_2, 10, 9;
      a_3, 18, 13;
      a_4, 5, 4
    )
  $

  $
    d_a (C_1, C_2) & = 1/(4 * 2) * (9 + 10 + 10 + 9 + 18 + 13 + 5 + 4) approx 11.25
  $

  On entoure le minimum : $d_a (C_1, C_2) = 9.75$

  =Dunn généralisé=

  $
    "Dunn"_G (P) = (d_a (C_1, C_2)) / (s_a (C_1)) = 9.75 / 3.67 approx 2.66
  $
]
