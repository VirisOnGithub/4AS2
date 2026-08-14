// Imports
#import "@preview/brilliant-cv:3.3.0": cv-entry, cv-entry-continued, cv-entry-start, cv-section


#cv-section("Expérience Professionnelle")

#cv-entry-start(
  society: [Xerfi],
  logo: image("../assets/logos/xerfi.jpg"),
  location: [Lyon, France],
)

#cv-entry-continued(
  title: [Directeur de la Science des Données],
  date: [2020 - Présent],
  description: list(
    [
      Développement de plusieurs scrapers d'articles de presse avec Python.
    ],
    [
      Création d'une extension Chrome pour remplir automatiquement des formulaires d'assurances.
    ],
  ),
  tags: ("Python", "Selenium", "Javascript"),
)

#cv-entry(
  title: [Ouvrier Polyvalent],
  society: [Lofoten Bois],
  date: [2017 - 2020],
  location: [Thurins, France],
  logo: image("../assets/logos/lofoten.png"),
  description: list(
    [Construction et emballages de colonnes en bois pour le commerce de petits objets],
  ),
)

// #cv-entry(
//   title: [Stagiaire en Analyse de Données],
//   society: [PQR Corporation],
//   date: list(
//     [été 2017],
//     [été 2016],
//   ),
//   location: [Chicago, IL],
//   logo: image("../assets/logos/pqr_corp.png"),
//   description: list(
//     [Aider à la préparation, au traitement et à l'analyse de données à l'aide de Python et Excel, participer aux réunions d'équipe et contribuer à la planification et à l'exécution de projets],
//   ),
// )
