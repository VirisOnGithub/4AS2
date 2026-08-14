// Imports
#import "@preview/brilliant-cv:3.3.0": cv-entry, cv-entry-continued, cv-entry-start, cv-section


#cv-section("Work Experience")

#cv-entry-start(
  society: [Xerfi],
  logo: image("../assets/logos/xerfi.jpg"),
  location: [Lyon, France],
)

#cv-entry-continued(
  title: [Software Engineer Intern],
  date: [sept. 2025 - feb. 2026],
  description: list(
    [
      // Développement de plusieurs scrapers d'articles de presse avec Python.
      Developed multiple web scrapers for news articles using Python.
    ],
    [
      // Création d'une extension Chrome pour remplir automatiquement des formulaires d'assurances.
      Created a Chrome extension to automatically fill out insurance forms.
    ],
  ),
  tags: ("Python", "Selenium", "Javascript"),
)

#cv-entry(
  title: [Polyvalent Worker],
  society: [Lofoten Bois],
  date: [jun. 2023],
  location: [Thurins, France],
  logo: image("../assets/logos/lofoten.png"),
  description: list(
    [
      // Construction et emballages de colonnes en bois pour le commerce de petits objets
      Construction and packaging of wooden columns for small object commerce],
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
