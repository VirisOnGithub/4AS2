#import "../template/polytech.typ": *

#show: conf(doctitle: "Fiche de tips", subject: "Optimisation Discrète", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = PLNE

  == Étapes
  - Variables
  - Fonction objectif
  - Contraintes

  == Choisir ses variables
  - "Quelle décision dois-je choisir ?" => Chaque variable doit correspondre à une décision à prendre;

  =Exemple= : Choisir un véhicule

  == Catégorie de variables
  - Décisions => binaire
  - Quantités => nombres entiers

  == Contraintes
  - Écrire d'abord en français pour savoir ce à quoi elle corresponde
  - Traduire ensuite en mathématiques
  - Penser aux intervalles de la solution (surtout pour les variables entières)

  == Exemples

  === Affectation

  On possède 2 machines et 3 tâches à effectuer. Pour chaque machine $j$, une tâche $i$ prendra $t_(i,j)$ de temps à s'effectuer. De plus, chaque machine $j$ ne peut travailler qu'un certain temps, nommé $T_j$. Comment peut-on minimiser le temps total ?

  =Variables= : On pose, $forall i in [|1, 3|], forall j in [|1, 2|], x_(i, j)$ qui vaut 1 si la tâche $i$ est effectuée par la machine $j$, 0 sinon.

  =Fonction objectif= : Minimiser $sum_(i=1)^3 sum_(j=1)^2 t_(i,j) x_(i,j)$

  =Contraintes= :

]

