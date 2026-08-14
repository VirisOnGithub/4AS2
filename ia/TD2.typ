#import "@local/polytech:1.0.0": *
#let argmax = $op("argmax", limits: #true)$

#show: conf(doctitle: "TD2", subject: "IA")[
  #titlepage(authors: "Clément RENIERS")

  = Exercice 1

  #rounded-image(image("assets/image.png"))

  Episodes :
  - Episode 1 : Cool $->^("Fast")(+2)$ Warm $->^("Fast")$ (-10) Overheated
  - Episode 2 : Cool $->^("Fast")(+2)$ Cool $->^("Fast")$ (+2) Warm $->^("Fast")$(-10) Overheated

  == AR passif

  === Q1

  #block-full(title: "méthode")[
    Pour chaque état, on calcule la valeur de la somme des récompenses jusqu'à la fin de l'épisode. (parce que $gamma = 1$)
  ]

  -Calcul des échantillons

  - Episode 1
    - $v_1("Cool") = 2 - 10 = -8$
    - $v_1("Warm") = -10$
  - Episode 2
    - $v_2("Cool"_1) = 2 + 2 - 10 = -6$
    - $v_2("Cool"_2) = 2 - 10 = -8$
    - $v_2("Warm") = -10$


  Ainsi :
  $
    V("Cool") & = (-8 - 6 - 8) / 3 = -22/3 \
    V("Warm") & = (-10 - 10) / 2 = -10
  $

  #pagebreak()

  === Q2

  #block-full(title: "MAJ des états visités")[
    $
      V^pi (s) = V^pi (s) + alpha lr((v(s) - V^pi (s)), size: #200%)
    $
  ]

  - Episode 1
    - MAJ Cool : $V("Cool") = 0 + 0.1 (-8 - 0) = -0.8$
    - MAJ Warm : $V("Warm") = 0 + 0.1 (-10 - 0) = -1$
  - Episode 2
    - MAJ $"Cool"_1$ : $V("Cool") = -0.8 + 0.1 (-6 - (-0.8))) = -1.32$
    - MAJ $"Cool"_2$ : $V("Cool") = -1.32 + 0.1(-8 - (-1.32)) = -1.988$
    - MAJ Warm : $V("Warm") = -1 + 0.1 (-10 -(-1)) = -1.9$

  - Valeurs finales :
  $
    V("Cool") & = -1.988 \
    V("Warm") & = -1.9
  $

  // #pagebreak()

  === Q3

  #block-full(title: "TD prediction")[
    $
      V^pi (s) = V^pi (s) + alpha lr((underbrace(r + gamma V^pi (s'), "échantillon") - V^pi (s)), size: #100%)
    $

    où
    - $alpha$ est le coef d'apprentissage
    - $r$ est la récompense associée à l'action
  ]

  - Episode 1
    - MAJ Cool : $V("Cool") = 0 + 0.1 (2 + 1 times 0 - 0) = 0.2$
    - MAJ Warm : $V("Warm") = 0 + 0.1(-10 + 1 times 0 - 0) = -1$

  - Episode 2
    - MAJ $"Cool"_1$ : $V("Cool") = 0.2 + 0.1 (2 + 1 times 0.2 - 0.2) = 0.4$
    - MAJ $"Cool"_1$ : $V("Cool") = 0.4 + 0.1 (2 + 1 times (-1) + 0.4) = 0.46$
    - MAJ Warm : $V("Warm") = -1 + 0.1 (-10 + 1 times 0 - (-1)) = -1.9$

  - Valeurs finales :

  $
    V("Cool") & = 0.46 \
    V("Warm") & = -1.9
  $

  == AR actif

  === Q4

  La fonction de $Q$-valeur est initialisée à 0 partout. Or la politique gloutonne sélectionne l'action qui maximise la $Q$-valeur, donc la politique gloutonne à cet instant est complètement aléatoire.

  === Q5

  #block-full(title: [Algorithme du $Q$-learning])[
    À chaque étape, on met à jour la valeur du couple $(s, a)$ comme suit :
    $
      Q(s, a) = (1 - alpha) Q(s, a) + alpha (r + gamma max_(b in A(s')) Q(s', b))
    $
  ]

  - Episode 1
    - MAJ (Cool, Fast) : $Q("Cool", "Fast") = (1 - 0.1) times 0 + 0.1 (2 + 1 times 0) = 0.2$
    - MAJ (Warm, Fast) : $Q("Warm", "Fast") = (1 - 0.1) times 0 + 0.1 (-10 + 1 times 0) = -1$
  - Episode 2
    - MAJ 1 (Cool, Fast) : $Q("Cool", "Fast") = 0.9 times 0.2 + 0.1 (2 + 1 times 0.2) = 0.18 + 0.22 = 0.4$
    - MAJ 2 (Cool, Fast) : $Q("Cool", "Fast") = 0.9 times 0.4 + 0.1 (2 + 1 times 0) = 0.36 + 0.2 = 0.56$
    - MAJ (Warm, Fast) : $Q("Warm", "Fast") = 0.9 times (-1) + 0.1 (-10 + 1 times 0) = -0.9 + (-1) = -1.9$

  Ainsi la fonction de $Q$-valeur après les 2 épisodes est :

  $
    Q("Cool", "Fast") & = 0.56 \
    Q("Cool", "Slow") & = 0 \
    Q("Warm", "Fast") & = -1.9 \
    Q("Warm", "Slow") & = 0 \
  $

  === Q6

  #block-full(title: "Politique gloutonne")[
    $
      forall s in S, pi(s) = argmax_(a in A) Q(s, a)
    $
  ]

  En utilisant la fonction de $Q$-valeur précédente :

  - Pour l'état "Cool", $max(0.56, 0)$ correspond à l'action "Fast", donc $pi("Cool") = "Fast"$.
  - pour l'état "Warm", $max(-1, 9, 0)$ correspond à l'action "Cool" donc $pi("Warm") = "Cool"$.
]
