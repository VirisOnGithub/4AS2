// Imports
#import "@preview/brilliant-cv:3.3.0": cv-entry, cv-section, h-bar


#cv-section("Education")

#cv-entry(
  title: [Engineering Degree in Computer Science],
  society: [Polytech Lyon],
  date: [2022 - 2027],
  location: [Lyon, France],
  logo: image("../assets/logos/polytech.png"),
  description: list(
    [
      // Cours sur le développement web, la programmation orientée objet, les réseaux informatiques, la science des données.
      Learned web development, object-oriented programming, computer networks, data science.
    ],
    [
      // Projets d'optimisation discrète, de compilation des langages bas niveau, de développement web full-stack.
      Worked on projects involving discrete optimization, low-level language compilation, and full-stack web development.
    ],
  ),
  tags: ("Python", "Java", "SQL", "Vue.js"),
)

#cv-entry(
  title: [
    // Baccalauréat Scientifique
    Scientific Baccalaureate
  ],
  society: [Lycée Blaise Pascal],
  date: [2019 - 2022],
  location: [Lyon, France],
  logo: image("../assets/logos/lycee.png"),
  description: list(
    [
      // Spécialités Mathématiques et Physique-Chimie
      Specialized in Mathematics and Physics-Chemistry],
    [
      // Mention Très Bien Section Européenne Anglais
      With highest honours, European Section, English],
  ),
)
