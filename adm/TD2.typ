#import "../template/polytech.typ": *;

#show "=>": $=>$

#show: conf(doctitle: "ADM - TD2", subject: "ADM", theme: rgb(150, 150, 30))[
  #titlepage(authors: ("Clément RENIERS", ""))

  = Exercice 1

  1. $m = (26 + 23 + 14 + 33 + 24 + 18)/6 = 23$
  2. $V(E) = 1/6sum_(k=1)^6 (x_k - m)^2 = 36$
  3. $ sigma = sqrt(V(E)) = sqrt(36) = 6 $
  4. On fait une ACP centrée réduite (normée)
  $ V'_1 = (v_1 - mu)/(sqrt(n) dot sigma) = (26 - 23)/(sqrt(6) * 6) = 3/(6sqrt(6)) = 1/(2sqrt(6)) approx 0.204 $
  5. Quand l'ACP est normée, la matrice de dispersion est la matrice de corrélation.
  $ "corr"(V_1, V_2) = "cov"(V_1, V_2) / (sigma_V_1 dot sigma_V_2) $
  $ "cov"(i, j) = 1/n sum_(k=1)^n (X_(k i) - g_i)(X_(k j) - g_j) $
  $
     "cov"(V_1, V_2) & = 1/n sum_(k=1)^n (X_(k v_1) - g_1)(X_(k v_2) - g_2) \
                     & = 1/6 [(26 - 23)(36 - 33.667) + (23 - 23)("___") \
                     & + (14 - 23)(25 - 33.667) + (33 - 23)(45 - 33.667) \
                     & + (24 - 23)(34 - 33.667) + (18 - 23)(30 - 33.667)] \
                     & approx 36.16 \
    "corr"(V_1, V_2) & = 36.16 / (6 dot 6.128) \
                     & approx 0.984
  $
  8. $A_1 dot U_1 = mat(0.204, 0.155, 0.642) mat(0.628; 0.610; 0.489) approx 0.533$²

  9. On a $psi_(j k) = sqrt(lambda_k) U_(j k)$
  Ainsi $psi_(1 1) = sqrt(lambda_1) U_(1 1) = sqrt(2.4161) * 0.628 = 0.976$

  10. $ phi^2_(11) / lambda_1 = 0.533^2 / 2.4161 = 0.118 $

  11. $ psi^2_(11) / lambda_1 = 0.976^2 / 2.4161 = 0.394 $

  12. $ cos^2(U_1, 1) = phi_(1 1)^2 / (sum_j phi_(1 j)^2) = 0.533^2 / (0.533^2 + (-0.439)^2 + (-0.016)^2) approx 0.595 $

  13. $ cos^2(V_1) = psi_(1 1)^2 / sum_j psi_(1 j)^2 = 0.976^2 / (0.976^2 + 0.205^2 + 0.074^2) approx 0.952 $
]
