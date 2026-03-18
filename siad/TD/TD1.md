# TP1

## Exercice 1

Etape 0 : $V = \emptyset$ car $r_2 = \emptyset$, donc la jointure est vide également.

Etape 1 : insert $(r_1, [4, 2])$ . $r_2$ est toujours vide, donc la jointure est vide. $\Delta V = \emptyset$. Donc $V = \emptyset$.

Etape 2 : insert $(r_2, [2, 5])$. La jointure vaut donc :

| W | X | Y |
|---|---|---|
| 1 | 2 | 5 |
| 4 | 2 | 5 |

Ainsi $\Delta V = \{1, 4\}$. Donc $V = \{1, 4\}$.

Etape 3 : insert $(r_1, [4, 2])$. 4 est rajouté dans la jointure, mais comme 4 existe déjà, il n'est pas rajouté dans $V$. Donc $\Delta V = \emptyset$. Donc $V = \{1, 4\}$.

## Exercice 1 - Méthode Prof

Etape 0 : $V = \emptyset, Collect = \emptyset, UQS = \emptyset$

- MAJ U1 : DW reçoit $U_1 = insert(r_1, [4, 2])$. DW envoie $Q_1 = V(U_1) = \pi_W([4, 2] \Join r_2)$. $r_2$ est vide, donc $Q_1 = \emptyset$. 
UQS = $\{Q_1\}$

- MAJ U2 : DW reçoit $U_2 = insert(r_2, [2, 5])$. DW envoie 
$$
\begin{aligned}
Q_2 &= V(U_2) - UQS(U_2) \\
    &= V(U_2) - Q_1(U_2) \\
    &= \underbrace{\pi_W(r_1 \Join [2, 5])}_{=\{1\}} - \underbrace{\pi_W([4, 2] \Join [2, 5])}_{=\{4\}} \\
    &= \{1\} - \{4\} \\
    &= \{1\}
\end{aligned}
$$. 
UQS = $\{Q_1, Q_2\}$

- MAJ U3 : DW reçoit $U_3 = insert(r_1, [4, 2])$. DW envoie 

$$
\begin{aligned}
Q_3 &= V(U_3) - UQS(U_3) \\
    &= V(U_3) - (Q_1(U_3) \cup Q_2(U_3)) \\
    &= \pi_W([4, 2] \Join r_2) - (\underbrace{\pi_W([4, 2] \Join r_2)}_{=\{4\}} \cup \underbrace{\pi_W(r_1 \Join [2, 5])}_{=\{1\}}) \\
    &= 
\end{aligned}
$$

ça m'a fané

## Exercice 2

$V = \pi_{W, Y}(r_1 \Join r_2)$

$UQS = \emptyset, Collect = \emptyset, V = \{[1, 3]\}$

$U_1 = insert(r_2, [5, 4])$  
$Q_1 = \pi_W(r_1 \Join [5, 4])$  
$UQS = \{Q_1\}$

$U_2 = insert(r_1, [3, 5])$  
$$ 
    \begin{aligned}
    Q_2 &= \pi_W([3, 5] \Join r_2) - UQS(U_2) \\
    &= \pi_W([3, 5] \Join r_2) - Q_1(U_2) \\
    &= \pi_W([3, 5] \Join r_2) - \pi_W([3, 5] \Join [5, 4]) \\
    \end{aligned}
$$  
$UQS = \{Q_1, Q_2\}$

$U_3 = delete(r_1, [1, 5])$
$$
    \begin{aligned}
    Q_3 &= \pi_W(-[1, 5] \Join r_2) - UQS(U_3) \\
    &= \pi_W(-[1, 5] \Join r_2) - Q_1(U_3) - Q_2(U_3) \\
    &= \pi_W(-[1, 5] \Join r_2) - \pi_W(-[1, 5] \Join [5, 4]) \\
    \end{aligned}
$$
$UQS = \{Q_1, Q_2, Q_3\}$

$A_1 = 