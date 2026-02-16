#import "../template/polytech.typ": *;

#show "=>": $=>$

#show: conf(doctitle: "ADM - TD2", subject: "ADM", theme: rgb(150, 150, 30))[
  #titlepage(authors: ("Clément RENIERS", ""))

  = Exercice 1

  1. $m = (26 + 23 + 14 + 33 + 24 + 18)/6 = 23$
  2. $V(E) = 1/6sum_(k=1)^6 (x_k - m)^2 = 36$
  3. $ sigma = sqrt(V(E)) = sqrt(36) = 6 $
  4. $ 1 / (2sqrt(6)) approx 0.204p $
]
