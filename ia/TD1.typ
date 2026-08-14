#import "@local/polytech:1.0.0": *

#let argmax = $op("argmax", limits: #true)$
#let bigp(content) = $lr(content, size: #150%)$

#show: conf(doctitle: "TD1", subject: "IA", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Modélisation d'un problème sous forme de MDP

  (j'ai pas noté)

  = Exercice 2

  #rounded-image(image("assets/image.png"))

  #block-left(title: "Algorithme")[
    - Initialisation arbitraire (souvent 0)
    - _Value Iteration_

    $
      forall s in S, V_k(s) <- max_(a in A) sum_(s' in S) T(s, a, s') lr((R(s,a,s') + gamma V_(k-1)(s')), size: #150%)
    $

    - Répétition jusqu'à convergence

    $ max_(s in S) |V_k (S) - V_(k+1) (S)| < epsilon $

  ]

  #block-left(title: "Politique gloutonne")[
    $
      pi^g (s) = argmax_(a in A) sum_(s' in S) T(s, a, s') bigp((R(s, a, s') + gamma V(s')))
    $
  ]

  On a 3 états : cool, warm, overheated

  On a :

  $
    & V_0 ("cool") = 0 \
    & V_0 ("warm") = 0 \
    & V_0 ("overheated") = 0
  $

  Après 1 itération :

  $
          V_1 ("cool") & = max (
                           1 times (1 + gamma times 0),
                           0.5 times (2 + gamma times 0) + 0.5 times (2 + gamma times 0)
                         ) \
                       & = max (1, 2) \
                       & = 2 \
          V_1 ("warm") & = max (
                           0.5 times (1 + gamma times 0) + 0.5 times (1 + gamma times 0),
                           1 times (-10 + gamma times 0)
                         ) \
                       & = max (1, -10) \
                       & = 1 \
    V_1 ("overheated") & = 0
  $

  Après 2 itérations :

  $
          V_2 ("cool") & = max (
                           1 times (1 + gamma times 2),
                           0.5 times (2 + gamma times V_1("warm")) + 0.5 times (2 + gamma times V_1("cool"))
                         ) \
                       & = max (2.8, 1.45 + 1.9) \
                       & = 3.35 \
          V_2 ("warm") & = max (
                         0.5 times (1 + gamma times 2) + 0.5 (1 + gamma times 1), 1 times (-10 + gamma times 0) \
                       & = max (1.4 + 0.95, -10) \
                       & = 2.35 \
    V_2 ("overheated") & = 0
  $

  La politique gloutonne après deux itérations est donc

  $
    pi_2 ("cool") &= argmax (1 times (1 + gamma times 3.35), 0.5 times (2 + gamma times 2.35) + 0.5 times (2 + gamma times 3.35)) \
    &= argmax (4.015, 4.565) \
    &= "FAST"\
    \
    pi_2 ("warm") &= argmax (1 * (-10 + gamma times 0), 0.5 * (1 + gamma times 3.35) + 0.5 * (1 + gamma times 2.35))
    &= "SLOW"
  $
]
