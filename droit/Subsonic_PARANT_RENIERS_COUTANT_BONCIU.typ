#import "@local/polytech:1.0.0": *

#let q(body) = pad(top: 0.69em)[
  #box(fill: luma(230), inset: 1em, radius: 10pt)[
    #text(size: 1.3em, "“" + body + "”")
  ]
]

#show link: underline

#show: conf(doctitle: "Subsonic : Étude de cas", theme: rgb("#186db4"), subject: "Droit PI")[
  #titlepage(
    authors: (
      "Louison Parant, Clément Reniers, Luka Coutant, Alin Bonciu",
    ),
    logo: image("logo_subsonic.webp"),
  )

  = Question 1

  #q(
    "Quelle(s) protection(s) industrielle(s) pouvez-vous utiliser pour les manettes de PS ? Décrivez le système de protection pour un brevet et le coût de cette protection.",
  )

  Les manettes de PlayStation peuvent être protégées par les dispositifs suivants :

  - Protection de la marque : la marque "PlayStation" et ses logos sont des marques déposées par Sony, empêchant d'autres entreprises de les utiliser sans autorisation. Ainsi, Subsonic ne peut pas utiliser le nom "PlayStation" ou les logos associés pour ses manettes compatibles sans risquer une action en contrefaçon de marque ;

  #figure(
    grid(
      columns: (200pt, 1fr),
      align(image("Sony_logo.svg", height: 5%), center + horizon),
      align(image("ps.png", height: 10%), center + horizon),
    ),
    caption: "Les logos déposés de la marque SONY et de la marque PlayStation",
  )

  - Protection du design : les formes de la manette, des joysticks, des boutons, leur position et leur couleur sont protégés par un dessin industriel pour éviter d'en copier l'apparence. Par exemple, quand Microsoft a sorti sa manette Xbox, elle a dû intégrer un design différent, notamment en échangeant les joysticks ;

  #figure(
    grid(
      columns: 2,
      column-gutter: 12pt,
      rounded-image(box(image("xbox.png", width: 50%), height: 120pt)),
      rounded-image(box(pad(image("manette2-removebg-preview.png", width: 50%), y: 20pt), height: 120pt)),
    ),
    caption: "Différence entre les designs de la manette Xbox Elite Series Core 2 (à gauche) et la manette Sony DualShock PS4 (à droite)",
  )

  - Protection par brevet : les objets techniques contenus à l'intérieur de la manette sont tous brevetés pour protéger l'accès à la technologie des manettes de PS.

  Ici Sony fait valoir le brevet d'invention : c'est un outil qui permet de protéger une invention technique. La solution est révélée au public, et le titulaire du brevet peut l'utiliser exclusivement pendant 20 ans.

  La demande d'un tel brevet doit se faire auprès de l'INPI (Institut National de la Propriété Industrielle) en France, ou auprès de l'Office Européen des Brevets (OEB) en Europe.

  En France, le coût du brevet d'invention se divise en plusieurs étapes @inpi :

  - Le dépôt coûte 26 euros;
  - La recherche coûte 520 euros;
  - La délivrance coûte 90 euros;
  - Les annuités coûtent 38 euros la première année, puis augmentent chaque année pour atteindre 800 euros la 20ème année.

  En Europe l'OEB indique que la délivrance d'un brevet européen coûte environ 6800€ @oeb, sans compter les annuités, qui doivent être payées dans chaque pays où le brevet est validé.

  Dans le monde, on peut initier une demande de brevet international via le système PCT (Traité de coopération en matière de brevets), qui permet de protéger une invention dans les 158 états membres du traité. @pct Les coûts d'une demande de brevet international sont très élevés (en raison des taxes prises par chaque état). De plus, trouver les coûts exacts est assez compliqué (chaque pays utilise sa propre monnaie, et les taux de change varient)#footnote[Le fichier PDF regroupant l'entièreté des coûts pour l'année 2026 est trouvable ici : #link("https://www.wipo.int/documents/d/pct-system/docs-en-fees.pdf")].


  = Question 2

  #q(
    "Identifiez le type de contrat qui peut lier SubSonic avec Sony, le fabriquant de la Playstation, afin qu’elle fabrique et vende des manettes compatibles avec les PS.",
  )

  Le contrat qui peut lier SubSonic à Sony est un contrat de licence officielle, comme le fait souvent Sony pour les produits dérivés de ses consoles : @licence

  Cette licence permettrait à SubSonic de fabriquer et vendre des manettes compatibles avec les PS, tout en respectant les droits de propriété intellectuelle de Sony. En échange, SubSonic devrait payer des redevances à Sony pour l'utilisation de sa technologie et de ses marques.

  De plus, la licence est un contrat long et fastidieux à mettre en place : Sony exige de ses produits sous licence qu'ils respectent des normes de qualité interne, un design conforme à celui des produits Sony, et également une compatibilité avec les consoles PS. Sony indique également que "selon la catégorie, des rapports d'essais supplémentaires peuvent être requis pour l'approbation. La validation comprend, sans toutefois s'y limiter, les éléments suivants : Compatibilité des appareils, Fiabilité, Qualité, Fonctionnalité, Tests de régression en cours"#footnote("En anglais sur le site internet").

  C'est donc un contrat lourd, qui explique également le nombre assez réduit de partenaires que possède Sony : actuellement, seules 20 entreprises (mentionnées sur le site de Sony) possèdent une licence officielle pour fabriquer des produits compatibles avec les PS.

  #rounded-image(
    image("partners.png"),
    caption: "Les 20 partenaires officiels de Sony pour la fabrication de produits compatibles avec les PS",
  )

  = Question 3

  #q(
    "Vous devez exposer à votre employeur dans quel cas il peut y avoir contrefaçon de brevet, quelles sont les conditions de l’action en contrefaçon. Définissez la notion de concurrence déloyale.",
  )

  La contrefaçon de brevet consiste en l'exploitation non autorisée d'une invention protégée par un brevet. Pour qu'il y ait contrefaçon, il faut remplir au moins une des conditions suivantes :

  - fabrication du produit breveté
  - utilisation du produit breveté
  - vente du produit breveté
  - importation/exportation du produit breveté
  - utilisation d'un procédé breveté

  Pour poursuivre le fautif (ou l'entité fautive), il faut d'abord que le plaignant soit le titulaire du brevet ou le licencié exclusif. Il faut aussi qu'il apporte la preuve concrète de ce qu'il avance. Par exemple, dans le cas de Sony, Sony devrait prouver que les manettes de Subsonic exploite la technologie brevetée par Sony, notamment en apportant une manette Subsonic mise sur le marché possédant les éléments techniques brevetés.

  La concurrence déloyale, elle concerne les pratiques commerciales trompeuses d'une entreprise envers une autre, ou un marché. Elle peut se manifester sous plusieurs formes, notamment :

  - la désorganisation : par exemple en débauchant massivement les employés d'une entreprise concurrente pour affaiblir cette dernière ;
  - la confusion : l'entreprise fautive crée une ressemblance avec une autre entreprise dans le but de tromper les consommateurs;
  - le dénigrement : l'entreprise fautive dévalue les produits ou services de son concurrent pour détourner sa clientèle;

  Sony a d'ailleurs attaqué Subsonic pour concurrence déloyale en 2022 @deloy22, en raison de la ressemblance de fonctionnement entre les deux modèles de manette et d'une contrefaçon de brevet. L'Autorité de la concurrence a cependant rejeté la plainte de Sony pour concurrence déloyale, en raison de l'absence d'apport de preuves suffisantes et concrètes de la part de Sony.

  = Question 4

  #q(
    "Quel recours juridique a Subsonic contre ces manœuvres anticoncurrentielles ? Décrivez les sanctions qui ont été prononcées à l’encontre de Sony et l’organisme français qui a prononcé ces sanctions.",
  )

  Texte de réponse : Décision de l'Autorité de la concurrence : @concu

  Pour sanctionner Sony, Subsonic peut faire les actions suivantes :

  - Saisir l'Autorité de la concurrence sur la base de l'article L.420-2 du Code du commerce @art420, pour "l'exploitation abusive par une entreprise ou un groupe d'entreprises d'une position dominante sur le marché intérieur", en raison de "conditions de vente discriminatoires".
  - Saisir la comission européenne sur la base de l'article 102 du Traité sur le fonctionnement de l'Union européenne @art102.

  La décision n°23-D-14 du 20 décembre 2023 de l'Autorité de la concurrence a condamné l'entreprise Sony, comprenant ses filières japonaises, européennes et françaises, à hauteur de 13 527 000 euros (page 90).

  #block-full(title: "Autorité de la concurrence")[
    L'Autorité de la concurrence est une autorité administrative indépendante française chargée de veiller au respect des règles de la concurrence sur les marchés. C'est notamment elle que les entreprises saisissent pour dénoncer des pratiques anticoncurrentielles, comme dans le cas de Subsonic contre Sony.
  ]

  #pagebreak()

  #bibliography("bib.yaml", title: "Bibliographie")
]
