// Imports
#import "@preview/brilliant-cv:3.3.0": cv-entry, cv-section, h-bar


#cv-section("Formation")

#cv-entry(
  title: [Diplôme d'ingénieur en Informatique],
  society: [Polytech Lyon],
  date: [2022 - 2027],
  location: [Lyon, France],
  logo: image("../assets/logos/polytech.png"),
  description: list(
    [
      Cours sur le développement web, la programmation orientée objet, les réseaux informatiques, la science des données.
    ],
    [
      Projets d'optimisation discrète, de compilation des langages bas niveau, de développement web full-stack.
    ],
  ),
  tags: ("Python", "Java", "SQL", "Vue.js"),
)

#cv-entry(
  title: [Baccalauréat Scientifique],
  society: [Lycée Blaise Pascal],
  date: [2019 - 2022],
  location: [Lyon, France],
  logo: image("../assets/logos/lycee.png"),
  description: list(
    [Spécialités Mathématiques et Physique-Chimie],
    [Mention Très Bien Section Européenne Anglais],
  ),
)
