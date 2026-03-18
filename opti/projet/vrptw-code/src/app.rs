use crate::{
    parser::InputData,
    problem::{Problem, Solution},
};
use egui::{Color32, Widget};
use std::path::PathBuf;

pub struct VrpApp {
    pub files: Vec<PathBuf>,
    pub selected_file_id: usize,
    pub time_into_account: bool,
    pub buffer: String,
    pub problem: Option<Problem>,
    pub solution: Option<Solution>,
}

impl Default for VrpApp {
    fn default() -> Self {
        Self::new()
    }
}

impl VrpApp {
    pub fn new() -> Self {
        let data_dir = "data";
        let files = std::fs::read_dir(data_dir)
            .into_iter()
            .flatten()
            .flatten()
            .map(|file| file.path())
            .filter(|file| file.extension().map(|e| e == "vrp").unwrap_or(false))
            .collect();
        Self {
            files,
            selected_file_id: 0,
            time_into_account: false,
            buffer: String::new(),
            problem: None,
            solution: None,
        }
    }

    fn load_file(&mut self, selected_file: PathBuf) -> InputData {
        let file_contents = std::fs::read_to_string(selected_file);
        InputData::parse_input(file_contents.expect("IO error for file").as_str())
    }
}

impl eframe::App for VrpApp {
    fn update(&mut self, ctx: &eframe::egui::Context, _frame: &mut eframe::Frame) {
        egui::SidePanel::left("controls")
            .min_width(200.0)
            .show(ctx, |ui| {
                ui.heading("VRPTW Solver");
                egui::Checkbox::new(&mut self.time_into_account, "Temps pris en compte").ui(ui);
                ui.separator();

                // sélecteur
                let selected_text = self.files[self.selected_file_id]
                    .to_string_lossy()
                    .into_owned();
                egui::ComboBox::new("file_selector", "Select a file")
                    .selected_text(selected_text)
                    .show_ui(ui, |ui| {
                        for (i, file) in self.files.iter().enumerate() {
                            ui.selectable_value(
                                &mut self.selected_file_id,
                                i,
                                file.display().to_string(),
                            );
                        }
                    });
                if ui.button("Charger").clicked() {
                    let selected_file = self.files[self.selected_file_id].clone();
                    let input_data = self.load_file(selected_file);
                    self.buffer = format!("{:#?}", input_data);
                    let problem = Problem::new(input_data);
                    self.problem = Some(problem.clone());
                }
                if ui.button("Effacer").clicked() {
                    self.buffer.clear();
                    self.problem = None;
                    self.solution = None;
                }
                ui.add_enabled_ui(self.problem.is_some(), |ui| {
                    if ui.button("Résoudre").clicked() {
                        let solution = Solution::random(
                            &self
                                .problem
                                .clone()
                                .expect("No problem was submitted before solving"),
                        );
                        self.solution = Some(solution.clone());
                        self.buffer = format!("{:#?}", solution);
                    }
                })
            });

        egui::CentralPanel::default().show(ctx, |ui| {
            let Some(_) = &self.problem else {
                ui.centered_and_justified(|ui| {
                    ui.label(
                        egui::RichText::new("Chargez un fichier .vrp pour commencer")
                            .size(18.0)
                            .color(Color32::GRAY),
                    );
                });
                return;
            };
            if !self.buffer.is_empty() {
                egui::ScrollArea::vertical()
                    .auto_shrink([false, false])
                    .show(ui, |ui| {
                        ui.label(self.buffer.clone());
                    });
            }
        });
    }
}
