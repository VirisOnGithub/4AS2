#import "../template/polytech.typ": *

#let r(body) = text(fill: red)[#body]

#show "=>": $=>$

#show: conf(doctitle: "Protocoles applicatifs", subject: "ARSIR", theme: rgb(100, 50, 0))[
  #titlepage(authors: ("Clément RENIERS", ""))

  = FTP (File Transfer Protocol)

  #block-left(title: "FTP")[
    Protocole de la couche application pour partager (copier/supprimer/modifier) des fichiers sur un réseau.
  ]

  - Intéropérable (= Fonctionne quelque soit le réseau, hardware, architecture)
  - Modèle Client/Serveur
  - Nécessite une authentification
  - Interactif (échange de messages)
  - #r[Non Sécurisé] => utiliser SFTP ou FTPS

  #block-full(title: "2 connexions TCP")[
    - Connexion de contrôle : port 21 => Transfert de commandes
    - Connexion de données pour transférer les données
  ]

  Deux modes :
  - Mode actif : Le serveur envoie les données sur le port envoyé par le client (problème de firewall)
  - Mode passif : Le serveur met les données à disposition sur son port 20

  = TFTP (Trivial File Transfer Protocol)

  #block-left(title: "TFTP")[
    Protocole simplifié de la couche application pour partager (lire/écrire) un fichier sur un réseau.
  ]
  - Utilisé pour la maj des équipements réseaux ou pour démarrer à partir d'une carte réseau
  - Basé du UDP
  - Limitations : ne peut pas lister, supprimer, renommer
  - Pas de gestion des droits
  - Sans chiffrement ni authentification

  #block-full(title: "Fonctionnement")[
    1. Demande du client sur le port 69
    2. Si acceptation fichier envoyé par 512 octets
    3. Accusé de réception à chaque block-full
    4. Moins de 512 octets = fin du transfert
    5. Si paquet perdu, renvoie de l'expéditeur
  ]

  = HTTP (HyperText Transfer Protocol)

  #block-left(title: "HTTP")[
    Protocole pour la consultation de ressources
  ]

  - Modèle Client/Serveur
  - Connexion TCP bidi 80
  - Stateless (sans session)
  - Support de la mise en cache, d'un proxy
  - #r[Non sécurisé] => HTTPS

  #block-full(title: "Fonctionnement")[
    1. Analyse URI
    2. Extraction nom d'ĥôte + req DNS
    3. Echange HTTP entre client/server + analyse
    4. Interprétation HTML
    5. Collecte des ressources autres (CSS, JS, PNG)
    6. Affichage de la page Web
  ]

  #block-full(title: "Entêtes")[
    - `GET` => demande la ressource
    - `HEAD` => en-tête
    - `POST` => transmet les données
    - `PUT` => transmet une ressource
    - `DELETE` => efface la ressource
  ]

  #block-left(title: "Types MIME (Multipurpose Internet Mail Extensions")[
    - `text/plain`: texte
    - `image/png`: images
    - `image/svg+xml`: svg
    - `audio/mpeg`: MP3
    - `application/pdf`: PDF
    - `text/html`, `text/css`
    - `application/json`
  ]

  #block-left(title: "Cookie")[
    Donnée envoyée par le serveur web au navigateur qui le retourne dans les requêtes HTTP suivantes.\
    Sert principalemtn à identifier les sessions.
  ]

  #block-left(title: "CGI (Common Gateway Interface)")[
    Interface pour traiter les requêtes HTTP en exécutatn un programme.
  ]

  #block-left(title: "Servlet")[
    Classe Java pour créer dynamiquement des données au sein d'un serveur HTTP.
  ]

  = SMTP (Simple Mail Transfer Protocol)

  #block-left(title: "SMTP")[
    Protocole pour le transfert de mail vers les serveurs de messagerie
  ]
  - Connexion TCP bidirectionnelle
  - Port 25 (no auth) ou 587 (auth)
  - #r[Non sécurisé] => SMTPS

  = POP (Post Office Protocol)

  #block-left(title: "POP")[
    Protocole pour la remise finale des courriels.
  ]

  - Connexion, bidirectionnelle sur le port 110
  - #r[Non sécurisé] => POP3S

  = IMAP (Internet Message Access Protocol)

  #block-left(title: "IMAP")[
    Protocole plus complexe pour la remise finale des courriels.
  ]

  - Connexion TCP bidirectionnelle
  - Port 143 (no SSL) ou 993 (avec SSL)
  - Fonctionnalités additionnelles 
    - Gestion des répertoires
    - Statut des mails
]
