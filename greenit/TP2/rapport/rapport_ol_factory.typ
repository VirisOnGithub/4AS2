#import "@local/polytech:1.0.0": *

#show link: underline

#show: conf(doctitle: "Rapport TP2 - Transformation Industry 4.0/5.0", subject: "Green IT", theme: blue)[
  #titlepage(authors: ("Clément RENIERS", "Louison PARANT", "Alin BONCIU"), toc: false)

  #pagebreak()
  #outline(depth: 3, indent: 1em, title: "Table des matières")

  #pagebreak()

  = Introduction

  #block-left(title: "Note")[
    _Cette entreprise est complètement fictive. Nous l'avons imaginée dans le cadre de l'exercice.\n En parallèle les chiffres voulus par l'entreprise sont des valeurs similaires à celles que nous avons vues pendant nos recherches._
  ]

  OL Factory, fabricant de maillots officiels pour l'Olympique Lyonnais depuis 2015, a un problème d'efficacité globale de sa production, ainsi qu'une empreinte écologique trop importante. Cette entreprise, employant 120 salariés sur son site de production principal, génère environ 25 tonnes de déchets textiles annuels et subit une pression croissante pour personnaliser rapidement les produits selon les demandes du marché.

  Ce rapport synthétise une analyse approfondie des solutions Industry 4.0 et 5.0 adaptées aux problématiques spécifiques d'OL Factory. L'objectif est d'présenter une vision complète de transformation numérique intégrant technologie, durabilité et compétences métiers, en mettant l'accent sur les aspects DDRS (développement durable and responsabilité sociétale).

  #pagebreak()

  == Volonté du Client

  OL Factory souhaite :

  + Réduire des 60% les délais de mise en marché pour nouveaux modèles personnalisés
  + Diminuer des 50% la consommation de ressources (eau, énergie, matières premières)
  + Atteindre une traçabilité totale des produits (de l'acheminement des matières premières jusqu'à la finition)
  + Implémenter une stratégie Industry 5.0 permettant la personnalisation de masse
  + Former ses équipes

  Cette transformation doit s'inscrire dans une démarche de responsabilité sociétale, avec un espoir de certification ISO 14001 @iso

  #pagebreak()

  = État Actuel de l'Usine OL Factory

  == Architecture Existante

  OL Factory dispose actuellement d'une infrastructure informelle :

  === Processus de Production
  + Conception : DAO traditionnel (CAO 2D sur un logiciel numérique comme AutoCAD @autocad)
  + Approvisionnement : Gestion manuelle des stocks
  + Production : Ateliers organisés par domaine (coupe, couture, impression, emballage)
  + Logistique : Gestion papier et Excel pour commandes clients
  + Vente : Site e-commerce basique, pas de suivi client
  + Données : Aucun système centralisé, fichiers dispersés sur serveurs locaux

  === Technologie Actuelle
  - Serveur local Linux pour partage fichiers
  - ERP basique et non adapté pour l'agrandissement de l'entreprise
  - Imprimantes 2D pour motifs, aucune personnalisation numérique
  - Gestion de stock manuelle avec saisies papier / Excel
  - Aucun capteur en production

  === Déchets et Impact Environnemental
  - 25 tonnes déchets textiles/an (2.8% production)
  - 800 m³ eau consommés/an pour teinture et nettoyage
  - Chauffage/climatisation non régulée : 150 MWh/an
  - Pas de suivi des émissions carbone

  === Compétences RH
  - Équipe production : \~90 personnes (couture, impression, emballage)
  - Équipe administrative : \~20 personnes (gestion, logistique)
  - Compétences IT : 2 personnes (admin système et développeur web)

  #pagebreak()

  = Cahier des Charges

  La transformation d'OL Factory s'articule autour de 5 axes majeurs, intégrés dans une architecture Industry 4.0/5.0 cohérente.

  == Objectifs Généraux

  === Objectif 1 : Accélération de l'Innovation (Fabrication Additive + CAO)
  - Réduire délai prototypage de 8 semaines à 2 semaines
  - Impression 3D dû à la nouvelle demande client (voir ci-dessous)
  - Réduire déchets de conception de 80%
  - Intégrer FabLab interne pour expérimentation

  #rounded-image(
    image("../assets/maillot_3d_ol.png"),
    size: 50%,
    caption: "Image des potentiels nouveaux motifs 3D voulus par les supporters",
  )

  === Objectif 2 : amélioration de la productivité
  - Réduire les accidents de travail liés aux troubles musculo-squelettiques de 70%
  - Déployer des robots qui assistants les couturiers #footnote[Ces robots sont communément appelés "cobots" (robots collaboratifs) : #link("https://fr.wikipedia.org/wiki/Cobotique")]

  === Objectif 3 : traçabilité totale et amélioration de l'expérience client
  - Intégrer capteurs RFID sur tous produits pour tracer leur passage en usine
  - Permettre clients de suivre leur commande en temps réel grâce à des numéros de suivis#footnote("En France, le service \"Lettre suivie\" de La Poste permet de suivre l'acheminement d'une lettre ou d'un colis grâce à un numéro de suivi unique")
  - Développer le service après-vente

  === Objectif 4 : analyse de la performance de l'usine
  - Centraliser données production, ventes, supply chain
  - Implémenter un _data lake_ pour analyser la production
  - Essayer de prédire les commandes clients à un horizon de 6 mois
  - Tracker impact environnemental en temps réel

  == Intégration aux Systèmes d'Information Existants

  L'architecture IT devra intégrer :

  - Nouvel ERP : SAP S/4HANA permettra de mieux répondre aux besoins de l'entreprise
  - _data lake_ : Architecture cloud pour Big Data avec AWS
  - Plateforme IoT : l'interface IoT d'AWS comprise avec le _data lake_ permettra de collecter les données des capteurs RFID et autres capteurs de production
  - Dashboard web : Intégration à l'interface existante d'un dashboard pour suivre les données collectées par les différents capteurs

  #pagebreak()

  = Solutions Proposées : Analyse Détaillée

  == Axe 1 : Fabrication Additive et Prototypage Rapide

  #rounded-image(
    image("../assets/mermaid-diagram.png"),
    size: 80%,
    caption: "Exemple de processus de prototypage rapide avec fabrication additive",
  )

  === Problématique pour OL Factory

  Actuellement, créer un nouveau motif ou prototype prend 8 semaines :
  - 1 semaine : Itérations CAO (beaucoup d'aller retours avec les designers)
  - 2 semaines : Commande tissu/matière
  - 3 semaines : Échantillon prototype manuel
  - 2 semaines : Tests et approbations
  - Beaucoup de gaspillage : \~15 kg déchets textile par prototype (retouches, rejects)

  Deuxième problème : pas de prototype numérique avant production en masse.

  === Solution Proposée

  Implémenter un FabLab interne avec :

  + Imprimante 3D textile (technologie directe) :
    - Imprimantes spécifiques comme celles de chez KORNIT#footnote[#link("https://www.kornit.com/fr/impression-directe-sur-vtements/")]
    - Réduction drastique des coûts (-80% matière, -15% énergie)

  + Découpe laser :
    - Coupe des motifs complexes avec un laser comme celui de FrCNCTec#footnote[#link("https://frcnctec.com/produit/machine-de-decoupe-laser-fibre-fc1530fl150/")]
    - Découpes précises et très rapides

  === Compétences Métier Requises

  - Ingénieur FAO (Fabrication Assistée Ordinateur)
  - Technicien maintenance imprimantes industrielles
  - Designer textile numérique
  - Data analyst (pour la prévision des motifs voulus par les clients)

  #pagebreak()

  == Axe 2 : Robotique Collaborative et Ergonomie

  === Problématique pour OL Factory

  Actuellement, ~60 personnes travaillent à la couture manuelle. Les problèmes majeurs sont :

  - Productivité : 150-200 maillots/couturier/jour (vs 400 potentiel)
  - Ergonomie : 35% des employés souffrent de TMS (épaule, poignet, dos)
  - Turnover : 18% de rotation annuelle (au-dessus moyenne secteur)
  - Qualité : 4-5% taux de retouches post-couture
  - Coût : En estimant un employé à 2100€ par mois en comptant les charges :

  $
    2100€ times 60 "employés" = 126,000€ "/ mois"
  $

  === Solution Proposée : Cobots (Robots Collaboratifs)

  Déployer 8-10 cobots Universal Robots (UR10e) pour :

  + Couture assistée (mode collaboratif) :
    - Bras robot fixe les tensions, guide l'aiguille
    - Opérateur positionne, appuie sur pédal pour avancer
    - Réduction TMS : +70% confort (moins d'effort de maintien)
    - Productivité : +40% (400-280 maillots/opérateur/jour)

  + Tâches répétitives :
    - Couture couture droites longues (manches, côtés)
    - Appliqué badges/logos
    - Retournement manches
    - Productivité augmented : +60% certains postes

  + Allocation intelligente :
    - Machine learning prédicteur charges → planification cobots
    - Apprentissage par démonstration (opérateur montre, robot reproduit)

  === Spécifications Techniques du UR10e @ur10e

  - Charge outile : 12.5kg
  - Portée : 1.3 mètre
  - Vitesse linéaire maximale : 1 m/s
  - Précision : #math.plus.minus 0.05 mm

  === Impact Chiffré

  #rounded-table(
    ("Métrique", "Impact", "Détail"),
    (
      ("Productivité", "+35%", "150 maillots supplémentaires par jour"),
      ("TMS réduits", "-70%", "Moins d'employés affectés"),
      ("Coût horaire", "-25%", "Automatisé: €45/h, humain: €60/h"),
      (
        "Coût collectif",
        [0% #footnote("Les salariés ne sont pas réduits mais redéployés vers contrôle qualité, maintenance, design.")],
        "Cobots amortis en 4 ans, redéploiement RH",
      ),
      ("Qualité", "+15%", "Réduction défauts de 4.5% à 1%"),
    ),
  )

  === Compétences IT Requises

  - Ingénieur robotique pour intégrer les robots à la production
  - Technicien maintenance (pour la réparation et l'entretien des cobots)

  #pagebreak()

  == Axe 3 : Traçabilité Complète et Smart Products (IoT)

  === Problématique

  - Pas de traçabilité pour la bonne réception des colis

  - Clients reçoivent maillot, aucune information post-vente
  - Pas de traçabilité matière (d'où vient le tissu?)
  - Impossible de faire rappels produits qualité
  - Pas de data client post-achat → aucune fidélité programmable

  === Solution : RFID + Smart Product Platform

  *Implémentation RFID* :

  + Étiquette RFID UHF passif intégrée en couture :
    - NXP Ucode (coût: €0.15/tag, volumes)
    - Encodage : ID_produit, ID_lot, date_fab, ID_fournisseur_tissu
    - Portée lect : 3-6 mètres

  + Lecteurs SCADA à différents points :
    - Avant expédition (scanns géométrique)
    - Logistique (palettes)
    - Magasin OL (retail)

  + IoT Cloud Platform (Azure IoT Hub) :
    - Client scanne RFID au déballage
    - Reçoit app mobile avec conseils personnalisés
    - Rapporte usure/qualité → feedback

  === KPI Attendus

  #rounded-table(
    ("Métrique", "Cible"),
    (
      ("Taux clients", "65% (première année)"),
      ("Feedback qualité capturé", "60% des clients"),
      ("Nouvelles ventes via recommandations", "+12%"),
      ("Coût additif / maillot", "€0.50 (tag + capteur)"),
    ),
  )

  === Compétences Métier

  - Architect IoT / ingénieur cloud
  - Data scientist (pour l'analyse des capteurs)
  - Spécialiste cybersécurité IoT (chiffrage RFID)

  #pagebreak()

  == Axe 5 : Big Data et Intelligence Métier

  === Problématique

  - Données production/ventes dispersées (ERP manuel, Excel)
  - Inexploitable pour prédictions

  === Solution : Data Lake

  #block-left(title: "Data Lake")[
    Un _data lake_ est un stockage centralisé utilisé dans le Big Data pour stocker de grandes quantités de données brutes dans leur format natif.
  ]

  - Utilisation possibile d'un service comme Azure Data Lake Storage Gen2

  - Données collectées :
    - Production : cycle times, défauts, matière utilisée
    - Commandes : volumes, timing, customization rate
    - Supply : fournisseur performances, délais
    - Clients : fidélité, retours qualité, avis

  === Métiers Informatique

  - Data architect (design data lake, security)
  - Data engineer (pipelines ETL, DevOps cloud)
  - Data scientist (Modèles de ML pour prévision de la demande)

  #pagebreak()

  = Intégration Systèmes et Architecture Informatique

  #block-left(title: "S/4HANA")[
    S/4HANA est l'ERP le plus populaire en entreprise. SAP a également développé une version cloud (S/4HANA Cloud) qui offre plus d'intégration avec les technologies modernes.
  ]

  === Modules Concernés

  *MM (Materials Management)* :
  - Gestion stock matière : tissu, fil, étiquettes
  - Commandes fournisseur automapprouved via ML (via Data Lake)

  *PP (Production Planning)* :
  - Nomenclatures produit (BOMs) : maillot = 3 types tissus + accessoires
  - Planification globale (MPS) basée MES temps réel

  *SD (Sales & Distribution)* :
  - Commandes clients via web interface (+ mobile app smart product)
  - Confirmations livraison basées MES (date réelle fin production)
  - Intégration IoT : suivi package via RFID jusqu'au client

  *FI (Finance)* :
  - Coûts standard produit : matière + production + logistique
  - KPI profitabilité par motif/client
  - Allocation coûts énergie (durabilité tracking)

  == IoT Platform : Azure IoT Hub

  Collecte données capteurs distribués :

  - Smart Products (maillots RFID+capteurs)
  - Cloud : Storage + Analytics
  - App Client : notification usure, conseils maintenance

  #pagebreak()

  = Aspects DDRS et Démarche Green IT

  == Contexte DDRS

  OL Factory s'engage dans une démarche volontaire DDRS (Développement Durable et Responsabilité Sociétale). Les solutions Industry 4.0 constituent un levier majeur pour :

  === D1 - Durabilité Environnementale

  *Réduction consommation ressources* :

  #rounded-table(
    ("Ressource", "Baseline", "Cible 2027", "Réduction"),
    (
      ("Eau (m³/an)", "800", "250", "*69%*"),
      ("Énergie (MWh/an)", "150", "95", "*37%*"),
      ("Déchets textiles (tonnes)", "25", "8", "*68%*"),
      ("Émissions CO2 eq (tonnes)", "350", "185", "*47%*"),
    ),
  )

  *Comment atteindre ces cibles* :

  1. *Fabrication additive* (KORNIT) :
    - Teinture numérique : 90% moins eau (8L → 0.8L/kg tissu)
    - Zéro défaut design répétés
    - Coût énergie : -15% (électricité vs gaz chaudière)

  2. *Cobots* :
    - +35% productivité → amortissement énergie machine
    - LED norme ISO 50001 (éclairage usine)

  3. *Jumeau numérique + optimisation* :
    - Plans production optimisés → moins hold-up stock
    - Maintenance prédictive → machines moins defaillantes
    - Réduction pertes processus : -8% énergie

  4. *Big Data* :
    - Prédictions demande (-20% surprod)
    - Write-offs textiles réduits

  === D2 - Responsabilité Sociétale

  *Volet Emploi* :

  - Créations de postes IT : +8
  - Redéploiement de 15 couturières vers QA, CAO et support MES
  - Aucun licenciement net grâce à la hausse de productivité
  - Budget formation : €80k/an

  *Certification ISO 14001* :

  - Objectif : certification en 2027
  - Audit initial : 2026 Q2
  - Mise en conformité : fin 2026

  *Engagement fournisseurs* :

  - Sélection basée sur une scorecard durabilité
  - Traçabilité renforcée des matières
  - Priorité aux matières recyclées ou certifiées GOTS

  *Éthique et Cybersécurité* :

  - Données client chiffrées et conformes RGPD
  - Droit à l'oubli après 2 ans
  - Audit cybersécurité annuel
  - Publication annuelle d'un rapport ESG

  === D3 - Vitalité Sociétale

  *Engagement communauté locale* :

  - Partenariat stage : 6-8 étudiants/an de Polytech Lyon
  - Open innovation workshops : FabLab ouvert collectivités Mondays
  - Données anonymisées partagées académie : benchmark Industry 4.0 PME

  === Green IT Spécifique

  *Bilan énergétique industrie 4.0* :

  #rounded-table(
    ("Phase", "Delta d'énergie"),
    (
      ("Construction soft / hardware", "+3 MWh"),
      ("Opération année 1 : MES + Cloud (24/7)", "+12 MWh"),
      ("Économies Shop Floor", "-55 MWh"),
      ("*NET année 1*", "*-40 MWh*"),
    ),
  )

  #pagebreak()

  = Métiers de l'Informatique dans ce Projet

  L'industrie 4.0 à OL Factory crée de la demande pour 12-15 nouveaux postes :

  == Informatique / Applications

  === Data Scientist / ML Engineer
  - Missions : prévision, détection d'anomalies, optimisation
  - Compétences : Python, SQL, ML
  - Salaire : €45-55k/an débutant, €60-75k confirmé

  === Data Engineer
  - Missions : pipelines ETL, Data Lake, APIs
  - Compétences : Python, PySpark, cloud, Airflow
  - Salaire : €40-50k/an junior, €55-70k senior

  === IoT Platform Architect
  - Missions : architecture IoT, sécurité RFID, intégration capteurs
  - Compétences : Cloud IoT, MQTT/CoAP, cybersécurité
  - Salaire : €55-70k/an

  === Développeur Full Stack (App Smart Product)
  - Missions : application mobile, backend API, connexion IoT
  - Compétences : React Native, Node.js, UX
  - Salaire : €35-45k junior, €50-60k confirmé

  == Opérationnel / Technique

  === Ingénieur MES / ERP
  - Missions : intégration MES et SAP
  - Compétences : SAP, SQL, process industriel
  - Salaire : €50-65k/an

  === Ingénieur Automation / SCADA
  - Missions : supervision, PLC, cobots
  - Compétences : Ladder, OPC-UA, électrotechnique
  - Salaire : €40-55k

  === Intégrateur Robotique / Cobot
  - Missions : déploiement et maintenance des cobots
  - Compétences : robotique, sécurité, mécanique
  - Salaire : €40-52k

  === Spécialiste Cybersécurité Industrielle
  - Missions : audit réseau, détection d'anomalies
  - Compétences : ICS/SCADA, ISO 27001, NIST
  - Salaire : €50-70k

  == Profils Transversaux

  === Chef de Projet Digital / Industry 4.0
  - Missions : pilotage des projets, coordination, budget
  - Compétences : management, agile, change management
  - Salaire : €50-65k junior, €70-90k senior

  === Consultant / Capability Builder
  - Missions : formation, ateliers, transfert de pratiques
  - Compétences : expertise Industry 4.0, pédagogie
  - Durée engagement : 6-18 mois

  == Bilan Compétences

  #rounded-table(
    ("Catégorie", "Profils Nécessaires", "Urgence", "Marché"),
    (
      ("Data/BI", "2", "Critique", "Tension élevée"),
      ("Cloud/IoT", "1", "Important", "Tension"),
      ("Automation", "2", "Immédiate", "Tension"),
      ("MES/ERP", "2", "Important", "Modérée"),
      ("Cybersécurité", "1", "Important", "Tension extrême"),
      ("Management", "1", "Critique", "Tension"),
      ("Support Ops", "2", "Immédiate", "Modérée"),
    ),
  )

  #pagebreak()

  = Conclusion

  OL Factory se trouve à un point stratégique. L'adoption progressive d'une architecture de l'Industrie 4.0/5.0, adressant une fabrication additive, une robotique collaborative, une traçabilité IoT, une optimisation numérique et une intelligence data, réprésente une opportunité d'innovation majeure pour l'entreprise.

  == Synthèse Bénéfices

  *Quantifiables* :
  - Délais produits : -75% (8 sem → 2 sem prototypage)
  - Productivité : +35% (150 maillots supp/jour)
  - Déchets : -68% (25 T → 8 T/an)
  - Énergie : -37% (150 MWh → 95 MWh/an)
  - ROI global : Breakeven 3.5 ans, puis €180-220k/an économies nettes

  *Qualitatifs* :
  - Employabilité RH : +8 créations de poste, zéro suppression

  - Green credentials : Certification ISO 14001, 47% réduction CO2 eq
  - Agilité métier : Réactivité 10x améliorée (rapport hebdomadaire au lieu de mensuel)
  - Avantage compétitif : Seul producteur OL conforme au norme de l'Industrie 4.1

  #bibliography("bib.yaml", title: "Bibliographie")
]
