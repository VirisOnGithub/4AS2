use eframe::egui::{
    self, Color32, ColorImage, Pos2, Rect, Stroke, TextureHandle, TextureOptions, Vec2,
};
use rand::Rng;
use std::f64::consts::PI;
use std::time::Duration;

// ──────────────────────────────────────────────────────────────────────────────
// Fonctions
// ──────────────────────────────────────────────────────────────────────────────

#[derive(Debug, Clone, PartialEq)]
enum Fonction {
    // 2D ──────────────────────────────────────────────
    Rastrigin,
    Rosenbrock,
    Ackley,
    Himmelblau,
    Sphere,
    Booth,
    Beale,
    // 1D ──────────────────────────────────────────────
    /// Double puits : (x²−2)² + 0.3x  — deux minima locaux
    PuitsDouble,
    /// Rastrigin 1D : x² − 10·cos(2πx) + 10  — très multimodal
    Rastrigin1D,
    /// Sinusoïde modulée : sin(3x) + 0.05·x²  — nombreux minima locaux
    Sinus1D,
}

impl Fonction {
    fn nom(&self) -> &str {
        match self {
            Fonction::Rastrigin => "Rastrigin 2D",
            Fonction::Rosenbrock => "Rosenbrock 2D",
            Fonction::Ackley => "Ackley 2D",
            Fonction::Himmelblau => "Himmelblau 2D",
            Fonction::Sphere => "Sphère 2D",
            Fonction::Booth => "Booth 2D",
            Fonction::Beale => "Beale 2D",
            Fonction::PuitsDouble => "Double Puits 1D",
            Fonction::Rastrigin1D => "Rastrigin 1D",
            Fonction::Sinus1D => "Sinus modulée 1D",
        }
    }

    fn est_1d(&self) -> bool {
        matches!(
            self,
            Fonction::PuitsDouble | Fonction::Rastrigin1D | Fonction::Sinus1D
        )
    }

    /// Pour les 2D : [x_min, x_max, y_min, y_max]
    /// Pour les 1D : [x_min, x_max, f_min_display, f_max_display]
    fn domaine(&self) -> [f64; 4] {
        match self {
            Fonction::Rastrigin => [-5.12, 5.12, -5.12, 5.12],
            Fonction::Rosenbrock => [-2.0, 2.0, -1.0, 3.0],
            Fonction::Ackley => [-5.0, 5.0, -5.0, 5.0],
            Fonction::Himmelblau => [-5.0, 5.0, -5.0, 5.0],
            Fonction::Sphere => [-5.0, 5.0, -5.0, 5.0],
            Fonction::Booth => [-10.0, 10.0, -10.0, 10.0],
            Fonction::Beale => [-4.5, 4.5, -4.5, 4.5],
            Fonction::PuitsDouble => [-2.5, 2.5, -1.5, 11.0],
            Fonction::Rastrigin1D => [-5.12, 5.12, -1.0, 42.0],
            Fonction::Sinus1D => [-5.0, 5.0, -1.2, 2.2],
        }
    }

    /// Évaluation — pour les fonctions 1D, y est ignoré.
    fn eval(&self, x: f64, y: f64) -> f64 {
        match self {
            Fonction::Rastrigin => {
                20.0 + x * x - 10.0 * (2.0 * PI * x).cos() + y * y - 10.0 * (2.0 * PI * y).cos()
            }
            Fonction::Rosenbrock => (1.0 - x).powi(2) + 100.0 * (y - x * x).powi(2),
            Fonction::Ackley => {
                -20.0 * (-0.2 * (0.5 * (x * x + y * y)).sqrt()).exp()
                    - (0.5 * ((2.0 * PI * x).cos() + (2.0 * PI * y).cos())).exp()
                    + std::f64::consts::E
                    + 20.0
            }
            Fonction::Himmelblau => (x * x + y - 11.0).powi(2) + (x + y * y - 7.0).powi(2),
            Fonction::Sphere => x * x + y * y,
            Fonction::Booth => (x + 2.0 * y - 7.0).powi(2) + (2.0 * x + y - 5.0).powi(2),
            Fonction::Beale => {
                (1.5 - x + x * y).powi(2)
                    + (2.25 - x + x * y * y).powi(2)
                    + (2.625 - x + x * y * y * y).powi(2)
            }
            // 1D — y ignoré
            Fonction::PuitsDouble => (x * x - 2.0).powi(2) + 0.3 * x,
            Fonction::Rastrigin1D => x * x - 10.0 * (2.0 * PI * x).cos() + 10.0,
            Fonction::Sinus1D => (3.0 * x).sin() + 0.05 * x * x,
        }
    }

    /// Minimum global connu analytiquement (pour les 2D) ou numériquement (1D).
    /// Retourne (x, y, f) — pour les 1D y=0.
    fn minimum_global(&self) -> (f64, f64, f64) {
        match self {
            Fonction::Rastrigin => (0.0, 0.0, 0.0),
            Fonction::Rosenbrock => (1.0, 1.0, 0.0),
            Fonction::Ackley => (0.0, 0.0, 0.0),
            Fonction::Himmelblau => (3.0, 2.0, 0.0),
            Fonction::Sphere => (0.0, 0.0, 0.0),
            Fonction::Booth => (1.0, 3.0, 0.0),
            Fonction::Beale => (3.0, 0.5, 0.0),
            // Pour les 1D on calcule numériquement
            f => {
                let d = f.domaine();
                let n = 50_000usize;
                let (mut bx, mut bf) = (d[0], f64::MAX);
                for i in 0..=n {
                    let x = d[0] + (i as f64 / n as f64) * (d[1] - d[0]);
                    let fv = f.eval(x, 0.0);
                    if fv < bf {
                        bf = fv;
                        bx = x;
                    }
                }
                (bx, 0.0, bf)
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Info sur la dernière décision (pour le mode pédagogique)
// ──────────────────────────────────────────────────────────────────────────────

#[derive(Clone, Default)]
struct DecisionInfo {
    etape_num: u64,
    temperature: f64,
    x_avant: f64,
    y_avant: f64,
    e_avant: f64,
    x_candidat: f64,
    y_candidat: f64,
    e_candidat: f64,
    delta: f64,
    /// Probabilité d'acceptation Metropolis (None si delta ≤ 0 → toujours accepté)
    prob: Option<f64>,
    accepte: bool,
}

// ──────────────────────────────────────────────────────────────────────────────
// Recuit simulé
// ──────────────────────────────────────────────────────────────────────────────

const RESOLUTION: usize = 400;
const MAX_TRAIL: usize = 2000;

struct RecuitState {
    x: f64,
    y: f64,
    energie: f64,
    meilleur_x: f64,
    meilleur_y: f64,
    meilleure_energie: f64,
    temperature: f64,
    t_initial: f64,
    t_final: f64,
    alpha: f64,
    pas: f64,
    etapes_par_frame: usize,
    total_etapes: u64,
    refus: u64,
    acceptations: u64,
    trail: Vec<[f64; 2]>,
    en_cours: bool,
    fini: bool,
    derniere_decision: Option<DecisionInfo>,
}

impl RecuitState {
    fn nouveau(f: &Fonction, t_initial: f64, t_final: f64, alpha: f64, pas: f64) -> Self {
        let d = f.domaine();
        let mut rng = rand::thread_rng();
        let x = rng.gen_range(d[0]..d[1]);
        let y = if f.est_1d() {
            0.0
        } else {
            rng.gen_range(d[2]..d[3])
        };
        let e = f.eval(x, y);
        RecuitState {
            x,
            y,
            energie: e,
            meilleur_x: x,
            meilleur_y: y,
            meilleure_energie: e,
            temperature: t_initial,
            t_initial,
            t_final,
            alpha,
            pas,
            etapes_par_frame: 30,
            total_etapes: 0,
            refus: 0,
            acceptations: 0,
            trail: vec![[x, y]],
            en_cours: false,
            fini: false,
            derniere_decision: None,
        }
    }

    fn reset(&mut self, f: &Fonction) {
        let d = f.domaine();
        let mut rng = rand::thread_rng();
        let x = rng.gen_range(d[0]..d[1]);
        let y = if f.est_1d() {
            0.0
        } else {
            rng.gen_range(d[2]..d[3])
        };
        let e = f.eval(x, y);
        self.x = x;
        self.y = y;
        self.energie = e;
        self.meilleur_x = x;
        self.meilleur_y = y;
        self.meilleure_energie = e;
        self.temperature = self.t_initial;
        self.total_etapes = 0;
        self.refus = 0;
        self.acceptations = 0;
        self.trail = vec![[x, y]];
        self.en_cours = false;
        self.fini = false;
        self.derniere_decision = None;
    }

    fn etape(&mut self, f: &Fonction) {
        if self.fini {
            return;
        }
        let d = f.domaine();
        let mut rng = rand::thread_rng();

        let dx = rng.gen_range(-self.pas..self.pas);
        let dy = if f.est_1d() {
            0.0
        } else {
            rng.gen_range(-self.pas..self.pas)
        };

        let nx = (self.x + dx).clamp(d[0], d[1]);
        let ny = if f.est_1d() {
            0.0
        } else {
            (self.y + dy).clamp(d[2], d[3])
        };
        let ne = f.eval(nx, ny);
        let delta = ne - self.energie;

        let (accepte, prob) = if delta < 0.0 {
            (true, None)
        } else {
            let p = (-delta / self.temperature).exp();
            (rng.r#gen::<f64>() < p, Some(p))
        };

        self.derniere_decision = Some(DecisionInfo {
            etape_num: self.total_etapes + 1,
            temperature: self.temperature,
            x_avant: self.x,
            y_avant: self.y,
            e_avant: self.energie,
            x_candidat: nx,
            y_candidat: ny,
            e_candidat: ne,
            delta,
            prob,
            accepte,
        });

        if accepte {
            self.x = nx;
            self.y = ny;
            self.energie = ne;
            self.acceptations += 1;
            if ne < self.meilleure_energie {
                self.meilleure_energie = ne;
                self.meilleur_x = nx;
                self.meilleur_y = ny;
            }
        } else {
            self.refus += 1;
        }

        self.trail.push([self.x, self.y]);
        if self.trail.len() > MAX_TRAIL {
            self.trail.remove(0);
        }

        self.temperature *= self.alpha;
        self.total_etapes += 1;

        if self.temperature <= self.t_final {
            self.fini = true;
            self.en_cours = false;
        }
    }

    fn avancer(&mut self, f: &Fonction) {
        for _ in 0..self.etapes_par_frame {
            self.etape(f);
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Application
// ──────────────────────────────────────────────────────────────────────────────

struct App {
    fonction: Fonction,
    sa: RecuitState,
    texture: Option<TextureHandle>,
    texture_fn: Option<Fonction>,
    // Paramètres SA
    t_initial: f64,
    t_final: f64,
    alpha: f64,
    pas: f64,
    // Mode pédagogique
    /// Délai entre frames en ms (0 = vitesse max).  Ignoré en mode pas-à-pas.
    delai_ms: u64,
    /// En mode pas-à-pas, l'animation ne tourne pas : on avance manuellement.
    mode_pas_a_pas: bool,
}

impl App {
    fn new() -> Self {
        let f = Fonction::Rastrigin;
        let (t0, tf, alpha, pas) = (100.0, 1e-4, 0.9995, 0.3);
        App {
            sa: RecuitState::nouveau(&f, t0, tf, alpha, pas),
            fonction: f,
            texture: None,
            texture_fn: None,
            t_initial: t0,
            t_final: tf,
            alpha,
            pas,
            delai_ms: 0,
            mode_pas_a_pas: false,
        }
    }

    fn obtenir_texture(&mut self, ctx: &egui::Context) -> TextureHandle {
        let besoin = match &self.texture_fn {
            None => true,
            Some(f) => f != &self.fonction,
        };
        if besoin {
            let img = generer_heatmap(&self.fonction, RESOLUTION);
            let handle = ctx.load_texture("heatmap", img, TextureOptions::LINEAR);
            self.texture = Some(handle);
            self.texture_fn = Some(self.fonction.clone());
        }
        self.texture.clone().unwrap()
    }

    fn appliquer_params_au_sa(&mut self) {
        self.sa.t_initial = self.t_initial;
        self.sa.t_final = self.t_final;
        self.sa.alpha = self.alpha;
        self.sa.pas = self.pas;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Rendu
// ──────────────────────────────────────────────────────────────────────────────

fn generer_heatmap(f: &Fonction, res: usize) -> ColorImage {
    let d = f.domaine();
    let mut vals: Vec<f64> = Vec::with_capacity(res * res);
    for row in 0..res {
        for col in 0..res {
            let x = d[0] + (col as f64 / (res - 1) as f64) * (d[1] - d[0]);
            let y = d[3] - (row as f64 / (res - 1) as f64) * (d[3] - d[2]);
            vals.push(f.eval(x, y));
        }
    }
    let mut sorted = vals.clone();
    sorted.sort_by(|a, b| a.partial_cmp(b).unwrap());
    let vmin = sorted[0];
    let vmax = sorted[(sorted.len() as f64 * 0.97) as usize];
    let range = (vmax - vmin).max(1e-10);
    let pixels: Vec<Color32> = vals
        .iter()
        .map(|&v| viridis(((v - vmin) / range).clamp(0.0, 1.0) as f32))
        .collect();
    ColorImage {
        size: [res, res],
        pixels,
    }
}

fn viridis(t: f32) -> Color32 {
    let stops: [(f32, f32, f32, f32); 5] = [
        (0.0, 0.267, 0.005, 0.329),
        (0.25, 0.230, 0.322, 0.545),
        (0.5, 0.128, 0.567, 0.551),
        (0.75, 0.369, 0.788, 0.384),
        (1.0, 0.993, 0.906, 0.144),
    ];
    let i = stops
        .partition_point(|s| s.0 <= t)
        .saturating_sub(1)
        .min(stops.len() - 2);
    let (t0, r0, g0, b0) = stops[i];
    let (t1, r1, g1, b1) = stops[i + 1];
    let u = if (t1 - t0).abs() < 1e-6 {
        0.0
    } else {
        (t - t0) / (t1 - t0)
    };
    Color32::from_rgb(
        ((r0 + u * (r1 - r0)).clamp(0.0, 1.0) * 255.0) as u8,
        ((g0 + u * (g1 - g0)).clamp(0.0, 1.0) * 255.0) as u8,
        ((b0 + u * (b1 - b0)).clamp(0.0, 1.0) * 255.0) as u8,
    )
}

fn espace_vers_ecran(x: f64, y: f64, d: &[f64; 4], rect: Rect) -> Pos2 {
    let u = ((x - d[0]) / (d[1] - d[0])) as f32;
    let v = (1.0 - (y - d[2]) / (d[3] - d[2])) as f32;
    Pos2 {
        x: rect.min.x + u * rect.width(),
        y: rect.min.y + v * rect.height(),
    }
}

/// Dessine la courbe 1D, la trajectoire des x, les marqueurs courant/meilleur/optimal.
fn dessiner_1d(f: &Fonction, sa: &RecuitState, painter: &egui::Painter, rect: Rect) {
    let d = f.domaine(); // [xmin, xmax, fmin_display, fmax_display]

    // Fond
    painter.rect_filled(rect, 4.0, Color32::from_rgb(18, 18, 28));

    // Grille horizontale légère
    let n_grid = 5usize;
    for i in 0..=n_grid {
        let fv = d[2] + (i as f64 / n_grid as f64) * (d[3] - d[2]);
        let vy = rect.min.y + (1.0 - (i as f32 / n_grid as f32)) * rect.height();
        painter.line_segment(
            [Pos2::new(rect.min.x, vy), Pos2::new(rect.max.x, vy)],
            Stroke::new(0.5, Color32::from_rgba_unmultiplied(255, 255, 255, 25)),
        );
        // Label valeur
        painter.text(
            Pos2::new(rect.min.x + 4.0, vy - 10.0),
            egui::Align2::LEFT_TOP,
            format!("{fv:.2}"),
            egui::FontId::monospace(10.0),
            Color32::from_rgba_unmultiplied(200, 200, 200, 120),
        );
    }

    // Courbe de la fonction
    let n_pts = 800usize;
    let curve: Vec<Pos2> = (0..=n_pts)
        .map(|i| {
            let xf = d[0] + (i as f64 / n_pts as f64) * (d[1] - d[0]);
            let yf = f.eval(xf, 0.0);
            espace_vers_ecran(xf, yf, &d, rect)
        })
        .collect();

    for i in 1..curve.len() {
        painter.line_segment(
            [curve[i - 1], curve[i]],
            Stroke::new(2.5, Color32::from_rgb(80, 160, 240)),
        );
    }

    // Axe x (y=0 ou bas si f>0 partout)
    let y0_screen = espace_vers_ecran(0.0, 0.0, &d, rect)
        .y
        .clamp(rect.min.y, rect.max.y);
    painter.line_segment(
        [
            Pos2::new(rect.min.x, y0_screen),
            Pos2::new(rect.max.x, y0_screen),
        ],
        Stroke::new(1.0, Color32::from_rgba_unmultiplied(255, 255, 255, 60)),
    );

    // Trajectoire (x seulement → trait vertical fantôme sur la courbe)
    let tl = sa.trail.len();
    if tl > 1 {
        for i in 1..tl {
            let alpha = ((i as f32 / tl as f32) * 200.0) as u8;
            let col = Color32::from_rgba_unmultiplied(255, 200, 50, alpha);
            let x0f = sa.trail[i - 1][0];
            let x1f = sa.trail[i][0];
            let y0f = f.eval(x0f, 0.0);
            let y1f = f.eval(x1f, 0.0);
            let p0 = espace_vers_ecran(x0f, y0f, &d, rect);
            let p1 = espace_vers_ecran(x1f, y1f, &d, rect);
            painter.line_segment([p0, p1], Stroke::new(1.5, col));
        }
    }

    // Minimum global (croix blanche)
    let (gx, _, _) = f.minimum_global();
    let gyf = f.eval(gx, 0.0);
    let gp = espace_vers_ecran(gx, gyf, &d, rect);
    let cs = 8.0f32;
    for &(ddx, ddy) in &[(-cs, 0.0f32), (cs, 0.0), (0.0, -cs), (0.0, cs)] {
        painter.line_segment(
            [gp, Pos2::new(gp.x + ddx, gp.y + ddy)],
            Stroke::new(2.5, Color32::WHITE),
        );
    }

    // Point candidat (si mode pas-à-pas, décision en cours)
    if let Some(dec) = &sa.derniere_decision {
        let cand_yf = f.eval(dec.x_candidat, 0.0);
        let cp = espace_vers_ecran(dec.x_candidat, cand_yf, &d, rect);
        // Ligne verticale pointillée orange
        for k in 0..10 {
            let yt = rect.min.y + (k as f32 / 10.0) * (rect.max.y - rect.min.y);
            if k % 2 == 0 {
                painter.line_segment(
                    [
                        Pos2::new(cp.x, yt),
                        Pos2::new(cp.x, yt + (rect.height() / 10.0) * 0.6),
                    ],
                    Stroke::new(1.5, Color32::from_rgba_unmultiplied(255, 160, 0, 160)),
                );
            }
        }
        painter.circle_filled(cp, 5.0, Color32::from_rgb(255, 160, 0));
        painter.circle_stroke(cp, 5.0, Stroke::new(1.5, Color32::WHITE));
    }

    // Meilleur x (cyan)
    let byf = f.eval(sa.meilleur_x, 0.0);
    let bp = espace_vers_ecran(sa.meilleur_x, byf, &d, rect);
    painter.circle_filled(bp, 6.0, Color32::from_rgb(0, 230, 230));
    painter.circle_stroke(bp, 6.0, Stroke::new(2.0, Color32::WHITE));

    // Position courante (rouge)
    let cyf = f.eval(sa.x, 0.0);
    let cp = espace_vers_ecran(sa.x, cyf, &d, rect);
    // Ligne verticale depuis le bas
    painter.line_segment(
        [Pos2::new(cp.x, rect.max.y), cp],
        Stroke::new(1.0, Color32::from_rgba_unmultiplied(255, 70, 70, 100)),
    );
    painter.circle_filled(cp, 6.0, Color32::from_rgb(255, 70, 70));
    painter.circle_stroke(cp, 6.0, Stroke::new(1.5, Color32::WHITE));
}

// ──────────────────────────────────────────────────────────────────────────────
// Panneau d'explication (mode pédagogique)
// ──────────────────────────────────────────────────────────────────────────────

fn afficher_explication(ui: &mut egui::Ui, dec: &DecisionInfo, est_1d: bool) {
    let bg = if dec.accepte {
        Color32::from_rgba_unmultiplied(0, 80, 0, 200)
    } else {
        Color32::from_rgba_unmultiplied(80, 0, 0, 200)
    };

    egui::Frame::new()
        .fill(bg)
        .inner_margin(egui::Margin::same(8))
        .corner_radius(egui::CornerRadius::same(6))
        .show(ui, |ui| {
            ui.label(egui::RichText::new(format!("Étape #{}", dec.etape_num)).strong());
            ui.separator();

            ui.label(format!("🌡 Température : {:.4e}", dec.temperature));
            ui.separator();

            // Position avant
            if est_1d {
                ui.label(format!("📍 Position actuelle   x = {:.4}", dec.x_avant));
                ui.label(format!("   f(x) = {:.4}", dec.e_avant));
                ui.separator();
                ui.label(format!("➡  Candidat proposé   x' = {:.4}", dec.x_candidat));
                ui.label(format!("   f(x') = {:.4}", dec.e_candidat));
            } else {
                ui.label(format!(
                    "📍 Position actuelle   ({:.3}, {:.3})",
                    dec.x_avant, dec.y_avant
                ));
                ui.label(format!("   E = {:.4}", dec.e_avant));
                ui.separator();
                ui.label(format!(
                    "➡  Candidat proposé   ({:.3}, {:.3})",
                    dec.x_candidat, dec.y_candidat
                ));
                ui.label(format!("   E' = {:.4}", dec.e_candidat));
            }
            ui.separator();

            let delta_str = if dec.delta < 0.0 {
                format!("ΔE = {:.4}  (amélioration ✓)", dec.delta)
            } else {
                format!("ΔE = +{:.4}  (dégradation)", dec.delta)
            };
            ui.label(delta_str);

            match dec.prob {
                None => {
                    ui.label("Acceptation : automatique (ΔE < 0)");
                }
                Some(p) => {
                    ui.label(format!("P(accept) = e^(−ΔE/T) = {:.4}", p));
                    ui.label(format!("  → tirage aléatoire < {:.4}", p));
                }
            }

            ui.separator();
            let (symbole, texte, couleur) = if dec.accepte {
                (
                    "✅",
                    "ACCEPTÉ — déplacement effectué",
                    Color32::from_rgb(100, 255, 100),
                )
            } else {
                (
                    "❌",
                    "REFUSÉ  — position inchangée",
                    Color32::from_rgb(255, 120, 120),
                )
            };
            ui.colored_label(couleur, format!("{symbole} {texte}"));
        });
}

// ──────────────────────────────────────────────────────────────────────────────
// Boucle principale egui
// ──────────────────────────────────────────────────────────────────────────────

impl eframe::App for App {
    fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        // ── Avancement de la simulation ────────────────────────────────────
        if self.sa.en_cours && !self.mode_pas_a_pas {
            if self.delai_ms > 0 {
                // Mode lent : 1 étape par frame avec délai
                self.sa.etape(&self.fonction);
                ctx.request_repaint_after(Duration::from_millis(self.delai_ms));
            } else {
                // Mode rapide : N étapes par frame
                self.sa.avancer(&self.fonction);
                ctx.request_repaint();
            }
        }

        // ── Panneau latéral ────────────────────────────────────────────────
        egui::SidePanel::left("controles")
            .exact_width(280.0)
            .show(ctx, |ui| {
                egui::ScrollArea::vertical().show(ui, |ui| {
                    ui.heading("⚙ Paramètres");
                    ui.separator();

                    // ── Sélecteur de fonction ──────────────────────────────
                    ui.label("Fonction :");
                    let fonctions_2d = [
                        Fonction::Rastrigin,
                        Fonction::Rosenbrock,
                        Fonction::Ackley,
                        Fonction::Himmelblau,
                        Fonction::Sphere,
                        Fonction::Booth,
                        Fonction::Beale,
                    ];
                    let fonctions_1d = [
                        Fonction::PuitsDouble,
                        Fonction::Rastrigin1D,
                        Fonction::Sinus1D,
                    ];
                    let ancien = self.fonction.clone();
                    egui::ComboBox::from_id_salt("fn_box")
                        .selected_text(self.fonction.nom())
                        .show_ui(ui, |ui| {
                            ui.label("── 2D ──");
                            for f in &fonctions_2d {
                                ui.selectable_value(&mut self.fonction, f.clone(), f.nom());
                            }
                            ui.separator();
                            ui.label("── 1D ──");
                            for f in &fonctions_1d {
                                ui.selectable_value(&mut self.fonction, f.clone(), f.nom());
                            }
                        });
                    if self.fonction != ancien {
                        self.sa = RecuitState::nouveau(
                            &self.fonction,
                            self.t_initial,
                            self.t_final,
                            self.alpha,
                            self.pas,
                        );
                        self.texture_fn = None;
                    }

                    ui.separator();
                    // ── Paramètres SA ──────────────────────────────────────
                    ui.label("Température initiale :");
                    ui.add(egui::Slider::new(&mut self.t_initial, 1.0..=1000.0).logarithmic(true));
                    ui.label("Température finale :");
                    ui.add(egui::Slider::new(&mut self.t_final, 1e-8..=0.1).logarithmic(true));
                    ui.label("Coefficient α (refroidissement) :");
                    ui.add(egui::Slider::new(&mut self.alpha, 0.990..=0.99999).step_by(0.00001));
                    ui.label("Amplitude du pas :");
                    ui.add(egui::Slider::new(&mut self.pas, 0.01..=2.0));

                    ui.separator();
                    // ── Mode pédagogique ───────────────────────────────────
                    ui.heading("🎓 Mode pédagogique");

                    ui.checkbox(&mut self.mode_pas_a_pas, "Mode pas-à-pas");
                    if self.mode_pas_a_pas {
                        self.sa.en_cours = false;
                    }

                    ui.label("Vitesse (mode lent) :");
                    ui.add(
                        egui::Slider::new(&mut self.delai_ms, 0_u64..=2000)
                            .suffix(" ms / étape")
                            .logarithmic(false),
                    );
                    if self.delai_ms > 0 && !self.mode_pas_a_pas {
                        ui.label(format!("→ ~{:.1} étapes/s", 1000.0 / self.delai_ms as f64));
                    }
                    ui.add(
                        egui::Slider::new(&mut self.sa.etapes_par_frame, 1..=500)
                            .text("étapes / frame (mode rapide)"),
                    );

                    ui.separator();
                    // ── Contrôles ──────────────────────────────────────────
                    ui.horizontal(|ui| {
                        if self.mode_pas_a_pas {
                            if ui.button("⏭ Pas suivant").clicked() && !self.sa.fini {
                                self.sa.etape(&self.fonction);
                                ctx.request_repaint();
                            }
                        } else {
                            let label = if self.sa.en_cours {
                                "⏸ Pause"
                            } else {
                                "▶ Démarrer"
                            };
                            if ui.button(label).clicked() && !self.sa.fini {
                                self.sa.en_cours = !self.sa.en_cours;
                                if self.sa.en_cours {
                                    ctx.request_repaint();
                                }
                            }
                        }
                        if ui.button("↺ Reset").clicked() {
                            self.appliquer_params_au_sa();
                            self.sa.reset(&self.fonction);
                        }
                    });
                    if self.sa.fini {
                        ui.colored_label(Color32::YELLOW, "✔ Convergé !");
                    }

                    ui.separator();
                    // ── État courant ───────────────────────────────────────
                    ui.heading("📊 État");
                    ui.label(format!("Température : {:.2e}", self.sa.temperature));
                    ui.label(format!("Énergie courante : {:.5}", self.sa.energie));
                    ui.label(format!(
                        "Meilleure énergie : {:.5}",
                        self.sa.meilleure_energie
                    ));
                    if self.fonction.est_1d() {
                        ui.label(format!("Meilleur x : {:.4}", self.sa.meilleur_x));
                        let (gx, _, ge) = self.fonction.minimum_global();
                        ui.label(format!("Minimum global ≈ x={gx:.4}, f={ge:.4}"));
                    } else {
                        ui.label(format!(
                            "Meilleur point : ({:.3}, {:.3})",
                            self.sa.meilleur_x, self.sa.meilleur_y
                        ));
                        let (gx, gy, ge) = self.fonction.minimum_global();
                        ui.label(format!("Minimum global : ({gx:.2}, {gy:.2}) = {ge:.2}"));
                    }
                    ui.label(format!("Étapes totales : {}", self.sa.total_etapes));
                    let tot = (self.sa.refus + self.sa.acceptations).max(1) as f64;
                    ui.label(format!(
                        "Taux d'acceptation : {:.1}%",
                        self.sa.acceptations as f64 / tot * 100.0
                    ));

                    ui.separator();
                    ui.label("Refroidissement :");
                    let prog = {
                        let log_r = (self.sa.t_initial / self.sa.t_final.max(1e-15)).ln() as f32;
                        let log_c = (self.sa.temperature / self.sa.t_final.max(1e-15))
                            .max(1.0)
                            .ln() as f32;
                        (1.0 - log_c / log_r.max(1e-6)).clamp(0.0, 1.0)
                    };
                    ui.add(egui::ProgressBar::new(prog).text(format!("{:.0}%", prog * 100.0)));

                    // Légende couleur (uniquement pour les 2D)
                    if !self.fonction.est_1d() {
                        ui.separator();
                        ui.label("Légende couleur :");
                        let (_, re) = ui.allocate_space(Vec2::new(240.0, 16.0));
                        let painter = ui.painter_at(re);
                        let n = 100usize;
                        for i in 0..n {
                            let t = i as f32 / n as f32;
                            let t1 = (i + 1) as f32 / n as f32;
                            let x0 = re.min.x + t * re.width();
                            let x1 = re.min.x + t1 * re.width();
                            painter.rect_filled(
                                Rect::from_min_max(
                                    Pos2::new(x0, re.min.y),
                                    Pos2::new(x1, re.max.y),
                                ),
                                0.0,
                                viridis(t),
                            );
                        }
                        ui.horizontal(|ui| {
                            ui.label("min");
                            ui.with_layout(
                                egui::Layout::right_to_left(egui::Align::Center),
                                |ui| {
                                    ui.label("max");
                                },
                            );
                        });
                    }

                    // ── Panneau d'explication (mode pédagogique) ───────────
                    if let Some(dec) = self.sa.derniere_decision.clone() {
                        ui.separator();
                        ui.heading("🔍 Dernière décision");
                        afficher_explication(ui, &dec, self.fonction.est_1d());
                    }
                });
            });

        // ── Zone centrale ──────────────────────────────────────────────────
        egui::CentralPanel::default().show(ctx, |ui| {
            ui.heading(format!(
                "Recuit simulé — {}   (T = {:.2e})",
                self.fonction.nom(),
                self.sa.temperature
            ));
            ui.separator();

            let available = ui.available_size();

            if self.fonction.est_1d() {
                // ── Visualisation 1D ──────────────────────────────────────
                let plot_h = (available.y - 80.0).max(120.0);
                let plot_w = (available.x - 20.0).max(200.0);
                let (plot_rect, _) =
                    ui.allocate_exact_size(Vec2::new(plot_w, plot_h), egui::Sense::hover());
                dessiner_1d(
                    &self.fonction,
                    &self.sa,
                    &ui.painter_at(plot_rect),
                    plot_rect,
                );

                ui.add_space(6.0);
                ui.horizontal(|ui| {
                    ui.colored_label(Color32::from_rgb(255, 70, 70), "● Position courante  ");
                    ui.colored_label(Color32::from_rgb(0, 230, 230), "● Meilleure solution  ");
                    ui.colored_label(Color32::WHITE, "✚ Minimum global  ");
                    ui.colored_label(Color32::from_rgb(255, 160, 0), "● Candidat (dernier)");
                });
            } else {
                // ── Visualisation 2D (heatmap) ────────────────────────────
                let side = (available.x.min(available.y) - 40.0).max(100.0);
                let (map_rect, _) = ui.allocate_exact_size(Vec2::splat(side), egui::Sense::hover());

                let texture = self.obtenir_texture(ctx);
                ui.painter().image(
                    texture.id(),
                    map_rect,
                    Rect::from_min_max(Pos2::ZERO, Pos2::new(1.0, 1.0)),
                    Color32::WHITE,
                );

                let painter = ui.painter_at(map_rect);
                let d = self.fonction.domaine();

                // Trajectoire
                let tl = self.sa.trail.len();
                if tl > 1 {
                    for i in 1..tl {
                        let alpha = ((i as f32 / tl as f32) * 200.0) as u8;
                        let col = Color32::from_rgba_unmultiplied(255, 220, 50, alpha);
                        let p0 = espace_vers_ecran(
                            self.sa.trail[i - 1][0],
                            self.sa.trail[i - 1][1],
                            &d,
                            map_rect,
                        );
                        let p1 = espace_vers_ecran(
                            self.sa.trail[i][0],
                            self.sa.trail[i][1],
                            &d,
                            map_rect,
                        );
                        painter.line_segment([p0, p1], Stroke::new(1.5, col));
                    }
                }

                // Candidat (mode pédagogique)
                if let Some(dec) = &self.sa.derniere_decision {
                    let cp = espace_vers_ecran(dec.x_candidat, dec.y_candidat, &d, map_rect);
                    painter.circle_filled(cp, 5.0, Color32::from_rgb(255, 160, 0));
                    painter.circle_stroke(cp, 5.0, Stroke::new(1.5, Color32::WHITE));
                }

                // Minimum global (croix blanche)
                let (gx, gy, _) = self.fonction.minimum_global();
                let gp = espace_vers_ecran(gx, gy, &d, map_rect);
                let cs = 9.0f32;
                for &(ddx, ddy) in &[(-cs, 0.0f32), (cs, 0.0), (0.0, -cs), (0.0, cs)] {
                    painter.line_segment(
                        [gp, Pos2::new(gp.x + ddx, gp.y + ddy)],
                        Stroke::new(2.5, Color32::WHITE),
                    );
                }
                // Meilleur point (cyan)
                let bp = espace_vers_ecran(self.sa.meilleur_x, self.sa.meilleur_y, &d, map_rect);
                painter.circle_filled(bp, 6.0, Color32::from_rgb(0, 230, 230));
                painter.circle_stroke(bp, 6.0, Stroke::new(2.0, Color32::WHITE));
                // Point courant (rouge)
                let cp = espace_vers_ecran(self.sa.x, self.sa.y, &d, map_rect);
                painter.circle_filled(cp, 5.0, Color32::from_rgb(255, 70, 70));
                painter.circle_stroke(cp, 5.0, Stroke::new(1.5, Color32::WHITE));

                ui.add_space(6.0);
                ui.horizontal(|ui| {
                    ui.colored_label(Color32::from_rgb(255, 70, 70), "● Position courante  ");
                    ui.colored_label(Color32::from_rgb(0, 230, 230), "● Meilleure solution  ");
                    ui.colored_label(Color32::WHITE, "✚ Minimum global  ");
                    ui.colored_label(Color32::from_rgb(255, 160, 0), "● Candidat (dernier)");
                });
            }
        });
    }
}

// ──────────────────────────────────────────────────────────────────────────────
fn main() -> eframe::Result<()> {
    let options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default()
            .with_title("Recuit Simulé")
            .with_inner_size([1100.0, 720.0]),
        ..Default::default()
    };
    eframe::run_native(
        "Recuit Simulé",
        options,
        Box::new(|_cc| Ok(Box::new(App::new()))),
    )
}
