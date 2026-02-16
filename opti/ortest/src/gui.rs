use crate::build_cost_matrix;
use crate::flpb_parser;
use crate::test::demo;
use eframe::egui::{self, Color32, Pos2, Rect, Sense, Stroke};
use good_lp::Solution;
use good_lp::{SolverModel, constraint, solvers, variable, variables};
use std::error::Error;

#[derive(Clone)]
struct GuiData {
    data: flpb_parser::FlpbInstance,
    opened_sites: Vec<usize>,
    assignments: Vec<Option<usize>>,
    bounds: Bounds,
}

#[derive(Clone, Copy)]
struct Bounds {
    min_x: f64,
    max_x: f64,
    min_y: f64,
    max_y: f64,
}

impl Bounds {
    fn expand_with(&mut self, x: f64, y: f64) {
        self.min_x = self.min_x.min(x);
        self.max_x = self.max_x.max(x);
        self.min_y = self.min_y.min(y);
        self.max_y = self.max_y.max(y);
    }
}

fn compute_bounds(data: &flpb_parser::FlpbInstance) -> Bounds {
    let mut bounds = Bounds {
        min_x: f64::INFINITY,
        max_x: f64::NEG_INFINITY,
        min_y: f64::INFINITY,
        max_y: f64::NEG_INFINITY,
    };

    for c in &data.customers {
        bounds.expand_with(c.x, c.y);
    }
    for f in &data.facilities {
        bounds.expand_with(f.x, f.y);
    }

    if !bounds.min_x.is_finite() {
        bounds = Bounds {
            min_x: 0.0,
            max_x: 1.0,
            min_y: 0.0,
            max_y: 1.0,
        };
    }

    if (bounds.max_x - bounds.min_x).abs() < f64::EPSILON {
        bounds.max_x += 1.0;
        bounds.min_x -= 1.0;
    }
    if (bounds.max_y - bounds.min_y).abs() < f64::EPSILON {
        bounds.max_y += 1.0;
        bounds.min_y -= 1.0;
    }

    bounds
}

fn compute_cost_matrix_or_distance(data: &flpb_parser::FlpbInstance) -> Option<Vec<Vec<f64>>> {
    if data.costs.is_empty() {
        return None;
    }
    build_cost_matrix(data).ok()
}

fn compute_assignments(
    data: &flpb_parser::FlpbInstance,
    opened_sites: &[usize],
    costs: Option<&Vec<Vec<f64>>>,
) -> Vec<Option<usize>> {
    if opened_sites.is_empty() {
        return vec![None; data.customers.len()];
    }

    let mut assignments = Vec::with_capacity(data.customers.len());
    for (i, customer) in data.customers.iter().enumerate() {
        let mut best_site = None;
        let mut best_cost = f64::INFINITY;
        for &j in opened_sites {
            let cost = if let Some(matrix) = costs {
                matrix[i][j]
            } else {
                let facility = &data.facilities[j];
                let dx = customer.x - facility.x;
                let dy = customer.y - facility.y;
                (dx * dx + dy * dy).sqrt()
            };
            if cost < best_cost {
                best_cost = cost;
                best_site = Some(j);
            }
        }
        assignments.push(best_site);
    }
    assignments
}

fn build_gui_data() -> Result<GuiData, Box<dyn Error>> {
    let data = demo()?;
    let opened_sites = {
        let n = data.customers.len();
        let m = data.facilities.len();
        let costs = build_cost_matrix(&data)?;

        let mut vars = variables!();
        let x = vars.add_vector(variable().binary(), m);
        let y = vars.add_vector(variable().binary(), n * m);

        let solution = vars
            .minimise(
                (0..m)
                    .map(|j| data.facilities[j].cost * x[j])
                    .sum::<good_lp::Expression>()
                    + (0..n)
                        .map(|i| {
                            (0..m)
                                .map(|j| costs[i][j] * y[i * m + j])
                                .sum::<good_lp::Expression>()
                        })
                        .sum::<good_lp::Expression>(),
            )
            .using(solvers::microlp::microlp)
            .with_all((0..n).map(|i| {
                constraint!((0..m).map(|j| y[i * m + j]).sum::<good_lp::Expression>() == 1)
            }))
            .with_all((0..n).flat_map(|i| {
                (0..m).map({
                    let yclone = y.clone();
                    let xclone = x.clone();
                    move |j| constraint!(yclone[i * m + j] <= xclone[j])
                })
            }))
            .solve()?;

        let mut opened = vec![];
        for (j, var) in x.iter().enumerate().take(m) {
            if solution.value(*var) > 0.5 {
                opened.push(j);
            }
        }
        opened
    };

    let costs = compute_cost_matrix_or_distance(&data);
    let assignments = compute_assignments(&data, &opened_sites, costs.as_ref());
    let bounds = compute_bounds(&data);

    Ok(GuiData {
        data,
        opened_sites,
        assignments,
        bounds,
    })
}

struct VisualApp {
    gui: GuiData,
}

impl VisualApp {
    fn new(gui: GuiData) -> Self {
        Self { gui }
    }
}

impl eframe::App for VisualApp {
    fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        egui::CentralPanel::default().show(ctx, |ui| {
            ui.heading("Casse pas les couilles et conduis");
            ui.label(format!(
                "Customers: {} | Facilities: {} | Sites ouverts: {}",
                self.gui.data.customers.len(),
                self.gui.data.facilities.len(),
                self.gui.opened_sites.len()
            ));

            let available = ui.available_size();
            let (response, painter) = ui.allocate_painter(available, Sense::hover());

            painter.rect_filled(response.rect, 0.0, Color32::from_gray(15));
            painter.rect_stroke(response.rect, 0.0, Stroke::new(1.0, Color32::from_gray(60)));

            let to_screen = |x: f64, y: f64, rect: Rect, bounds: Bounds| -> Pos2 {
                let margin = 20.0;
                let width = rect.width().max(1.0) - 2.0 * margin;
                let height = rect.height().max(1.0) - 2.0 * margin;
                let nx = ((x - bounds.min_x) / (bounds.max_x - bounds.min_x)).clamp(0.0, 1.0);
                let ny = ((y - bounds.min_y) / (bounds.max_y - bounds.min_y)).clamp(0.0, 1.0);
                let sx = rect.left() + margin + (nx as f32) * width;
                let sy = rect.bottom() - margin - (ny as f32) * height;
                Pos2::new(sx, sy)
            };

            for (i, customer) in self.gui.data.customers.iter().enumerate() {
                if let Some(site) = self.gui.assignments[i] {
                    let facility = &self.gui.data.facilities[site];
                    let p1 = to_screen(customer.x, customer.y, response.rect, self.gui.bounds);
                    let p2 = to_screen(facility.x, facility.y, response.rect, self.gui.bounds);
                    painter
                        .line_segment([p1, p2], Stroke::new(0.6, Color32::from_rgb(80, 170, 255)));
                }
            }

            for customer in &self.gui.data.customers {
                let p = to_screen(customer.x, customer.y, response.rect, self.gui.bounds);
                painter.circle_filled(p, 2.0, Color32::from_rgb(220, 220, 220));
            }

            for &site in &self.gui.opened_sites {
                let facility = &self.gui.data.facilities[site];
                let p = to_screen(facility.x, facility.y, response.rect, self.gui.bounds);
                painter.circle_filled(p, 5.0, Color32::from_rgb(255, 140, 0));
                painter.circle_stroke(p, 6.0, Stroke::new(1.0, Color32::WHITE));
            }

            ui.add_space(8.0);
            ui.horizontal(|ui| {
                ui.label("Légende:");
                ui.colored_label(Color32::from_rgb(220, 220, 220), "Customers");
                ui.colored_label(Color32::from_rgb(255, 140, 0), "Sites ouverts");
                ui.colored_label(Color32::from_rgb(80, 170, 255), "Affectations");
            });
        });
    }
}

pub fn run_gui() -> Result<(), Box<dyn Error>> {
    let gui = build_gui_data()?;
    let options = eframe::NativeOptions {
        ..Default::default()
    };
    eframe::run_native(
        "casse_pas_les_couilles_et_conduis",
        options,
        Box::new(|_cc| Box::new(VisualApp::new(gui))),
    )?;
    Ok(())
}
