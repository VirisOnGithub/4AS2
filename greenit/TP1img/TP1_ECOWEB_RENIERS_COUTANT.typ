#import "@local/polytech:1.0.0": *

#let grad = 240

#set page(fill: rgb(grad, grad, grad))

#show: conf(doctitle: "TP1 Écoconception", theme: green)[
  #titlepage(authors: ("Clément RENIERS", "Luka COUTANT"), toc: false)

  = Introduction

  En 2022, une étude de l’ADEME a révélé que le numérique représente 4% de l'empreinte carbone mondiale#footnote(link("https://ecoresponsable.numerique.gouv.fr/docs/2024/etude-ademe-impacts-environnementaux-numerique.pdf")). Ce chiffre souligne l'importance de l'écoconception dans le développement d'un site web.

  C'est dans ce cadre que nous avons recréé un site internet d'une grande entreprise, pour souligner les améliorations possibles en termes d'écoconception.

  Nous avons trouvé un exemple parfait : l'entreprise SodaStream, filière du groupe PepsiCo, qui se vend comme une entreprise écoresponsable, en vendant des machines à gazéifier l'eau du robinet, afin de réduire la consommation de bouteilles en plastique.

  Notre objectif dans ce projet sera d'analyser le site de SodaStream, de souligner les points négatifs, et de proposer des améliorations justifiées.

  Nous mettrons un point d'honneur à ne pas dénaturer l'entreprise, afin de garder son image de marque.

  #pagebreak()
  #tableofcontents
  #pagebreak()

  = Etude de l'existant

  == Outils utilisés

  Concernant le site #link("sodastream.com"), que nous avons choisi d'analyser, nous avons utilisé trois outils : EcoGrader, EcoIndex et Website Carbon. Ces trois sites ont chacun leur importance :

  - EcoGrader est l'outil le plus détaillé sur les améliorations possibles, mais il est aussi le moins sévère. @sodaecograder
  - EcoIndex est le plus dur en termes de notation, et donne des pistes d'amélioration, sans pour autant rentrer dans les détails. @sodaecoindex
  - Website Carbon est, d'après nos tests préalables, le plus fiable des sites, mais il ne donne pas d'indice explicite sur sa notation. @sodawebsitecarbon

  == Étude de cas

  Nous avons analysé le site de SodaStream sur les trois sites susmentionnés, voilà les résultats que nous obtenons :

  #rounded-image(image("assets/image.png"), caption: "Note par EcoGrader : 52/100")
  #rounded-image(image("assets/image-1.png"), caption: "Note par EcoIndex : 9/100")
  #rounded-image(image("assets/image-2.png"), caption: "Note par WebsiteCarbon : F, sur une échelle de A à F")

  Les analyses révèlent plusieurs points négatifs du site :

  - Il est trop lourd : EcoIndex compte plus de 5.5Mo de données à charger, et EcoGrader révèle environ 0.9Mo de scripts complètement inutilisés. Le même site indique également environ 3Mo d'images reçues (onglet "Images" + "Other"), montant évidemment énorme
  - Il est beaucoup trop compliqué pour le navigateur : EcoIndex indique 1662 éléments dans le DOM. Par comparaison, le site médian comporte 693 éléments. Ce nombre est donc évidemment à réduire
  - Il fait trop de requêtes : EcoIndex compte 504 requêtes HTTP, alors que le site médian en fait 78. C'est aussi sur ce point qu'il faudra agir.

  Finalement le seul critère positif est que le site est hébergé sur un serveur utilisant de l'énergie renouvelable. C'est une bonne mesure, mais d'une part elle n'est pas suffisante, et d'autre part nous ne pourrons rien y faire.

  = Propositions justifiées

  Les propositions suivantes sont presque toutes issues du "référentiel Ecoconception web / les 115 bonnes pratiques"@ref.

  == Pratique n°1 : Ne pas retenir les fonctionnalités non essentielles

  Quand on arrive sur le site de SodaStream, le premier élément qui saute aux yeux est une claire publicité, qui change environ toutes les semaines.

  #rounded-image(
    image("assets/image-3.png"),
    caption: "Page d'accueil de SodaStream au 22 mars 2026. Deux publicités sont présentes",
  )

  Ces publicités ne sont pas très utiles ne donnent pas forcément l'envie d'acheter, et de manière générale, le site en est rempli. nous allons donc supprimer ces bannières inutiles.

  == Pratique n°10 : Limiter le recours aux carrousels

  Les carrousels sont éléments très pratique et utile pour l'interface utilisateur. Cependant ils peuvent être lourds, et inutiles. Sur le site de SodaStream, on en trouve un en page d'accueil, et il nous semble utile. Cependant, il possède 25 items, donc 5 affichés à la fois. Nous allons donc réduire le nombre d'items à afficher, et indiquer un lien si l'utilisateur souhaite poursuivre.

  #rounded-image(image("assets/image-4.png"), caption: "Carrousel présent sur le site actuel")

  == Pratique n°15 : N'utilisez que les portions indispensables des bibliothèques JS et CSS

  Nous avons vu que le site de SodaStream importe environ 1Mo de scripts inutilisés à chaque connexion. C'est un chiffre que nous allons réduire, avec un objectif d'avoir aucun script inutile.

  == Pratiques n°21 & 46 : Limiter le nombre d'appels aux API HTTP

  Le nombre de requêtes HTTP du site de SodaStream est énorme (504 requêtes), ainsi nous voulons limiter ce nombre. Nous allons notamment enlever toutes les requêtes liées au tracking de l'utilisateur (évidemment inutiles pour l'utilisateur et surtout à des fins de statistiques), et nous allons aussi réduire le nombre de requêtes liées à l'affichage du site (en réduisant le nombre d'images, en limitant les scripts, etc).

  == Pratique n°42 : Valider votre code avec un Linter

  Notre code sera validé avec le linter Biome#footnote("Biome est le linter le plus performant du marché, environ 35 fois plus rapide que Prettier, son principal concurrent. " + link("https://biomejs.dev/")), qui nous permettra de corriger les erreurs de code, et d'améliorer la qualité de notre code.

  == Pratique 49 : Préférer les glyphs aux images

  C'est le point le plus dur à corriger. Les images importées par sodastream incluent les boutons, les textes, les figures, etc. Or charger une image est toujours beaucoup plus coûteux que de charger des balises HTML et du CSS. Nous allons donc remplacer les images comme celle ci-dessus par des balises HTML pour améliorer la performance du site.

  #rounded-image(
    image("assets/image-5.png"),
    size: 50%,
    caption: [Une des images du site.#footnote("Cette image n'est pas une capture d'écran, mais bien une image importée par le site à chaque connexion.")],
  )

  == Pratique 67 : Utiliser certains forks applicatifs orientés "performance"

  Le site de SodaStream particulièrement Shopify, un CMS très populaire, qui est connu pour être particulièrement lourd.@techstack

  Pour améliorer ceci, nous allons utiliser un framework beaucoup plus léger qui permettra d'améliorer la performance. Après quelques recherches, nous avons choisi d'utiliser Astro. Astro utilise notamment Astro Islands pour n'importer que les éléments nécessaires à l'affichage du site, et ainsi réduire le nombre de scripts inutilisés.

  = Maquettes

  Ces maquettes ont été réalisées avec Figma, avant la création du site. Elles ne représentent donc uniquement l'idée de ce nous voulions faire, avec une précision relative.

  #rounded-image(image("assets/accueil1.svg"), caption: "Page d'accueil du nouveau site. ", size: 70%)
  #rounded-image(image("assets/accueil2.svg"), caption: "Suite de la page d'accueil avec le carrousel.", size: 70%)
  #rounded-image(image("assets/accueil3.svg"), caption: "Fin de la page d'accueil avec le footer.", size: 70%)

  = Points d'amélioration et d'attention

  == Cartes de présentation du site

  Les cartes présentés à la figure 7 étaient à l'origine des images, mais nous avons décidé de les remplacer par des balises HTML, pour pouvoir uniquement importer des photos. Ainsi nous importons uniquement l'image ci-dessous par exemple :

  #figure(
    caption: "Comparaison des images importées : \n à gauche sur le site de SodaStream, à droite dans notre site",
  )[
    #grid(
      columns: 2,
      gutter: 10mm,
      rounded-image(image("assets/image-6.png")), rounded-image(image("assets/refill_gas_no_bg.webp")),
    )
  ]

  Nous avons donc réduit considérablement le poids des images : l'image originale faisait environ 350ko, et l'image finale importée fait environ 35ko.

  En contrepartie, nous avons dû réimplémenter les mathématiques pour placer correctement les élements sur la carte, notamment les bulles, avec un peu de trigonométrie. C'est un calcul qui est réutilisé pour les deux cartes de chaque catégorie, et que nous estimons à moins de 10ko.

  #figure(caption: "Extrait de code servant à calculer les bulles", supplement: "Code")[
    ```js
    const smallBubblesAngle = angleToRadians(15);
    const mediumBubblesAngle = angleToRadians(38);
    const smallCircleOffsetY = Math.sin(smallBubblesAngle) * (bigCircleSize / 2 + smallCircleSize / 2);
    const smallCircleOffsetX = Math.cos(smallBubblesAngle) * (bigCircleSize / 2 + smallCircleSize / 2);
    const mediumCircleOffsetY = Math.sin(mediumBubblesAngle) * (bigCircleSize / 2 + mediumCircleSize / 2);
    const mediumCircleOffsetX = Math.cos(mediumBubblesAngle) * (bigCircleSize / 2 + mediumCircleSize / 2);
    ```
  ]

  == Approche mobile-first

  Aujourd'hui la majorité du trafic web est réalisé sur mobile. Il est donc important de penser à l'affichage mobile dès la conception du site. Nous avons donc dû l'intégrer dans la conception de notre site, en mettant par exemple un burger d'option sur la barre de navigation, ou en adaptant les options dans le footer de la page.

  = Résultats

  Notre projet une fois codé a été mis en ligne sur le site #link("https://sodastream.clement-reniers.fr/").

  Nous avons soumis notre site aux trois outils d'analyse que nous avons utilisés pour analyser le site de SodaStream, et voilà les résultats que nous obtenons :

  #rounded-image(
    image("assets/image-7.png"),
    caption: [Résultats de l'analyse du nouveau site avec EcoIndex @newecoindex],
  )
  #rounded-image(
    image("assets/image-8.png"),
    caption: [Résultats de l'analyse du nouveau site avec EcoGrader @newecograder],
  )
  #rounded-image(
    image("assets/image-9.png"),
    caption: [Résultats de l'analyse du nouveau site avec Website Carbon @newwebsitecarbon],
  )

  Notre nouveau site reçoit donc une note quasi parfaite sur les trois outils, et les seuls problèmes qui subsistent sont liés à l'hébergement du site, que nous ne pouvons pas changer.

  = Conclusion

  L'écoconception est un enjeu majeur aujourd'hui : Pour chaque 1000 visites du sites, nous économisons environ 5.6kg de CO#sub("2") équivalent#footnote[D'après Website Carbon, pour 1000 visiteurs, le site de SodaStream produit 5.98kg de CO#sub("2") équivalent. En comparaison, notre site en produit 0.35kg.]). L'énergie produite permet aussi d'alimenter une voiture électrique pour environ 90 km !

  #pagebreak()
  #bibliography("TP1bib.yml", title: "Bibliographie")
]
