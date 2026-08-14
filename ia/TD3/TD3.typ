#import "@local/polytech:1.0.0": *;

#show: conf(doctitle: "TP3", subject: "IA")[
  #titlepage(authors: "Clément RENIERS", pb: false)

  = Exercice 1

  == 1

  On choisit une fonction d'erreur quadratique moyenne :

  $
    cal(L) = 1 / 2 (hat(y) - y)^2
  $

  Intérêt de la fonction d'activation sigmoïde : bah sinon ça resterait linéaire
  // connard

  == 2

  Données :

  $
    x = mat(2; 3) ; y = 10
  $

  $
    w_(0, 1)^((1)) = -1 \
    w_(1, 1)^((1)) = 2 \
    w_(2, 1)^((1)) = 1 \
    w_(0, 2)^((1)) = 2 \
    w_(1, 2)^((1)) = -1 \
    w_(2, 2)^((1)) = -1 \
    w_(0, 1)^((2)) = 1 \
    w_(1, 1)^((2)) = 2 \
    w_(2, 1)^((2)) = -1 \
  $

  $
    z_1^((1)) & = 1 * (-1) + 2 * 2 + 3 * 1 = 6 \
    z_2^((1)) & = 1 * 2 + 2 * (-1) + 3 * (-1) = -3 \
  $

  #let sig(nb) = calc.round(1 / (1 + calc.exp(-nb)), digits: 4)

  $
    a_(1)^((1)) & = sigma(z_1^((1))) = #sig(6) \
    a_(2)^((1)) & = sigma(z_2^((1))) = #sig(-3) \
  $

  Donc :

  #let z12 = 1 * 1 + 2 * sig(6) + (-1) * sig(-3)

  $
    hat(y) & = z_1^((2)) = 1 * 1 + 2 * a_(1)^((1)) + (-1) * a_(2)^((1)) = z12 \
    cal(L) & = 1 / 2 (hat(y) - y)^2 = #(calc.round((1 / 2) * calc.pow(z12 - 10, 2), digits: 4))
  $

  == 3

  On reprend la même fonction d'erreur.

  Activation linéaire :
  $
    delta_1^(2) = (partial L) / (partial z_1^((2))) = -(y - hat(y)) underbrace(sigma(z_1^1), "=1 car activation linéaire")
  $

  Rétropropagation :

  $
    delta_1^((1)) = (partial L) / (partial z_1^((1))) = delta_1^((2)) w_(1, 1)^((2)) sigma'(z_1^((1)))
  $

  On sait que $sigma'(z) = sigma(z)(1 - sigma(z))$

  Gradient pour $w_(2, 1)^((1))$ :

  $
    (partial L) / (partial w_(2, 1)^((1))) = delta_1^((2)) w_(1, 1)^((2)) sigma'(z_1^((1))) x_2
  $

  (On a $k = 1$ par qu'on n'a qu'un seul neurone sur la couche 2 (une seule sortie))

  $
    w_(2 1)^1 = w_(2 1)^1 + "à compléter j'ai rien compris"
  $

  = Exercice 2

  == 1

  - Calculer l'expression du gradient :

  $
    (partial cal(L)_("MSE")) / (partial z_i) & = z_i - y_i
  $

  - Valeur numérique du gradient :

    - Classe 1 : $z_1 - y_1 = 1$
    - Classe 2 : $z_2 - y_2 = 1$
    - Classe 3 : $z_3 - y_3 = -1$

]
