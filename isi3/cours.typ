#import "../template/polytech.typ": *

#show: conf(theme: blue, subject: "ISI3", doctitle: "Principes avancés de conception OO")[
  #titlepage(
    authors: ("Clément RENIERS", ""),
  )

  = Principe de conceptions des classes

  #block-full(title: "Principes de conception SOLID")[
    - Principe de responsabilité unique (Single Responsibility Principle)
    - Principe ouvert/fermé (Open/Closed Principle)
    - Principe de substitution de Liskov (Liskov Substitution Principle)
    - Principe de ségrégation des interfaces (Interface Segregation Principle)
    - Principe d'inversion des dépendances (Dependency Inversion Principle)
  ]

  == Principe de responsabilité unique (Single Responsibility Principle)

  #block-left(title: "Principe de responsabilité unique")[
    Une classe ne doit avoir qu'une seule raison de changer. \
    Pour cela, une classe doit se focaliser sur un seul objectif fonctionnel.
  ]

  == Principe ouvert/fermé (Open/Closed Principle)

  #block-left(title: "Principe Open/Closed")[
    Les entitées informatiques, (paquetages, classe, méthodes) doivent être ouvertes aux extensions mais fermées aux modifications.
  ]

  #block-right(title: "Principes")[
    - Ouvert aux extensions:
      - Le comportement d'une classe peut être étendu
    - Fermé aux modifications:
      - Le comportement d'unc classe doit pouvoir être étendu mais sans modification du code source.
    - L'ajout de fonctionnalités doit se faire en ajoutant du code et non en modifiant le code existant.
  ]

  == Principe de substitution de Liskov (Liskov Substitution Principle)

  #block-left(title: "Principe de substitution de Liskov")[
    Une instance d'une classe doit pouvoir être substituée par une instance d'une sous-classe sans altération de la compilation ni du comportement du programme.
  ]

  #block-full(title: "Conséquences", stroke-color: red)[
    Soit B héritant de A.
    Ne pas introduire de modification dans le fonctionnement de B qui rend inutilisatble tout objet de type B utilisé comme un objet de type A.
  ]

  == Principe de Séparation des interfaces (Interface Segregation Principle)

  #block-left(title: "Principe de Séparation des interfaces")[
    Un client ne doit jamais être forcé de dépendre d'une interface qu'il n'utilise pas.
  ]

  == Principe d'inversion des dépendances (Dependency Inversion Principle)

  #block-left(title: "Principe d'inversion des dépendances")[
    Dépendez des abstractions, pas des concrétisations.
  ]
]
