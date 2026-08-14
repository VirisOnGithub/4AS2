#import "@local/polytech:1.0.0": *
#import "@preview/mmdr:0.2.1": mermaid

#show raw.where(lang: "mermaid"): it => mermaid(layout: (node_spacing: 50), it.text)

#show: conf(doctitle: "TD2", subject: "ISI3", theme: blue)[
  #titlepage(authors: "Clément RENIERS")

  = Système de vente de véhicules

  ```mermaid
  classDiagram

  class Vehicule {
      - cout: float
  }

  class Societe {
      <<interface>>
      + calculerCout() float
  }

  class SocieteSimple

  class SocieteAvecFiliale {
      - part: float
  }

  SocieteSimple ..|> Societe
  SocieteAvecFiliale ..|> Societe
  SocieteAvecFiliale --o Societe: filiale
  Societe --* Vehicule: parc
  ```

  #rounded-image(
    image("exo1.svg"),
    size: 50%,
  )

  == Correction prof

  ```mermaid
  classDiagram

  class Societe {
    + ajouterVehicule(v: Vehicule)
    + getNbVehicules() int
    + coutEntretien() float
  }

  class SocieteAvecFiliale {
    - pourcentage: float
    + coutEntretien() float
    + ajouterFiliale(s: Societe)
  }

  class Vehicule {
    - coutEntretien: float
  }

  SocieteAvecFiliale ..|> Societe
  SocieteAvecFiliale "1..*"--o Societe: filiales
  Vehicule " * "--o Societe: parc
  ```

  = Pilote automatique

  // ```mermaid
  //   classDiagram

  // class PiloteAuto{
  //     - etatNormal: Etat
  //     - etatPluie: Etat
  //     - etatNeige: Etat
  //     + traiter(obstacle)
  //     + tourner(angle)
  //     - setEtat(e: Etat)
  // }

  // class Etat {
  //     <<interface>>
  //     + traiter(obstacle)
  //     + tourner(angle)
  // }

  // class Observer {
  //     <<interface>>
  //     + miseajour()
  // }

  // class Sujet {
  //     <<abstract>>
  //     + ajouter(o: Observer)
  //     + supprimer(o: Observer)
  //     + notifier()
  // }

  // class ConditionMeteo {
  //     + getPluie() boolean
  //     + getNeige() boolean
  //     + updateSensors()
  // }

  // class EtatNeige
  // class EtatPluie

  // Sujet --> Observer
  // PiloteAuto ..> Observer
  // ConditionMeteo--|> Sujet
  // PiloteAuto--> Sujet
  // PiloteAuto -- Etat
  // EtatNeige ..> Etat
  // EtatPluie ..> Etat
  // ```

  #rounded-image(
    image("exo2.svg"),
  )

  ```java
  class PiloteAuto {
    @Override
    public void miseajour() {
      if(sujet.getPluie()) {
        setEtat(PiloteAuto.etatPluie);
      } else if(sujet.getNeige()) {
        setEtat(PiloteAuto.etatNeige);
      } else {
        setEtat(PiloteAuto.etatNormal);
      }
    }
  }
  ```
]
