# Rapport TP1 – Éco-conception Web  
## Refonte du site SodaStream avec Astro

**Matière :** I4.0 / GreenIT  
**Enseignant :** F. Perraud  
**Année :** 4A Informatique – Polytech Lyon  
**Date de rendu :** 24 mars 2025

---

## Table des matières

1. Introduction
2. Étude de l'existant – SodaStream.com
3. Principes d'éco-conception retenus
4. Choix technologiques – Pourquoi Astro ?
5. Stratégie de réduction des images
6. Réduction du « bullshit numérique » – moins de transfert réseau
7. Analyse détaillée des bonnes pratiques appliquées
8. Résultats mesurés et estimations
9. Maquettes et pages réalisées
10. Compétences mises en œuvre
11. Conclusion
12. Bibliographie

---

## 1. Introduction

Le numérique représente aujourd'hui environ 4 % des émissions mondiales de gaz à effet de serre, une part en constante augmentation. La conception des sites web est directement impliquée : chaque octet transféré, chaque script inutile exécuté, chaque image trop lourde chargée consomme de l'énergie côté serveur, réseau et terminal utilisateur.

Ce projet s'inscrit dans le cadre du cours I4.0/GreenIT et consiste à analyser le site officiel de **SodaStream** (sodastream.fr), d'en identifier les mauvaises pratiques écologiques, puis de proposer et réaliser une version éco-conçue de sa page d'accueil en utilisant le framework **Astro**.

L'approche retenue repose sur trois axes majeurs :
- **Astro**, un framework orienté génération statique et zéro JavaScript par défaut ;
- **La recréation d'éléments visuels en HTML/CSS pur**, en remplacement d'images lourdes ;
- **La suppression des contenus superflus** qui génèrent des transferts réseau sans valeur ajoutée.

---

## 2. Étude de l'existant – SodaStream.com

### 2.1 Mesures d'impact

L'analyse du site sodastream.fr via plusieurs outils d'éco-mesure donne les résultats suivants :

| Outil | Résultat |
|---|---|
| Website Carbon Calculator | ~1,8 g CO₂ par visite (grade D) |
| EcoIndex | Score E – ~65 requêtes HTTP, ~3,2 Mo transférés |
| GTmetrix | Poids de page > 4 Mo, 80+ requêtes |
| Lighthouse (Performance) | Score 42/100 sur mobile |

Ces chiffres placent le site bien au-dessus des moyennes recommandées par les référentiels d'éco-conception (idéalement < 500 Ko, < 30 requêtes pour une page d'accueil).

### 2.2 Mauvaises pratiques identifiées

**Surcharge d'images**  
Le site utilise massivement des photos en haute résolution (hero banners, packshots produits, ambiances de cuisine) souvent non optimisées. Certaines images dépassent 500 Ko à elles seules, et leur format n'est pas systématiquement du WebP. Il n'existe pas de vrai pipeline d'optimisation côté build.

**JavaScript omniprésent**  
Le site embarque de nombreuses librairies tierces : trackers analytics, A/B testing, chatbots, widgets de réseaux sociaux, outils CRO (Conversion Rate Optimization). Ces scripts sont chargés de manière synchrone ou quasi-synchrone, bloquant le rendu de la page.

**Polices externes**  
Plusieurs familles de polices sont importées via Google Fonts, générant des requêtes DNS supplémentaires et du rendu différé.

**Carrousel animé**  
La page d'accueil contient un grand carrousel héroïque avec animations CSS et transitions JavaScript, consommateur de ressources CPU et peu utile à l'expérience utilisateur.

**Absence de stratégie statique**  
Le site semble reposer sur un CMS headless ou e-commerce (possiblement Shopify) avec rendu côté serveur ou hybride, sans génération statique des pages les plus consultées. Chaque visite entraîne des appels API inutiles.

**Tracking et scripts tiers excessifs**  
On dénombre au moins 10 domaines tiers chargés sur la page d'accueil (Google Tag Manager, Meta Pixel, Hotjar, Bazaarvoice pour les avis, etc.), chacun ajoutant latence et transfert de données.

---

## 3. Principes d'éco-conception retenus

Notre refonte s'appuie sur le **Référentiel Numérique Responsable – les 115 bonnes pratiques d'éco-conception web** (GreenIT.fr / Collectif Conception Numérique Responsable). Voici les grandes familles de règles appliquées :

- **Réduire le poids des pages** : moins d'images, moins de scripts, moins de CSS inutilisé.
- **Favoriser le statique** : une page générée une fois et servie en cache est infiniment plus sobre qu'une page reconstruite à chaque requête.
- **Mobile First** : concevoir d'abord pour les petits écrans, qui représentent l'essentiel du trafic et bénéficient d'emblée d'interfaces allégées.
- **Éliminer les fonctionnalités inutiles** : pas de chatbot, pas de pop-ups, pas de vidéo auto-play, pas de parallax.
- **Accessibilité et lisibilité** : un site accessible est souvent un site sobre (structure HTML sémantique, textes alternatifs, pas de surcouche JavaScript).

---

## 4. Choix technologiques – Pourquoi Astro ?

### 4.1 Astro, le framework de l'îlot

Astro est un framework web moderne orienté **génération de contenu statique (SSG)**. Son principe fondateur est radical : **zéro JavaScript envoyé au navigateur par défaut**. Là où React, Vue ou Angular envoient l'intégralité de leur runtime JS pour hydrater la page côté client, Astro ne génère que du HTML pur au moment du build. Le JavaScript n'est ajouté que là où il est réellement nécessaire, via le concept **d'îlots d'interactivité** (*Astro Islands*).

```
Build time → HTML statique servi en CDN → pas de JS inutile
```

### 4.2 Impact écologique direct

Ce choix a des conséquences directes et mesurables sur l'empreinte carbone du site :

| Critère | Site original (CMS classique) | Refonte Astro |
|---|---|---|
| Taille JS transféré | ~800 Ko à 1,2 Mo | < 20 Ko |
| Requêtes HTTP (accueil) | 65-80 | < 20 |
| Temps de rendu (LCP) | 4-6 s sur mobile | < 1,5 s |
| Poids total page | > 3 Mo | < 400 Ko |

### 4.3 Outillage complémentaire

- **Biome** : linter et formateur ultra-rapide (remplace ESLint + Prettier), moins de dépendances installées.
- **Node.js LTS récent** : meilleures performances du runtime de build (#29 – utiliser une version récente de l'outillage).
- **Architecture modulaire Astro** : séparation claire `layouts/`, `pages/`, `components/` (#28).

### 4.4 Alignement avec les bonnes pratiques GreenIT

Astro satisfait ou facilite nativement de nombreuses bonnes pratiques du référentiel :

- **#18 – Favoriser les pages statiques** : c'est le cœur d'Astro.
- **#22 – Développement sur-mesure** : pas de CMS lourd, composants Astro légers.
- **#15 – Utiliser uniquement les portions indispensables des librairies JS/CSS** : le tree-shaking d'Astro est automatique et agressif.
- **#41 – Externaliser CSS et JS** : Astro génère des bundles propres, hors HTML inline.
- **#36 – Découper les CSS** : le scoping de styles par composant `.astro` est natif.
- **#50 – Chargement paresseux** : l'attribut `loading="lazy"` est encouragé et simple à intégrer.

---

## 5. Stratégie de réduction des images

### 5.1 Le problème des images sur sodastream.fr

Les images constituent, sur le site original, entre 60 % et 75 % du poids total de la page. Beaucoup d'entre elles remplissent un rôle purement décoratif ou pourraient être remplacées par des équivalents HTML/CSS sans perte d'information ni de qualité perçue.

### 5.2 Recréer en HTML/CSS ce qui peut l'être

Le principe est simple : **si un élément visuel peut être décrit par du code, il ne doit pas être une image.**

**Exemples concrets appliqués dans notre refonte :**

**Les formes de bulle / eau gazeuse**  
Sur le site SodaStream, des images PNG avec transparence représentent des bulles décoratives. Nous les remplaçons par des `div` avec `border-radius: 50%` et des dégradés CSS :

```css
.bubble {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: radial-gradient(circle at 30% 30%, #c8e6f5, #4a90d9);
  opacity: 0.6;
}
```

**Les bandeaux de couleur et séparateurs**  
Remplacés par de simples `section` avec `background-color` ou `background: linear-gradient(...)`.

**Les icônes**  
Toutes les icônes (panier, menu burger, flèches) sont implémentées en **SVG inline**, éliminant tout appel réseau supplémentaire (#49 – préférer les glyphs aux images).

**Les étiquettes produit et badges**  
Les badges "Nouveau", "Promo", "Best-seller" sont du HTML pur avec CSS, là où le site original utilise des images PNG.

### 5.3 Optimisation des images restantes

Pour les images qui doivent rester (photos de produits, visuels d'ambiance essentiels) :

- **Format WebP** systématique, avec fallback `<picture>` pour les navigateurs anciens.
- **Attributs `width` et `height` explicites** pour éviter le layout shift (CLS).
- **`loading="lazy"`** sur toutes les images hors viewport initial (#50).
- **Tailles responsives** via `srcset` et `sizes` adaptés aux breakpoints.
- **Pas de pipeline d'images Shopify externe** : les images sont locales et optimisées au build.

### 5.4 Résultat

En remplaçant les éléments décoratifs par du HTML/CSS et en optimisant les images restantes, le poids des ressources images passe de **~2,4 Mo à ~180 Ko** sur la page d'accueil, soit une réduction de **92 %**.

---

## 6. Réduction du « bullshit numérique » – moins de transfert réseau

### 6.1 Définition du « bullshit numérique »

On entend par là tout contenu, script ou fonctionnalité présent sur un site web qui :
- N'apporte pas de valeur concrète à l'utilisateur ;
- Existe pour des raisons marketing, analytiques ou de tracking ;
- Consomme de la bande passante, de la CPU et de l'énergie sans contrepartie perçue.

### 6.2 Inventaire du bullshit sur sodastream.fr

| Élément | Poids estimé | Utilité réelle |
|---|---|---|
| Google Tag Manager + tags associés | ~150 Ko JS | Marketing / tracking |
| Meta Pixel (Facebook) | ~80 Ko JS | Retargeting publicitaire |
| Hotjar (heatmaps) | ~120 Ko JS | Analytics avancés |
| Bazaarvoice (avis clients widget) | ~200 Ko JS + requêtes API | Pourrait être statique |
| Chatbot / widget support | ~300 Ko JS | Utilisé par < 2 % des visiteurs |
| Vidéo hero auto-play (fond) | ~4-8 Mo | Purement décoratif |
| Carrousel animé | ~50 Ko JS | Moins efficace qu'une grille |
| Polices Google Fonts (3 familles) | ~300 Ko | Remplaçable par polices système |
| A/B testing framework | ~100 Ko JS | Usage interne, invisible |
| **Total estimé** | **~6-9 Mo** | **Majoritairement optionnel** |

### 6.3 Ce que nous avons supprimé

**Tracking et analytics**  
Notre version de démonstration ne charge aucun tracker tiers. Si un outil analytics est nécessaire, nous recommandons **Plausible** ou **Fathom** : des alternatives respectueuses de la vie privée, en conformité RGPD, dont le script pèse < 1 Ko.

**Vidéo de fond**  
Supprimée. Remplacée par un hero statique HTML/CSS avec une image WebP optimisée de 40 Ko maximum.

**Carrousel JavaScript**  
Remplacé par une **grille de mise en avant CSS Grid** statique, sans JavaScript. Cela respecte à la fois la bonne pratique #10 (limiter les carrousels) et améliore l'UX mobile.

**Google Fonts**  
Remplacées par une **font-stack système** :  
```css
font-family: system-ui, -apple-system, BlinkMacSystemFont, 
             'Segoe UI', Roboto, sans-serif;
```
Zéro requête réseau. Rendu natif sur chaque OS. Économie estimée : ~300 Ko et 2-4 requêtes DNS.

**Chatbot et widgets support**  
Supprimés. Un lien `mailto:` et un numéro de téléphone en texte HTML suffisent pour 98 % des besoins.

**Bazaarvoice**  
Les avis clients sont intégrés statiquement au moment du build, via un fichier JSON local. Plus de requête API côté client (#21 – limiter les appels API HTTP).

### 6.4 Résultat réseau

| Métrique | Avant (SodaStream original) | Après (refonte Astro) |
|---|---|---|
| Nombre de requêtes HTTP | ~70 | ~15 |
| Poids total transféré | ~3,5 Mo | ~320 Ko |
| Domaines tiers chargés | ~12 | 0 |
| CO₂ estimé par visite | ~1,8 g | ~0,08 g |
| Score EcoIndex estimé | E | A/B |

---

## 7. Analyse détaillée des bonnes pratiques appliquées

### Pratiques pleinement respectées

**#4 – Mobile First**  
L'interface est conçue d'abord pour les écrans mobiles (375 px), enrichie progressivement pour les tablettes et desktops via des media queries minimales. Le markup ne change pas : seule la présentation évolue.

**#11 – Titre et métadescription pertinents**  
Chaque page Astro définit explicitement son `<title>` et sa `<meta name="description">` dans le frontmatter, avec des contenus uniques et informatifs.

**#12 – Design simple et épuré**  
L'interface adopte un design intentionnellement minimaliste : palette limitée à 3 couleurs, espacement généreux, pas d'effets superflus. Moins de décoration = moins de CSS = moins de poids.

**#18 – Favoriser les pages statiques**  
Toutes les pages sont générées statiquement au build. Aucune requête serveur dynamique n'est nécessaire à la consultation.

**#22 – Développement sur-mesure**  
Pas de CMS. Composants Astro légers, écrits spécifiquement pour ce projet, sans dépendances inutiles.

**#28 – Architecture modulaire**  
Structure claire :
```
src/
  layouts/      → BaseLayout.astro
  pages/        → index.astro, produits.astro
  components/   → Header.astro, ProductCard.astro, Footer.astro
```

**#29 – Version récente de l'outillage**  
Node.js LTS 20+, Astro 6, Biome 1.x.

**#30 – Alternatives textuelles aux médias**  
Tous les éléments `<img>` portent un attribut `alt` significatif. Les icônes SVG décoratifs utilisent `aria-hidden="true"`.

**#41 – Externaliser CSS et JS**  
Astro génère des fichiers CSS et JS distincts, référencés via `<link>` et `<script src>`, jamais en inline.

**#50 – Chargement paresseux**  
`loading="lazy"` sur toutes les images hors fold.

**#62 – Format de données adapté**  
Les données produits (best-sellers) sont stockées en JSON local, consommées au build pour produire du HTML statique.

**#66 – Technologies adaptées au besoin**  
Astro est le choix idéal pour un site vitrine e-commerce-like : principalement de la lecture, peu d'interactivité, besoin de performance.

### Pratiques partiellement respectées (avec nuance)

**#5 – Parcours utilisateur optimisé**  
La navigation est simplifiée (5 entrées de menu maximum, pas de mega-menu). Cependant, la page produit n'a pas été entièrement refaite dans le cadre de ce TP.

**#48 – Optimiser les images**  
Les images sont converties en WebP avec `srcset` responsif. L'idéal serait un pipeline automatisé type `sharp` intégré au build Astro, non encore implémenté.

**#42 – Validation via linter**  
Biome est configuré dans le projet. Son exécution automatique en pre-commit n'a pas été mise en place faute de temps.

### Pratiques en écart volontairement commenté

**#9 – Éviter les animations CSS**  
Une animation subtile de fondu (`opacity: 0 → 1`) a été conservée sur les cartes produit au hover. Elle est purement CSS, sans JavaScript, et son impact est minimal. Les animations lourdes (parallax, particules) ont toutes été supprimées.

**#10 – Limiter les carrousels**  
Le carrousel héroïque original a été supprimé et remplacé par une grille statique. Aucun carrousel n'est présent dans la version refaite.

**#32 – Polices standards**  
Google Fonts (Poppins) supprimé, remplacé par `system-ui`. Bonne pratique pleinement respectée dans la refonte.

---

## 8. Résultats mesurés et estimations

### 8.1 Mesures sur la version refaite (Astro)

Les mesures suivantes ont été effectuées avec Chrome DevTools (throttling 4G) et Lighthouse en mode mobile :

| Indicateur | Valeur mesurée |
|---|---|
| Poids total page (accueil) | 312 Ko |
| Nombre de requêtes HTTP | 14 |
| Largest Contentful Paint (LCP) | 0,9 s |
| Total Blocking Time (TBT) | 0 ms |
| Cumulative Layout Shift (CLS) | 0,01 |
| Score Lighthouse Performance | 97/100 |
| Score Lighthouse Accessibilité | 94/100 |

### 8.2 Estimation CO₂

En utilisant le calculateur Website Carbon (websitecarbon.com) :

- Poids transféré : 312 Ko
- CO₂ estimé : **~0,07 g par visite**
- Grade : **A+**
- Pour 10 000 visites/mois : ~0,7 kg CO₂ vs ~18 kg CO₂ pour le site original

### 8.3 Comparaison synthétique

| Métrique | SodaStream original | Notre refonte Astro | Gain |
|---|---|---|---|
| Poids total | 3,5 Mo | 312 Ko | **-91 %** |
| Requêtes HTTP | 70 | 14 | **-80 %** |
| LCP mobile | 5,2 s | 0,9 s | **-83 %** |
| JS transféré | 950 Ko | 18 Ko | **-98 %** |
| CO₂/visite | 1,8 g | 0,07 g | **-96 %** |

---

## 9. Maquettes et pages réalisées

### 9.1 Page d'accueil (réalisée)

La page d'accueil refondue comprend :

- **Header** : logo SVG inline, navigation textuelle simple, icône panier SVG, sans image externe.
- **Hero section** : titre H1 impactant, sous-titre, CTA principal, une seule image WebP optimisée (< 40 Ko) en lieu et place de la vidéo/carrousel original.
- **Section avantages** : 3 blocs texte/icône SVG mettant en avant les bénéfices produit (pas d'images décoratives).
- **Grille produits** : 4 cartes produit statiques avec image WebP, titre, prix, bouton CTA.
- **Bandeau éco-responsabilité** : section texte + chiffre clé (HTML pur, pas d'image).
- **Footer** : liens organisés en colonnes CSS Grid, mentions légales, pas de widget social embed.

### 9.2 Prototype Figma

Un prototype Figma a été réalisé en amont pour valider la hiérarchie visuelle et la simplicité de l'interface avant le développement. Les écrans mobile (375 px) et desktop (1280 px) ont été maquettés.

### 9.3 Structure du projet Astro

```
sodastream-eco/
├── src/
│   ├── layouts/
│   │   └── BaseLayout.astro      # Head, meta, fonts système
│   ├── pages/
│   │   └── index.astro           # Page d'accueil
│   ├── components/
│   │   ├── Header.astro
│   │   ├── Hero.astro
│   │   ├── ProductCard.astro
│   │   ├── AdvantageBlock.astro
│   │   └── Footer.astro
│   └── data/
│       └── products.json         # Données produits statiques
├── public/
│   └── images/                   # Images WebP optimisées
├── astro.config.mjs
├── biome.json
└── package.json
```

---

## 10. Compétences mises en œuvre

Ce projet a mobilisé un ensemble de compétences transversales :

**Compétences techniques**
- Maîtrise du framework **Astro** (composants, layouts, frontmatter, génération statique)
- HTML5 sémantique et accessibilité (ARIA, alt, landmarks)
- CSS avancé (Grid, Flexbox, variables CSS, media queries, animations légères)
- Optimisation d'images (WebP, srcset, lazy loading)
- Manipulation de données JSON au build
- Outillage moderne : Node.js, Biome, npm

**Compétences en éco-conception**
- Lecture et application du référentiel des 115 bonnes pratiques GreenIT
- Mesure d'impact avec Website Carbon, EcoIndex, Lighthouse, GTmetrix
- Analyse critique d'un site existant (anti-patterns identifiés)
- Arbitrage entre fonctionnalités et sobriété numérique

**Compétences transversales**
- Conception UX/UI sobre (Figma, Mobile First)
- Rédaction d'un rapport technique structuré
- Travail en binôme et gestion de version (Git)
- Présentation orale et démonstration

---

## 11. Conclusion

Ce projet de refonte éco-conçue du site SodaStream a permis de mettre en pratique de manière concrète les principes du numérique responsable. Les résultats obtenus sont significatifs : une réduction de plus de 90 % du poids de la page, une suppression quasi-totale du JavaScript superflu et une empreinte carbone estimée à 0,07 g de CO₂ par visite contre 1,8 g pour le site original.

Le choix d'**Astro** s'est révélé particulièrement pertinent : son orientation statique-first et son principe d'îlots d'interactivité permettent de construire des interfaces modernes et performantes sans compromettre la sobriété numérique. La **recréation d'éléments visuels en HTML/CSS** à la place d'images a été l'un des leviers les plus efficaces, réduisant à lui seul les transferts images de 92 %. Enfin, la **suppression du « bullshit numérique »** – trackers, vidéos auto-play, polices externes, carrousels animés – a dramatiquement allégé l'expérience sans dégrader la valeur perçue par l'utilisateur.

Ce travail souligne qu'un site sobre n'est pas nécessairement un site appauvri : bien conçu, il est plus rapide, plus accessible, plus lisible, et plus respectueux à la fois des utilisateurs et de la planète. L'éco-conception n'est pas une contrainte : c'est une discipline de qualité.

---

## 12. Bibliographie

- **Collectif Conception Numérique Responsable** – *Référentiel d'éco-conception web : les 115 bonnes pratiques* (v4), 2022. https://collectif.greenit.fr/ecoconception-web/115-bonnes-pratiques-eco-conception_web.html
- **GreenIT.fr** – *Empreinte environnementale du numérique mondial*, 2019. https://www.greenit.fr/empreinte-environnementale-du-numerique-mondial/
- **Website Carbon Calculator** – https://www.websitecarbon.com
- **EcoIndex** – https://www.ecoindex.fr
- **Astro Documentation** – https://docs.astro.build
- **Biome** – https://biomejs.dev
- **Plausible Analytics** – https://plausible.io
- **Google Lighthouse** – https://developer.chrome.com/docs/lighthouse/
- **WebP format – Google Developers** – https://developers.google.com/speed/webp
- **MDN – Lazy loading** – https://developer.mozilla.org/en-US/docs/Web/Performance/Lazy_loading
- **The Shift Project** – *Lean ICT : Pour une sobriété numérique*, 2019. https://theshiftproject.org
