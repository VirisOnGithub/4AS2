#import "../template/polytech.typ": *;

#show "=>": $=>$

#show: conf(doctitle: "ADM - TD1", subject: "ADM", theme: rgb(150, 150, 30))[
  #titlepage(authors: ("Clément RENIERS", ""))

  = Ex1

  1. $forall i, m_i = 1$, donc
  $ g(E) = (sum_(i=1)^n m_i a_i) / (sum_(i=1)^n m_i) = 1/8 sum_(i=1)^8 a_i $
  D'où $ g(E) & = (1/8) mat(3 + 0 + 1 + 0 + 4 + 2 + 4 + 2, 0 + 1 + 1 + 2 + 2 + 3 + 3 + 4) \
       & = (1/8) mat(16, 16) \
       & = mat(2, 2) $

  $
    g(A_1) & = (1/4) mat(3 + 0 + 1 + 0, 0 + 1 + 1 + 2) \
           & = (1/4) mat(4, 4) \
           & = mat(1, 1)
  $

  $
    g(A_2) & = (1/4) mat(4 + 2 + 4 + 2, 2 + 3 + 3 + 4) \
           & = (1/4) mat(12, 12) \
           & = mat(3, 3)
  $

  Ainsi $G = mat(1, 1; 3, 3)$

  2. $hat(X)_(i,j) = X_(i,j) - g_j$, donc $ hat(X) = mat(1, -2; -2, -1; -1, -1; -2, 0; 2, 0; 0, 1; 2, 1; 0, 2) $

  $ hat(Y) = mat(2, -1; -1, 0; 0, 0; -1, 1) $
  $ hat(Z) = mat(1, -1; -1, 0; 1, 0; -1, 1) $

  3. $T = hat(X)^T D hat(X) = hat(X)^T hat(X) =$

  $W = M(A_1) + M(A_2) = mat(6, -3; -3, 2) + mat(4, -2; -2, 2) = mat(10, -5; -5, 4)$

  $B = G^T D_g G = mat(-1, 1; -1, 1) mat(4, 0; 0, 4) mat(-1, -1; 1, 1) = mat(8, 8; 8, 8)$

  4.

  5. (j'ai oublié de prendre le TD hihi)

  6.

  $ 1/2 u^T B u = 1 / 2 mat(1, 1) mat(8, 8; 8, 8) mat(1; 1) = 16 $

  d'où
  $
    p & = (u^t B u) / (u^t W u) \
      & = 16 / 18 \
      & approx 0.89
  $

  7.
  $
    I_b (E) & = sum_(i=1)^n m_i dot d^2(a_i, b) \
    & = d^2((3, 0), c) + d^2((0, 1), c) + d^2((1, 1), c) + d^2((0, 2), c) + d^2((4, 2), c) + d^2((2, 3), c) + d^2((4, 3), c) + d^2((2, 4), c) \
    & = 110
  $


  = Exercice 2
  1.
    $
      V_1 & = 1 / 6 mat(1, 0, 1) mat(1; 0; -1) \
          & dots.v \
        V & = 1 / 6 mat(4, -2, -2; -2, 4, -2; -2, -2, 4) \
    $
  2.
    $
      I_O (E) & = sum_(i=1)^n m_i dot d^2(a_i, O) \
              & = 1 / 6 (d^2((1, 0, -1), O) + d^2((0, 1, -1), O) + d^2((-1, 1, 0), O) \
              & + d^2((0, -1, 1), O) + d^2((-1, 0, 1), O) + d^2((1, -1, 0), O)) \
              & = 1/ 6 (2 + 2 + 2 + 2 + 2 + 2) \
              & = 2
    $

  3. $
      chi_A & = det(V - lambda I_3) \
            & = 1 / 3 mat(delim: "|", 2 - lambda, -1, -1; -1, 2 - lambda, -1; -1, -1, 2 - lambda) \
            & = - 1 / 3 lambda (-3 + lambda)
    $

    BALC ça fait {0, 1, 1}

  4. $u = k mat(1; 1; 1;)$

  5. $u' = mat(1; -2; 1)$

  6. $mat(sqrt(2), 0; -sqrt(2)/2, -sqrt(3/2); -sqrt(2)/2, -sqrt(3/2); -sqrt(2)/2, sqrt(3/2); -sqrt(2), 0; sqrt(2)/2, sqrt(3/2))$

]
