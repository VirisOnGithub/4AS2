### Question 2.8.2

Écrire le programme dual, le résoudre par la méthode du simplexe et en déduire la solution du problème initial.

Le programme dual est:
$$
\begin{cases}
\text{Maximiser :} w = 6y_1 + 6y_2 + y_3\\
\text{Sous les contraintes :} \\
3y_1 + 2y_2 + y_3 \leq 2\\
2y_1 + 3y_2 \leq 1\\
y_1, y_2, y_3 \geq 0
\end{cases}
$$

On rajoute des variables d'écart pour les contraintes d'inégalité:
$$
\begin{cases}
\text{Maximiser :} w = 6y_1 + 6y_2 + y_3\\
\text{Sous les contraintes :} \\
3y_1 + 2y_2 + y_3 + y_4 = 2\\
2y_1 + 3y_2 + y_5 = 1\\
y_1, y_2, y_3, y_4, y_5 \geq 0
\end{cases}
$$

Puis on applique la méthode du simplexe :

| | $y_1$ | $y_2$ | $y_3$ | $y_4$ | $y_5$ | |
| --- | --- | --- | --- | --- | --- | --- |
| $y_4$ | 3 | 2 | 1 | 1 | 0 | 2 |
| $y_5$ | 2 | 3 | 0 | 0 | 1 | 1 |
| | 6 | 6 | 1 | 0 | 0 | 0 |

Itération 1, on sélectionne $y_1$ comme variable entrante:
La variable sortante est $y_5$:
$$
\begin{cases}
2/3 \approx 0.667 \\
1/2 = 0.5
\end{cases}
$$

$$
\begin{cases}
L'_1 = L_1 - 3 * L'_2\\
L'_2 = L_2 / 2\\
L'_3 = L_3 - 6 * L'_2
\end{cases}
$$

| | $y_1$ | $y_2$ | $y_3$ | $y_4$ | $y_5$ | |
| --- | --- | --- | --- | --- | --- | --- |
| $y_4$ | 0 | -5/2 | 1 | 1 | -3/2 | 1/2 |
| $y_1$ | 1 | 3/2 | 0 | 0 | 1/2 | 1/2 |
| | 0 | -3 | 1 | 0 | -3 | -3 |

Itération 2, on sélectionne $y_3$ comme variable entrante:
La variable sortante est $y_4$:
$$
\begin{cases}
1/1 = 1 \\
0/1 = \infty
\end{cases}
$$

$$
\begin{cases}
L''_1 = L'_1 / 1\\
L''_2 = L'_2\\
L''_3 = L'_3 - L''_1
\end{cases}
$$

| | $y_1$ | $y_2$ | $y_3$ | $y_4$ | $y_5$ | |
| --- | --- | --- | --- | --- | --- | --- |
| $y_3$ | 0 | -5/2 | 1 | 1 | -3/2 | 1/2 |
| $y_1$ | 1 | 3/2 | 0 | 0 | 1/2 | 1/2 |
| | 0 | -1/2 | 0 | 0 | -3/2 | -5/2 |