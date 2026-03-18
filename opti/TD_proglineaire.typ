#import "../template/src/polytech.typ": *

#show: conf(doctitle: "TD - Programmation linéaire", subject: "Optimisation discrète", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 2

  3. On a :
  $
    & cases(x_1 + 2x_2 <= 8, 2x_1 + 2x_2 <= 10, 9x_1 + 4x_2 <= 36) \
    & z= 50x_1 + 60x_2 \
    & <=> cases(x_1 + 2x_2 + x_3 = 8, 2x_1 + 2x_2 + x_4 = 10, 9x_1 + 4x_2 + x_5 = 36) \
    & "où" x_3, x_4, x_5 >= 0
  $

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_3$, "1", "2", "1", "0", "0", "8"),
      ($x_4$, "2", "2", "0", "1", "0", "10"),
      ($x_5$, "9", "4", "0", "0", "1", "36"),
      ("z", "50", "60", "0", "0", "0", "0"),
    ),
  )

  - Plus grand coefficient de la dernière ligne : $x_2$

  - On calcule $"résultat"/x_2$ pour chaque ligne :
    - $x_3$: $8/2 = 4$
    - $x_4$: $10/2 = 5$
    - $x_5$: $36/4 = 9$

    On prend la plus petite valeur comme variable à intervertir, donc ici $x_3$.

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_2$, "1/2", "1", "1/2", "0", "0", "4"),
      ($x_4$, "1", "0", "-1", "1", "0", "2"),
      ($x_5$, "7", "0", "-2", "0", "1", "20"),
      ("z", "20", "0", "-30", "0", "0", "-240"),
    ),
  )

  On recommence :

  - Plus grand coefficient de la dernière ligne : $x_1$
  - Plus petit résultat : $2/1 = 2$ pour $x_4$

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_2$, "0", "1", "1", "-1/2", "0", "3"),
      ($x_1$, "1", "0", "-1", "1", "0", "2"),
      ($x_5$, "0", "0", "5", "-7", "1", "6"),
      ("z", "0", "0", "-10", "-20", "0", "-280"),
    ),
  )

  On peut plus continuer, mais on voit que les coefficients de la dernière ligne sont tous négatifs, donc on a trouvé la solution optimale : $x_1 = 2$, $x_2 = 3$, $z = 50*2 + 60*3 = 280$.

  = Exercice 3

  2.

  $
    cases(-x_1 + 2x_2 + x_3 = 5, x_1 + 2x_2 + x_4 = 14, x_1 + x_5 = 8, z = 10x_1 + 50 x_2)
  $

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_3$, "-1", "2", "1", "0", "0", "5"),
      ($x_4$, "1", "2", "0", "1", "0", "14"),
      ($x_5$, "1", "0", "0", "0", "1", "8"),
      ("z", "10", "50", "0", "0", "0", "0"),
    ),
  )

  Plus grand coef : $x_2$

  Plus petit résultat : $5\/2$ pour $x_3$

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_2$, "-1/2", "1", "1/2", "0", "0", "5/2"),
      ($x_4$, "2", "0", "-1", "1", "0", "9"),
      ($x_5$, "1", "0", "0", "0", "1", "8"),
      ("z", "35", "0", "-25", "0", "0", "-125"),
    ),
  )

  Plus grand coef : $x_1$

  Plus petit résultat (on doit prendre la plus petite valeur *positive*) : $9/2$ pour $x_4$

  #rounded-table(
    ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
    (
      ($x_2$, "0", "1", "1/4", "1/4", "0", "4.75"),
      ($x_1$, "1", "0", "-1/2", "1/2", "0", "9/2"),
      ($x_5$, "0", "0", "1/2", "-1/2", "1", "7/2"),
      ("z", "0", "0", "-7.5", "-17.5", "0", "-565/2"),
    ),
  )

  C'est gagné, tous les coefficients de la dernière ligne sont négatifs, donc la solution optimale est $x_1 = 9/2$, $x_2 = 4.75$, $z = 565/2$.

  = Exercice 4
  1.
    #rounded-image(image("/assets/image-8.png"))

    On voit bien que l'origine n'est pas une solution de base.

    2.

    $
      cases(x_1 + x_2 <= 6, x_2 <= 3, x_1 + x_2 >= 1) \
      z = x_1 + 2x_2
    $

    On insère les variables d'écart :

    $
      cases(x_1 + x_2 #text($+ x_3$, red) = 6, x_2 #text($+ x_4$, red) = 3, x_1 + x_2 #text($- x_5$, red) = 1) \
      z = x_1 + 2x_2
    $

    On insère les variables artificielles :

    $
      cases(x_1 + x_2 #text($+ x_3$, red) = 6, x_2 #text($+ x_4$, red) = 3, x_1 + x_2 #text($- x_5$, red) #text($+ x_6$, blue) = 1) \
      z = x_1 + 2x_2
    $

    Nos variables de base sont $x_3$, $x_4$ et $x_6$.

    == Phase 1

    Nouvelle fonction objectif : $z' = x_6$ à minimiser.

    On exprime $z'$ en fonction des variables hors base :
    $
      z' = x_6 = 1 - x_1 - x_2 + x_5
    $

    On maximise $z'' = -z' = -(1 - x_1 - x_2 + x_5) = -1 + x_1 + x_2 - x_5$

    #rounded-table(
      ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, $x_6$, ""),
      (
        ($x_3$, "1", "1", "1", "0", "0", "0", "6"),
        ($x_4$, "0", "1", "0", "1", "0", "0", "3"),
        ($x_6$, "1", "1", "0", "0", "-1", "1", "1"),
        ("z''", "1", "1", "0", "0", "-1", "0", "1"),
      ),
    )

    On applique la méthode du simplexe :

    On choisit $x_1$ comme variable à faire entrer dans la base (on a déjà un 0 dans la colonne)
    On choisit $x_6$ comme variable à faire sortir de la base (on a $1/1 = 1$ qui est le plus petit résultat positif)

    #rounded-table(
      ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, $x_6$, ""),
      (
        ($x_3$, "0", "0", "1", "0", "1", "-1", "5"),
        ($x_4$, "0", "1", "0", "1", "0", "0", "3"),
        ($x_1$, "1", "1", "0", "0", "-1", "1", "1"),
        ("z''", "0", "0", "0", "0", "0", "-1", "0"),
      ),
    )

    On a un zéro en bas à droite, donc la phase 1 est terminée.

    On enlève la colonne de $x_6$ et on recommence pour la phase 2 :

    == Phase 2

    On réécrit la fonction objectif en fonction des variables hors base ($x_2$ et $x_5$) :

    $
      z = x_1 + 2x_2 = 1 + x_2 + x_5
    $

    #rounded-table(
      ("", $x_1$, $x_2$, $x_3$, $x_4$, $x_5$, ""),
      (
        ($x_3$, "0", "0", "1", "0", "1", "5"),
        ($x_4$, "0", "1", "0", "1", "0", "3"),
        ($x_1$, "1", "1", "0", "0", "-1", "1"),
        ("z", "0", "1", "0", "0", "1", "-1"),
      ),
    )

    Ici on a intérêt à sélectionner $x_5$
]
