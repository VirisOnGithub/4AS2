#grid(
  column-gutter: 295pt,
  columns: 2,
  align("Clément RENIERS", left), align("3A Informatique", right),
)

#v(2cm)

#align(heading("Analyse réflexive d’une situation professionnelle"), center)

#v(1cm)

= Contexte

Lors de mon stage au sein du pôle Data de Xerfi, j’ai été confronté à un problème  technique significatif dans le cadre d’un projet client portant sur le scraping  de sites d’assurances automobiles#footnote("Le scraping désigne la récupération automatique de données à partir de sites web"). L'objectif de ce projet était de remplir automatiquement des devis d'assurance auto et habitation.

Initialement, nous avions envisagé d'utiliser un navigateur _headless_#footnote("Un navigateur headless est uniquement la partie interne d'un navigateur, sans l'interface graphique, utile notamment pour l'automatisation.") pour simuler une interaction humaine avec les sites d'assurances.

Cependant, les assurances possèdent des systèmes qui ont pour but d'empêcher les programmes comme le mien de récupérer leurs données (nous appelerons ça des anti-bots).
Le véritable challenge pour moi était donc de faire face à ces systèmes anti-bots, et les comprendre pour mieux les contourner.

= Choix de cette expérience

Ce moment était un tournant dans mon stage. L'entreprise m'a véritablement donné carte blanche pour trouver la solution à ces problèmes, et j'ai pu à cette occasion utiliser des solutions techniques qui n'avaient jamais été envisagées auparavant, avec des fonds d'une entreprise que je n'aurais pas pu obtenir en tant que particulier. Ça a été au global une expérience enrichissante, ce qui explique mon choix de l'analyser dans ce travail.

= Gestion de la situation

La première partie pour moi a été l'analyse complète de la situation. En effet, les systèmes anti-bots sont très variés, et la plupart ne se déclenchent pas à chaque fois, ce qui rend l'analyse de leur fonctionnement assez difficile. J'ai cependant réussi à identifier les causes de notre détection assez rapidement (en vérité, les navigateurs automatisés sont détecté assez facilement par les anti-bots). À la suite d'une réunion avec l'équipe Data, nous avons donc convenu, sous mon impulsion, d'essayer de trouver une autre stratégie. J'étais à ce moment-là le seul développer sur le projet, je pouvais donc explorer toutes les options qui pouvaient sembler prometteuses.

La deuxième partie de ma gestion était de trouver une alternative et de convaincre ma hiérarchie de l'intérêt de ma solution. Pour ça j'ai d'abord passe à peu près une semaine à essayer énormément de situation différentes. J'ai également pu avoir une réunion avec les membres de l'équipe technique l'entreprise à Paris : eux avaient déjà travaillé sur différentes technologies web et m'ont permis d'avoir une vision plus grande des possiblités. La solution finale a été d'utiliser une extension web#footnote("Une extension web est un petit programme qui s'intègre dans un navigateur, lui ajoutant des fonctionnalités"). Cette solution permettait non seulement au programme de contourner les protection anti-bots, mais aussi de paraître beaucoup plus humain, et donc de se fondre dans la masse.

#pagebreak()

= Ressenti

Au démarrage du projet, j'avais déjà une appréhension éthique à l'idée de faire du scraping, même si l'expérience a été, avec du recul, une expérience utile pour le futur ingénieur que je vais devenir.

Quand j'ai compris le problème technique, j'ai dû prendre un peu de temps pour essayer de comprendre comment j'allais faire : à l'échelle individuelle, je n'ai jamais été confronté à des problèmes d'une telle ampleur (si l'entreprise se fait repérer en tant que scraper, elle risque d'avoir une mauvaise réputation auprès d'autres entreprises). C'était pour moi une grande responsabilité, et cela m'a généré un stress important.

De plus, le client imposait des contraintes de temps assez fortes, ce qui a ajouté une pression supplémentaire. J'ai dûredoubler d'efforts pour livrer un POC#footnote[_Proof of Concept_, preuve de concept] fonctionnel dans les délais impartis, ce qui a été une source de stress supplémentaire.

Ensuite même avec le POC finalisé, le client, très exigeant, a demandé de nombreuses modifications, ce qui entraînait des retards conséquents sur les autres projets. Heureusement, les réunions régulières avec l'équipe Data m'ont permis de rester performant sans me plonger dans des détails importants qui auraient pu me faire perdre du temps.

Enfin, nous étions limité en terme de coût, nous ne pouvions pas non plus nous permettre d'acheter des solutions déjà faites, qui auraient été plus simple, mais moins enrichissantes pour moi.

= Conclusion

Ce problème m'a permis d'aller puiser dans des ressources très différentes de celles que j'avais l'habitude d'utiliser, j'ai dû faire preuve de créativité pour trouver une solution contre les anti-bots, tout en gardant en tête les contraintes de temps et d'argent que nous avions. J'ai aussi énormément développé mes compétences de travail en équipe, en collaborant avec les autres membres de l'équipe à la fois dans le travail quotidien ou au cours des différentes réunions que nous avions. Enfin, j'ai aussi appris à gérer le stress et la pression liés à ce projet, en trouvant un équilibre entre les différentes tâches à accomplir.
