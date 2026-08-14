const {
  Document,
  Packer,
  Paragraph,
  TextRun,
  Table,
  TableRow,
  TableCell,
  HeadingLevel,
  AlignmentType,
  BorderStyle,
  WidthType,
  ShadingType,
  LevelFormat,
  PageNumber,
  NumberFormat,
  TableOfContents,
  Header,
  Footer,
  VerticalAlign,
} = require("docx");
const fs = require("fs");

const BLUE = "1F4E79";
const BLUE_LIGHT = "2E75B6";
const BLUE_PALE = "D6E4F0";
const BLUE_HEADER = "BDD7EE";
const GRAY = "595959";
const BORDER = { style: BorderStyle.SINGLE, size: 1, color: "CCCCCC" };
const BORDERS = { top: BORDER, bottom: BORDER, left: BORDER, right: BORDER };

function h1(text) {
  return new Paragraph({
    heading: HeadingLevel.HEADING_1,
    spacing: { before: 400, after: 200 },
    border: { bottom: { style: BorderStyle.SINGLE, size: 8, color: BLUE_LIGHT, space: 6 } },
    children: [new TextRun({ text, bold: true, size: 36, color: BLUE, font: "Arial" })],
  });
}

function h2(text) {
  return new Paragraph({
    heading: HeadingLevel.HEADING_2,
    spacing: { before: 300, after: 160 },
    children: [new TextRun({ text, bold: true, size: 28, color: BLUE_LIGHT, font: "Arial" })],
  });
}

function h3(text) {
  return new Paragraph({
    heading: HeadingLevel.HEADING_3,
    spacing: { before: 200, after: 120 },
    children: [new TextRun({ text, bold: true, size: 24, color: GRAY, font: "Arial" })],
  });
}

function body(text, options = {}) {
  return new Paragraph({
    spacing: { before: 100, after: 100 },
    children: [new TextRun({ text, size: 22, font: "Arial", color: "333333", ...options })],
  });
}

function bullet(text) {
  return new Paragraph({
    numbering: { reference: "bullets", level: 0 },
    spacing: { before: 60, after: 60 },
    children: [new TextRun({ text, size: 22, font: "Arial", color: "333333" })],
  });
}

function numberedItem(text) {
  return new Paragraph({
    numbering: { reference: "numbers", level: 0 },
    spacing: { before: 60, after: 60 },
    children: [new TextRun({ text, size: 22, font: "Arial", color: "333333" })],
  });
}

function spacer(lines = 1) {
  return new Paragraph({ spacing: { before: lines * 80, after: 0 }, children: [new TextRun("")] });
}

function infoBand(label, value) {
  return new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [3500, 5860],
    rows: [
      new TableRow({
        children: [
          new TableCell({
            borders: BORDERS,
            width: { size: 3500, type: WidthType.DXA },
            shading: { fill: BLUE_HEADER, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: label, bold: true, size: 22, font: "Arial", color: "1F4E79" })],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 5860, type: WidthType.DXA },
            shading: { fill: "FFFFFF", type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({ children: [new TextRun({ text: value, size: 22, font: "Arial", color: "333333" })] }),
            ],
          }),
        ],
      }),
    ],
  });
}

function salaryRow(niveau, intitule, min, mid, max, isHeader = false) {
  const fill = isHeader ? BLUE : niveau % 2 === 0 ? BLUE_PALE : "FFFFFF";
  const color = isHeader ? "FFFFFF" : "333333";
  const bold = isHeader;
  function cell(text, w) {
    return new TableCell({
      borders: BORDERS,
      width: { size: w, type: WidthType.DXA },
      shading: { fill, type: ShadingType.CLEAR },
      margins: { top: 80, bottom: 80, left: 120, right: 120 },
      verticalAlign: VerticalAlign.CENTER,
      children: [
        new Paragraph({
          alignment: AlignmentType.CENTER,
          children: [new TextRun({ text, bold, size: 20, font: "Arial", color })],
        }),
      ],
    });
  }
  const cols = [1560, 3000, 1600, 1600, 1600];
  const vals = [String(isHeader ? niveau : `N${niveau}`), intitule, min, mid, max];
  return new TableRow({ children: vals.map((v, i) => cell(v, cols[i])) });
}

// ─── COVER PAGE ───────────────────────────────────────────────────────────────
const coverPage = [
  spacer(4),
  new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { before: 0, after: 60 },
    children: [new TextRun({ text: "CHARTE D'ENTREPRISE", bold: true, size: 72, color: BLUE, font: "Arial" })],
  }),
  new Paragraph({
    alignment: AlignmentType.CENTER,
    border: { bottom: { style: BorderStyle.SINGLE, size: 12, color: BLUE_LIGHT, space: 4 } },
    spacing: { before: 0, after: 200 },
    children: [
      new TextRun({ text: "CISS", size: 40, color: BLUE_LIGHT, font: "Arial", italics: true }),
    ],
  }),
  spacer(2),
  new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { before: 0, after: 60 },
    children: [
      new TextRun({
        text: "Document fondateur — Valeurs, Règles & Engagements",
        size: 28,
        color: GRAY,
        font: "Arial",
        italics: true,
      }),
    ],
  }),
  spacer(4),
  infoBand("Version", "1.0 — Ébauche"),
  spacer(1),
  infoBand("Date d'entrée en vigueur", "21 / 04 / 2026"),
  spacer(1),
  infoBand("Approuvée par", "Le président-directeur général"),
  spacer(1),
  infoBand("Périmètre d'application", "Tous les salariés, stagiaires et prestataires"),
  spacer(1),
  infoBand("Révision prévue", "Annuelle ou en cas de changement réglementaire"),
  spacer(3),
  new Paragraph({
    alignment: AlignmentType.CENTER,
    children: [
      new TextRun({
        text: "Ce document est confidentiel et à usage interne exclusivement.",
        size: 18,
        color: GRAY,
        font: "Arial",
        italics: true,
      }),
    ],
  }),
];

// ─── SECTION 1 — PREAMBULE ────────────────────────────────────────────────────
const section1 = [
  h1("PRÉAMBULE"),
  body(
    "La présente Charte d'Entreprise constitue le socle commun des valeurs, droits et obligations qui régissent les relations au sein de [NOM DE L'ENTREPRISE]. Elle s'applique sans exception à l'ensemble des collaborateurs, quel que soit leur statut (CDI, CDD, alternant, stagiaire, prestataire).",
  ),
  spacer(),
  body(
    "Elle complète le Règlement Intérieur, les accords collectifs en vigueur et le Code du travail, sans s'y substituer. En cas de conflit, les dispositions légales et réglementaires prévalent.",
  ),
  spacer(),
  body(
    "Cette charte est un document vivant. Elle sera révisée au minimum une fois par an, en associant les représentants du personnel.",
  ),
];

// ─── SECTION 2 — DISCRIMINATION ──────────────────────────────────────────────
const section2 = [
  h1("CHAPITRE 1 — PRÉVENTION DES DISCRIMINATIONS ET PROMOTION DE L'ÉGALITÉ"),

  h2("1.1 Principe fondamental de non-discrimination"),
  body(
    "Conformément aux articles L.1132-1 et suivants du Code du travail, aucune décision relative au recrutement, à la rémunération, à la promotion, à la formation, aux mutations ou à la rupture du contrat de travail ne peut être fondée sur l'un des critères suivants :",
  ),
  bullet("Origine, nationalité, appartenance ou non-appartenance à une ethnie, une nation ou une prétendue race"),
  bullet("Sexe, genre, identité de genre, orientation sexuelle"),
  bullet("Âge, situation de famille, grossesse, état de santé, handicap"),
  bullet("Apparence physique, nom de famille, lieu de résidence"),
  bullet("Convictions politiques, religieuses, syndicales ou philosophiques"),
  bullet("Activités syndicales ou mutualistes"),
  spacer(),
  body(
    "Tout acte discriminatoire est passible de sanctions disciplinaires pouvant aller jusqu'au licenciement pour faute grave, sans préjudice des poursuites pénales.",
  ),

  h2("1.2 Harcèlement moral et sexuel"),
  body("L'entreprise condamne avec la plus grande fermeté toute forme de harcèlement :"),
  bullet(
    "Harcèlement moral (art. L.1152-1 du Code du travail) : comportements répétés ayant pour objet ou pour effet une dégradation des conditions de travail.",
  ),
  bullet(
    "Harcèlement sexuel (art. L.1153-1) : propos ou comportements à connotation sexuelle non désirés, répétés ou graves.",
  ),
  spacer(),
  body("Les victimes et témoins peuvent signaler les faits sans crainte de représailles, par les voies suivantes :"),
  bullet("Auprès du/de la responsable RH ou d'un référent harcèlement désigné"),
  bullet("Via le dispositif d'alerte anonyme : [alerteharcelement@ciss.fr]"),
  bullet("Auprès des représentants du personnel (CSE)"),

  h2("1.3 Égalité professionnelle Femmes / Hommes"),
  body("L'entreprise s'engage à garantir l'égalité de traitement entre les femmes et les hommes en matière de :"),
  bullet("Rémunération à poste et compétences équivalents"),
  bullet("Accès à la formation et à la promotion"),
  bullet("Équilibre vie professionnelle / vie personnelle"),
  spacer(),
  body(
    "L'Index d'égalité professionnelle est publié annuellement et mis à disposition de tous les salariés. Tout écart injustifié identifié donne lieu à un plan d'action correctif dans les 12 mois.",
  ),

  h2("1.4 Inclusion du handicap"),
  body(
    "L'entreprise s'engage à atteindre et maintenir le taux légal d'emploi de travailleurs handicapés (6 %). Des aménagements de poste raisonnables sont mis en œuvre sur demande, après évaluation par la médecine du travail.",
  ),

  h2("1.5 Procédure de traitement des signalements"),
  numberedItem("Dépôt du signalement (écrit, oral ou anonyme) auprès du référent RH"),
  numberedItem("Accusé de réception sous 5 jours ouvrés"),
  numberedItem("Enquête interne contradictoire conduite sous 30 jours calendaires"),
  numberedItem("Décision et notification aux parties — mesures conservatoires si nécessaire"),
  numberedItem("Suivi à 3 mois pour vérifier la cessation des comportements signalés"),
  spacer(),
  body(
    "La confidentialité des parties est garantie tout au long de la procédure. Toute fausse déclaration délibérée engage la responsabilité de son auteur.",
  ),
];

// ─── SECTION 3 — TÉLÉTRAVAIL ──────────────────────────────────────────────────
const section3 = [
  h1("CHAPITRE 2 — RÈGLEMENT DU TÉLÉTRAVAIL"),

  h2("2.1 Définition et champ d'application"),
  body(
    "Le télétravail désigne toute forme de travail organisé hors des locaux de l'entreprise, utilisant les technologies de l'information et de la communication. Il peut être régulier (jours fixes chaque semaine) ou occasionnel (ponctuel, sur demande).",
  ),
  spacer(),
  body("Sont éligibles au télétravail les salariés remplissant simultanément les conditions suivantes :"),
  bullet("Titulaires d'un CDI ou CDD de plus de 6 mois, ayant dépassé la période d'essai"),
  bullet("Exercer des fonctions compatibles avec le travail à distance (appréciation par le responsable hiérarchique)"),
  bullet("Disposer d'un environnement de travail adapté à domicile (espace calme, connexion internet stable)"),

  h2("2.2 Volume et organisation"),
  new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [3500, 5860],
    rows: [
      new TableRow({
        children: [
          new TableCell({
            borders: BORDERS,
            width: { size: 3500, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: "Paramètre", bold: true, size: 20, font: "Arial", color: "FFFFFF" })],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 5860, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [
                  new TextRun({ text: "Règle applicable", bold: true, size: 20, font: "Arial", color: "FFFFFF" }),
                ],
              }),
            ],
          }),
        ],
      }),
      ...[
        ["Nombre de jours maximum", "2 jours par semaine (sauf accord exceptionnel)"],
        ["Jours obligatoires en présentiel", "Au minimum 3 jours/semaine, dont le lundi ou le vendredi"],
        ["Plages horaires de disponibilité", "9h00 – 12h00 et 14h00 – 17h00 (heure locale France)"],
        ["Demande de télétravail", "Formulaire écrit au responsable, au plus tard le vendredi J-7"],
        ["Délai de réponse du manager", "2 jours ouvrés maximum"],
        ["Révision possible", "Par l'entreprise avec 1 mois de préavis ; par le salarié avec 2 semaines"],
      ].map(
        ([k, v], i) =>
          new TableRow({
            children: [
              new TableCell({
                borders: BORDERS,
                width: { size: 3500, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({
                    children: [new TextRun({ text: k, bold: true, size: 20, font: "Arial", color: "333333" })],
                  }),
                ],
              }),
              new TableCell({
                borders: BORDERS,
                width: { size: 5860, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({ children: [new TextRun({ text: v, size: 20, font: "Arial", color: "333333" })] }),
                ],
              }),
            ],
          }),
      ),
    ],
  }),
  spacer(),

  h2("2.3 Obligations du salarié en télétravail"),
  bullet("Être joignable et réactif pendant les plages de disponibilité définies"),
  bullet("Participer à toutes les réunions d'équipe, en visioconférence si nécessaire"),
  bullet("Garantir la confidentialité des données et utiliser exclusivement les outils fournis par l'entreprise"),
  bullet("Signaler toute difficulté technique sans délai au support informatique"),
  bullet("Ne pas exercer d'activité professionnelle tierce pendant les heures de travail"),

  h2("2.4 Obligations de l'employeur"),
  bullet("Fournir et maintenir en bon état le matériel informatique nécessaire"),
  bullet("Verser une indemnité forfaitaire de télétravail de [X] € par jour télétravaillé, conformément à l'URSSAF"),
  bullet("Garantir le respect du droit à la déconnexion en dehors des horaires de travail"),
  bullet("Assurer la couverture AT/MP pendant les heures de travail à domicile"),
  bullet("Offrir les mêmes opportunités de formation et d'évolution aux télétravailleurs"),

  h2("2.5 Suspension et retrait"),
  body(
    "L'entreprise peut suspendre ou mettre fin au télétravail dans les cas suivants : nécessité de service, baisse de performance constatée, non-respect de la présente charte, ou changement de poste. Le retour en présentiel total sera notifié par écrit avec un préavis d'un mois.",
  ),
];

// ─── SECTION 4 — GRILLES DE SALAIRE ──────────────────────────────────────────
const section4 = [
  h1("CHAPITRE 3 — GRILLES DE SALAIRE ET POLITIQUE DE RÉMUNÉRATION"),

  h2("3.1 Principes directeurs"),
  body("La politique de rémunération de l'entreprise repose sur quatre piliers :"),
  bullet("Équité interne : à compétences et responsabilités équivalentes, salaires identiques, sans discrimination"),
  bullet("Compétitivité externe : alignement sur les médianes de marché de notre secteur (benchmark annuel)"),
  bullet("Transparence : communication des fourchettes salariales à tous les candidats et salariés"),
  bullet("Évolutivité : revalorisations basées sur des critères objectifs et partagés"),

  h2("3.2 Grille de rémunération par niveau"),
  body(
    "Les niveaux ci-dessous correspondent à des familles de postes et non à des titres spécifiques. Chaque salarié est positionné dans une fourchette selon son expérience, ses compétences et sa maîtrise du poste.",
  ),
  spacer(),
  new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [1560, 3000, 1600, 1600, 1600],
    rows: [
      salaryRow("Niveau", "Famille de postes", "Minimum (€/an)", "Médiane (€/an)", "Maximum (€/an)", true),
      salaryRow(1, "Employé / Assistant", "24 000 €", "27 000 €", "30 000 €"),
      salaryRow(2, "Technicien / Chargé junior", "29 000 €", "33 000 €", "37 000 €"),
      salaryRow(3, "Chargé confirmé / Expert junior", "36 000 €", "42 000 €", "48 000 €"),
      salaryRow(4, "Expert / Manager opérationnel", "47 000 €", "55 000 €", "63 000 €"),
      salaryRow(5, "Manager senior / Responsable", "60 000 €", "72 000 €", "84 000 €"),
      salaryRow(6, "Directeur / Lead expert", "80 000 €", "100 000 €", "120 000 €"),
    ],
  }),
  spacer(),
  body(
    "* Montants bruts annuels, hors primes et avantages. Révisés chaque année au 1er janvier. Les fourchettes incluent l'inflation anticipée.",
    { italics: true, color: GRAY },
  ),

  h2("3.3 Composantes de la rémunération globale"),
  new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [3000, 6360],
    rows: [
      new TableRow({
        children: [
          new TableCell({
            borders: BORDERS,
            width: { size: 3000, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: "Composante", bold: true, size: 20, font: "Arial", color: "FFFFFF" })],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 6360, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: "Description", bold: true, size: 20, font: "Arial", color: "FFFFFF" })],
              }),
            ],
          }),
        ],
      }),
      ...[
        ["Salaire fixe brut", "Défini à l'embauche selon la grille ; révisable annuellement"],
        [
          "Prime de performance individuelle",
          "Jusqu'à [X]% du salaire fixe, versée en [mois], selon objectifs définis en début d'année",
        ],
        ["Intéressement / Participation", "Selon accord d'entreprise en vigueur"],
        ["Tickets restaurant", "10 € par jour travaillé, prise en charge à 60% par l'employeur"],
        ["Mutuelle santé", "Prise en charge à [X]% par l'employeur"],
        ["Plan d'épargne entreprise (PEE)", "Abondement de 75% jusqu'à 1000 € par an"],
        ["Indemnité télétravail", "10 € par jour, exonérée de charges"],
      ].map(
        ([k, v], i) =>
          new TableRow({
            children: [
              new TableCell({
                borders: BORDERS,
                width: { size: 3000, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({
                    children: [new TextRun({ text: k, bold: true, size: 20, font: "Arial", color: "333333" })],
                  }),
                ],
              }),
              new TableCell({
                borders: BORDERS,
                width: { size: 6360, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({ children: [new TextRun({ text: v, size: 20, font: "Arial", color: "333333" })] }),
                ],
              }),
            ],
          }),
      ),
    ],
  }),

  h2("3.4 Revalorisations salariales"),
  body("Les révisions de salaire ont lieu une fois par an, au 1er janvier. Elles tiennent compte de :"),
  bullet("L'inflation (indice INSEE des prix à la consommation de l'année N-1)"),
  bullet("La performance individuelle évaluée lors de l'entretien annuel"),
  bullet("L'évolution du marché (benchmark sectoriel annuel)"),
  bullet("La position du salarié dans sa fourchette de niveau"),
  spacer(),
  body(
    "Aucune revalorisation ne peut être accordée de manière discrétionnaire sans validation RH. Tout salarié a le droit de demander un entretien RH pour comprendre sa position dans la grille.",
  ),
];

// ─── SECTION 5 — QUALITÉ DE VIE AU TRAVAIL ───────────────────────────────────
const section5 = [
  h1("CHAPITRE 4 — QUALITÉ DES ÉCHANGES ET BIEN-ÊTRE AU TRAVAIL"),

  h2("4.1 Charte de communication interne"),
  body(
    "L'entreprise s'engage à favoriser des échanges professionnels respectueux, directs et constructifs. Les principes suivants s'appliquent à toutes les communications internes, y compris les canaux numériques :",
  ),
  bullet("Respect et courtoisie : tout échange, même contradictoire, se fait dans le respect de l'interlocuteur"),
  bullet("Clarté : les messages sont formulés de manière concise, avec un objet précis et une demande explicite"),
  bullet("Réactivité : les messages urgents sont traités dans les 4 heures en heure ouvrée ; les autres sous 24 h"),
  bullet("Droit à la déconnexion : aucune réponse n'est exigée en dehors des horaires de travail définis"),
  bullet("Pas de copie excessive : les e-mails en CC se limitent aux personnes réellement concernées"),

  h2("4.2 Entretiens réguliers et feedback"),
  body("L'entreprise garantit à chaque salarié les moments d'échange suivants :"),
  new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [2500, 2860, 4000],
    rows: [
      new TableRow({
        children: [
          new TableCell({
            borders: BORDERS,
            width: { size: 2500, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [
                  new TextRun({ text: "Type d'entretien", bold: true, size: 20, font: "Arial", color: "FFFFFF" }),
                ],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 2860, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: "Fréquence", bold: true, size: 20, font: "Arial", color: "FFFFFF" })],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 4000, type: WidthType.DXA },
            shading: { fill: BLUE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [
                  new TextRun({ text: "Contenu principal", bold: true, size: 20, font: "Arial", color: "FFFFFF" }),
                ],
              }),
            ],
          }),
        ],
      }),
      ...[
        ["One-to-one manager", "Hebdomadaire ou bimensuel", "Suivi opérationnel, blocages, priorités"],
        ["Entretien de feedback", "Trimestriel", "Performance, objectifs, axes de développement"],
        [
          "Entretien annuel d'évaluation",
          "Annuel (décembre)",
          "Bilan annuel, fixation des objectifs N+1, révision salariale",
        ],
        ["Entretien professionnel", "Tous les 2 ans (légal)", "Évolution de carrière, formation, projet professionnel"],
        [
          "Entretien de retour d'absence",
          "Après toute absence > 3 jours",
          "Reprise en douceur, éventuels aménagements",
        ],
      ].map(
        ([a, b, c], i) =>
          new TableRow({
            children: [
              new TableCell({
                borders: BORDERS,
                width: { size: 2500, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({
                    children: [new TextRun({ text: a, bold: true, size: 20, font: "Arial", color: "333333" })],
                  }),
                ],
              }),
              new TableCell({
                borders: BORDERS,
                width: { size: 2860, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({ children: [new TextRun({ text: b, size: 20, font: "Arial", color: "333333" })] }),
                ],
              }),
              new TableCell({
                borders: BORDERS,
                width: { size: 4000, type: WidthType.DXA },
                shading: { fill: i % 2 === 0 ? BLUE_PALE : "FFFFFF", type: ShadingType.CLEAR },
                margins: { top: 80, bottom: 80, left: 120, right: 120 },
                children: [
                  new Paragraph({ children: [new TextRun({ text: c, size: 20, font: "Arial", color: "333333" })] }),
                ],
              }),
            ],
          }),
      ),
    ],
  }),
  spacer(),
  body(
    "Les entretiens annuels sont obligatoires et font l'objet d'un compte-rendu écrit, co-signé par le manager et le salarié. Le salarié peut y joindre ses propres observations.",
  ),

  h2("4.3 Dispositifs d'écoute et d'alerte"),
  bullet("Boîte à idées anonyme : accessible via [lien intranet] — remontées traitées en réunion mensuelle RH"),
  bullet("Enquête de satisfaction annuelle : anonyme, résultats publiés et plan d'action présenté au CSE"),
  bullet("Référent bien-être au travail : [Prénom NOM — email@domaine.com]"),
  bullet("Programme d'aide aux employés (PAE) : assistance psychologique confidentielle 24h/24 au [numéro]"),

  h2("4.4 Gestion des conflits interpersonnels"),
  body("En cas de désaccord entre collègues ou avec la hiérarchie, la procédure suivante est encouragée :"),
  numberedItem("Dialogue direct entre les parties concernées (étape prioritaire)"),
  numberedItem("Médiation informelle avec le manager N+1, si le dialogue direct échoue"),
  numberedItem("Saisine des RH pour médiation formelle (dans les 15 jours suivant l'échec du niveau 2)"),
  numberedItem("Recours possible au CSE ou à l'inspection du travail si aucune solution n'est trouvée"),
  spacer(),
  body(
    "L'entreprise s'engage à traiter chaque signalement avec sérieux et impartialité. Aucune rétorsion ne sera tolérée à l'encontre d'un salarié ayant exprimé un désaccord par les voies appropriées.",
  ),

  h2("4.5 Formation des managers"),
  body("Tout manager encadrant une équipe bénéficie d'une formation obligatoire portant notamment sur :"),
  bullet("Les techniques de feedback positif et correctif (modèle COIN, DESC, etc.)"),
  bullet("La prévention des risques psychosociaux (RPS) et détection des signaux faibles"),
  bullet("La conduite d'entretiens professionnels et d'évaluation"),
  bullet("La communication non violente (CNV) et la gestion des conflits"),
  spacer(),
  body(
    "Cette formation est renouvelée tous les 3 ans et complétée par des modules en ligne accessibles à tout moment sur le LMS interne.",
  ),
];

// ─── SECTION 6 — ENTRÉE EN VIGUEUR & SIGNATURES ──────────────────────────────
const section6 = [
  h1("DISPOSITIONS FINALES"),

  h2("Entrée en vigueur"),
  body(
    "La présente Charte entre en vigueur le [JJ/MM/AAAA], après information et consultation du CSE. Elle est accessible à tous les salariés via l'intranet et remise à chaque nouvel embauché lors de son intégration.",
  ),

  h2("Révision"),
  body(
    "La Charte est révisée au minimum une fois par an, ou à chaque évolution législative majeure. Toute modification est soumise au CSE avant application et communiquée aux salariés avec un préavis de 30 jours.",
  ),

  h2("Signatures"),
  spacer(2),
  new Table({
    width: { size: 9360, type: WidthType.DXA },
    columnWidths: [4680, 4680],
    rows: [
      new TableRow({
        children: [
          new TableCell({
            borders: BORDERS,
            width: { size: 4680, type: WidthType.DXA },
            shading: { fill: BLUE_PALE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [
                  new TextRun({ text: "Pour la Direction", bold: true, size: 22, font: "Arial", color: BLUE }),
                ],
              }),
              spacer(2),
              new Paragraph({
                children: [
                  new TextRun({
                    text: "Nom & Titre : ___________________________",
                    size: 20,
                    font: "Arial",
                    color: GRAY,
                  }),
                ],
              }),
              spacer(),
              new Paragraph({
                children: [
                  new TextRun({ text: "Date : ___________________________", size: 20, font: "Arial", color: GRAY }),
                ],
              }),
              spacer(),
              new Paragraph({
                children: [
                  new TextRun({
                    text: "Signature : ___________________________",
                    size: 20,
                    font: "Arial",
                    color: GRAY,
                  }),
                ],
              }),
            ],
          }),
          new TableCell({
            borders: BORDERS,
            width: { size: 4680, type: WidthType.DXA },
            shading: { fill: BLUE_PALE, type: ShadingType.CLEAR },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: "Pour le CSE", bold: true, size: 22, font: "Arial", color: BLUE })],
              }),
              spacer(2),
              new Paragraph({
                children: [
                  new TextRun({
                    text: "Nom & Titre : ___________________________",
                    size: 20,
                    font: "Arial",
                    color: GRAY,
                  }),
                ],
              }),
              spacer(),
              new Paragraph({
                children: [
                  new TextRun({ text: "Date : ___________________________", size: 20, font: "Arial", color: GRAY }),
                ],
              }),
              spacer(),
              new Paragraph({
                children: [
                  new TextRun({
                    text: "Signature : ___________________________",
                    size: 20,
                    font: "Arial",
                    color: GRAY,
                  }),
                ],
              }),
            ],
          }),
        ],
      }),
    ],
  }),
];

// ─── DOCUMENT ─────────────────────────────────────────────────────────────────
const doc = new Document({
  numbering: {
    config: [
      {
        reference: "bullets",
        levels: [
          {
            level: 0,
            format: LevelFormat.BULLET,
            text: "•",
            alignment: AlignmentType.LEFT,
            style: { paragraph: { indent: { left: 720, hanging: 360 } } },
          },
        ],
      },
      {
        reference: "numbers",
        levels: [
          {
            level: 0,
            format: LevelFormat.DECIMAL,
            text: "%1.",
            alignment: AlignmentType.LEFT,
            style: { paragraph: { indent: { left: 720, hanging: 360 } } },
          },
        ],
      },
    ],
  },
  styles: {
    default: { document: { run: { font: "Arial", size: 22 } } },
    paragraphStyles: [
      {
        id: "Heading1",
        name: "Heading 1",
        basedOn: "Normal",
        next: "Normal",
        quickFormat: true,
        run: { size: 36, bold: true, font: "Arial", color: BLUE },
        paragraph: { spacing: { before: 400, after: 200 }, outlineLevel: 0 },
      },
      {
        id: "Heading2",
        name: "Heading 2",
        basedOn: "Normal",
        next: "Normal",
        quickFormat: true,
        run: { size: 28, bold: true, font: "Arial", color: BLUE_LIGHT },
        paragraph: { spacing: { before: 300, after: 160 }, outlineLevel: 1 },
      },
      {
        id: "Heading3",
        name: "Heading 3",
        basedOn: "Normal",
        next: "Normal",
        quickFormat: true,
        run: { size: 24, bold: true, font: "Arial", color: GRAY },
        paragraph: { spacing: { before: 200, after: 120 }, outlineLevel: 2 },
      },
    ],
  },
  sections: [
    // Cover page
    {
      properties: {
        page: { size: { width: 11906, height: 16838 }, margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 } },
      },
      children: coverPage,
    },
    // Main content
    {
      properties: {
        page: { size: { width: 11906, height: 16838 }, margin: { top: 1200, right: 1200, bottom: 1200, left: 1440 } },
      },
      headers: {
        default: new Header({
          children: [
            new Paragraph({
              border: { bottom: { style: BorderStyle.SINGLE, size: 6, color: BLUE_LIGHT, space: 4 } },
              spacing: { before: 0, after: 100 },
              children: [
                new TextRun({
                  text: "CHARTE D'ENTREPRISE — [NOM DE L'ENTREPRISE]",
                  size: 18,
                  font: "Arial",
                  color: GRAY,
                }),
                new TextRun({ text: "   |   Version 1.0", size: 18, font: "Arial", color: BLUE_LIGHT }),
              ],
            }),
          ],
        }),
      },
      footers: {
        default: new Footer({
          children: [
            new Paragraph({
              border: { top: { style: BorderStyle.SINGLE, size: 6, color: BLUE_LIGHT, space: 4 } },
              alignment: AlignmentType.CENTER,
              spacing: { before: 100, after: 0 },
              children: [
                new TextRun({ text: "Page ", size: 18, font: "Arial", color: GRAY }),
                new TextRun({ children: [PageNumber.CURRENT], size: 18, font: "Arial", color: GRAY }),
                new TextRun({ text: " — Document confidentiel à usage interne", size: 18, font: "Arial", color: GRAY }),
              ],
            }),
          ],
        }),
      },
      children: [
        ...section1,
        spacer(),
        ...section2,
        spacer(),
        ...section3,
        spacer(),
        ...section4,
        spacer(),
        ...section5,
        spacer(),
        ...section6,
      ],
    },
  ],
});

Packer.toBuffer(doc).then((buffer) => {
  fs.writeFileSync("charte.docx", buffer);
  console.log("Done");
});
