#import "@local/polytech:1.0.0": *;

#show: conf(doctitle: "TD2", subject: "ADM", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 1

  == Définitions

  - Borné : nombre de tokens borné -> Algorithme de Karp-Miller
  - Bloquant : marquage accessible où on est bloqué
  - Propre : on peut toujours revenir au marquage initial
  - Quasi-vivant : depuis le marquage initial, toutes les transitions peuvent être franchies (possiblement après un chemin)
  - Vivant : depuis tous les marquages accessibles, toutes les transitions peuvent être franchies (possiblement après un chemin)

  === Karp & Miller pour $R_1$

  ==== Déroulement

  - On part du noeud initial.
  - On consomme le token du noeud initial et on le propage à tous les noeuds accessibles.
  - On répète jusqu'à ce que tous les noeuds soient marqués ou qu'on ait un noeud marqué $omega$ qui est accessible depuis un noeud déjà marqué $omega$

  #rounded-image(image("R1_Karp_Miller.webp"))

  Pour Borné, si à chaque étape, on n'obtient pas de noeud marqué $omega$ accessible depuis un noeud déjà marqué $omega$, alors le réseau est borné. Sinon, il ne l'est pas.

  Pour Quasi-vivant, on regarde juste si toutes les transitions sont présentes dans le graphe.

  Pour Vivant : Si on est Quasi-vivant et Propre, alors on est vivant. Sinon, on regarde pour chaque état si on peut utiliser toutes les transitions

  == Tableaux

  #rounded-image(image("assets/image.png"))


  #rounded-table(
    ("", $R_1$, $R_2$, $R_3$, $R_4$, $R_5$, $R_6$),
    (
      ("Borné", "O", "O", "O", "", "", ""),
      ("Bloquant", "X", "X", "O", "", "", ""),
      ("Propre", "O", "O", "X", "", "", ""),
      ("Quasi-vivant", "X", "O", "", "", "", ""),
      ("Vivant", "0", "X", "X", "", "", ""),
    ),
  )
]
