# ISI3 TP 2: Design Pattern Decorateur
Laëtitia Matignon

> Objectifs du TP
> - Comprendre et mettre en oeuvre les patterns Décorateur et les patterns de Création
> - Vérifier l’application des principes SOLID

Le Décorateur est un patron de conception qui permet d’ajouter **dynamiquement** de nouveaux comportements à des objets en les plaçant à l’intérieur d’objets spéciaux appelés décorateurs (*wrappers*).

![decorateur](img/decorator.png)

## Enoncé

A un distributeur de Cafés, les clients ont le choix entre 3 types de Café: Colombia (0.5€), Expresso (0.6€), et Deca (0.4€). 

Chaque café a une recette différente. On souhaite pouvoir afficher la recette , le nom et le prix du Café choisi.

Une solution vous est proposée dans le [package exerciceCafe1](src/exerciceCafe1/) qui sera le point de départ de cet exercice.


### Itération 1

On souhaite maintenant pouvoir ajouter des suppléments aux boissons : Lait, Sucre et Caramel. L'ajout de sucre est facturé 0.1€, de lait  0.3€, de caramel 0.5€. 


- proposez **deux solutions simples (sans design pattern)** dans le rapport pour réaliser cela. Ces solutions doivent être différentes de l'itération 2. Vous donnerez le **diagramme de classes de vos 2 solutions** .

=> Après le choix de la boisson, on propose un supplément en mode do/while
=> Après le choix de la boisson, l'utilisateur rentre tous ses suppléments en chaîne de caractères (sucre/lait/caramel)

- **discutez les avantages et inconvénients** de vos solutions par rapport aux principes SOLID. 

- implémentez une des 2 solutions dans le [package exerciceCafe2](src/exerciceCafe2/). 


### Itération 2: Pattern Décorateur

Appliquez maintenant le design pattern Décorateur pour pouvoir commander des cafés avec des suppléments. 

Exemple d'affichage par le Distributeur après le choix:

> Vous avez choisi: Boisson{nom='Deca', prix=0.4} avec supplement : Lait (prix: 0.1) avec supplement : Caramel (prix: 0.2)

> recette de votre boisson : 

> Preparation du Deca

> ajout de supplement : Lait

> ajout de supplement : Caramel

> Prix a payer: 0.7`

- Vous mettrez votre code dans le [package exerciceCafe3](src/exerciceCafe3/). 

- Présentez votre solution dans le rapport en donnant le diagramme de classes et le rôle de chaque classe.

- Discutez le respect des principes SOLID en comparaison avec les solutions de l'itération 1.


### Itération 3: Fabrique Simple

On souhaite maintenant que la classe **Distributeur** soit fermée aux modifications lors de l'ajout de nouveaux types concrets de Boissons et d'Ingrédients. 

- En repartant de l'itération 2, proposer une solution que vous mettrez dans le [package exerciceCafe4](src/exerciceCafe4/), qui utilise la **fabrique simple**.

