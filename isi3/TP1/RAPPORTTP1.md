**Nom/Prénom Etudiant  :**



# Rapport TP1 : Design Pattern Stratégie / Etat

## Exercice 
Question 1: *répondre à la question*

Quand on veut ajouter une nouvelle opération, on est obligé de modifier le code de la calculatrice, et donc on ne respecte pas le principe OCP, car on devrait pouvoir ajouter des modifications sans pour autant avoir à modifier le code existant.

Question 2: 

![](https://mermaid.ink/svg/pako:eNqdUU1Pg0AQ_SubOWmkhM9SNg2JqXcP3gyXKTtQIrBkWRIV-e8uoFhNL3VO897kvTezO0AmBQGHrMKueyixUFinDTM1M-yAVdZXpDWxYeGnussriZpl8xA13SwYLbY0R4s9tqRQS8Vke7sIx3PfdXzmut-XjSaVY0ZJckXYJf97Ia5a-JLHU3_stMJM_9NpaaZNbPsjWU9e6LOH3Wz-Dn-SfynBgkKVArhWPVlQk6pxgjDvl4I-UU0pcNMKVC8ppM1oNC02z1LW3zIl--IEPMeqM6hvhbni6-dXVlEjSB1k32jgkevNJsAHeAW-jezYCVzXi3f-Nnb80II34F7g2YEfBbEfhnFkKhwteJ9jHXtn0CezocBf)

Question 3:
Lors de l'ajout de nouvelles opérations, on doit ajouter une nouvelle classe Java qui implémente l'interface `Operator`, et on ne modifie donc pas le code existant. Le principe OCP est donc respecté, puisqu'aucune modification du code existant de la calculette n'est nécessaire pour ajouter une opération.

Question 4:
Avec le pattern Stratégie, on utilise une interface pour pouvoir dépendre uniquement de l'interface `Operator` pour l'opération, et non des concrétisations `Add` et `Substract`.





