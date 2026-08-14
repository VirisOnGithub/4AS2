# TD3 — Apprentissage profond (MLP) — Correction

Date : 22 mai 2026

## Exercice 1 — Forward et backpropagation

On considère un MLP de régression avec :
- entrée $x \in \mathbb{R}^2$ et biais $1$,
- 1 couche cachée de 2 neurones avec activation sigmoïde $\sigma(z)=\frac{1}{1+e^{-z}}$,
- 1 neurone de sortie à activation linéaire (donc $\hat y = z_1^{(2)}$).

Poids donnés :
- couche 1 (entrée → cachée) :
  - $w^{(1)}_{0,1}=-1$, $w^{(1)}_{1,1}=2$, $w^{(1)}_{2,1}=1$
  - $w^{(1)}_{0,2}=2$,  $w^{(1)}_{1,2}=-1$, $w^{(1)}_{2,2}=-1$
- couche 2 (cachée → sortie) :
  - $w^{(2)}_{0,1}=1$, $w^{(2)}_{1,1}=2$, $w^{(2)}_{2,1}=-1$

Exemple : $x = \begin{bmatrix}2\\3\end{bmatrix}$, $y=10$.

### Question 1

**Fonction de coût (régression)** : une erreur quadratique, par exemple

$$
L = \frac{1}{2}(\hat y - y)^2 \quad (\text{ou MSE si on moyenne sur un dataset}).
$$

**Intérêt de la sigmoïde en couche cachée** :
- introduit une **non-linéarité** (sinon un empilement de couches linéaires resterait linéaire),
- est **dérivable** ⇒ permet la rétropropagation,
- borne les activations dans $(0,1)$ (utile pour stabilité/interprétation, même si elle peut saturer).

### Question 2 — Calcul de l’erreur (forward)

1) **Couche cachée**

$$
\begin{aligned}
z_1^{(1)} &= w^{(1)}_{0,1} + w^{(1)}_{1,1}x_1 + w^{(1)}_{2,1}x_2
= -1 + 2\cdot 2 + 1\cdot 3 = 6\\
a_1^{(1)} &= \sigma(6) \approx 0.997527\\
\\
z_2^{(1)} &= w^{(1)}_{0,2} + w^{(1)}_{1,2}x_1 + w^{(1)}_{2,2}x_2
= 2 + (-1)\cdot 2 + (-1)\cdot 3 = -3\\
a_2^{(1)} &= \sigma(-3) \approx 0.047426
\end{aligned}
$$

2) **Sortie (linéaire)**

$$
\hat y = z_1^{(2)} = w^{(2)}_{0,1} + w^{(2)}_{1,1}a_1^{(1)} + w^{(2)}_{2,1}a_2^{(1)}
= 1 + 2\cdot 0.997527 - 1\cdot 0.047426 \approx 2.947629.
$$

3) **Erreur**

- écart : $\hat y - y \approx 2.947629 - 10 = -7.052371$
- avec $L=\tfrac{1}{2}(\hat y-y)^2$ :

$$
L \approx \frac{1}{2}\,(7.052371)^2 \approx 24.867969.
$$

(avec la convention sans $\tfrac{1}{2}$, on obtient $(\hat y-y)^2 \approx 49.735938$).

### Question 3 — Mise à jour de $w_{2,1}^{(1)}$ (descente de gradient en ligne)

On prend $L = \tfrac{1}{2}(\hat y - y)^2$.

- terme d’erreur en sortie (activation linéaire) :

$$
\delta^{(2)} = \frac{\partial L}{\partial z_1^{(2)}} = \hat y - y.
$$

- rétropropagation vers le neurone caché 1 :

$$
\delta^{(1)}_1 = \frac{\partial L}{\partial z_1^{(1)}}
= \delta^{(2)}\, w^{(2)}_{1,1}\, \sigma'(z_1^{(1)}),
\quad \text{avec } \sigma'(z)=\sigma(z)(1-\sigma(z)).
$$

- gradient pour le poids $w_{2,1}^{(1)}$ (qui multiplie $x_2$) :

$$
\frac{\partial L}{\partial w^{(1)}_{2,1}} = \delta^{(1)}_1\, x_2
= (\hat y-y)\, w^{(2)}_{1,1}\, \sigma'(z_1^{(1)})\, x_2.
$$

**Mise à jour (SGD en ligne)** :

$$
 w^{(1)}_{2,1} \leftarrow w^{(1)}_{2,1} - \eta\, \frac{\partial L}{\partial w^{(1)}_{2,1}}.
$$

Numériquement :
- $\hat y-y \approx -7.052371$
- $\sigma'(6)=\sigma(6)(1-\sigma(6)) \approx 0.002466509$
- $w^{(2)}_{1,1}=2$, $x_2=3$

Donc

$$
\frac{\partial L}{\partial w^{(1)}_{2,1}} \approx -0.1043684.
$$

Avec $w^{(1)}_{2,1}=1$ au départ :

$$
 w^{(1)}_{2,1}\,\text{(nouveau)} = 1 - \eta(-0.1043684) = 1 + 0.1043684\,\eta.
$$

---

## Exercice 2 — Comparaison MSE vs Cross-Entropy (classification multi-classe)

Données :
- $K=3$
- logits $z = (2,\,1,\,-1)^\top$
- vérité terrain (one-hot) pour la classe 1 : $y=(1,\,0,\,0)^\top$
- softmax :

$$
 p_i = \frac{e^{z_i}}{\sum_{j=1}^3 e^{z_j}}.
$$

### Question 1 — Perte MSE sur les logits

Perte :

$$
L_{\mathrm{MSE}} = \frac{1}{2}\sum_{i=1}^3 (z_i - y_i)^2.
$$

Gradient :

$$
\frac{\partial L_{\mathrm{MSE}}}{\partial z_i} = z_i - y_i.
$$

Valeurs numériques :

$$
\nabla_z L_{\mathrm{MSE}} = z-y = (2-1,\,1-0,\,-1-0) = (1,\,1,\,-1).
$$

**Interprétation (classe correcte i=1)** : le gradient est **positif** ($+1$), donc une descente de gradient ferait
$z_1 \leftarrow z_1 - \eta\cdot 1$ ⇒ **diminue** le logit de la classe correcte (ici parce qu’on “vise” $z_1=1$).
Cela illustre que la MSE appliquée directement aux logits n’est pas toujours un bon choix en classification.

### Question 2 — Entropie croisée

Perte :

$$
L_{\mathrm{CE}} = -\sum_{i=1}^3 y_i \log(p_i).
$$

Gradient (donné) :

$$
\frac{\partial L_{\mathrm{CE}}}{\partial z_i} = p_i - y_i.
$$

Calcul de $p$ (softmax) :
- $e^2\approx 7.3891$, $e^1\approx 2.7183$, $e^{-1}\approx 0.3679$, somme $\approx 10.4752$
- $p \approx (0.705385,\,0.259496,\,0.035119)$

Donc

$$
\nabla_z L_{\mathrm{CE}} = p-y \approx (-0.294615,\,0.259496,\,0.035119).
$$

**Interprétation (classe correcte i=1)** : le gradient est **négatif**, donc une descente de gradient fait
$z_1 \leftarrow z_1 - \eta(-0.2946)$ ⇒ **augmente** le logit de la classe correcte, ce qui va dans le bon sens.
