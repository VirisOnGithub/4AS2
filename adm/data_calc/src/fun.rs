use crate::matrix::*;

#[derive(Clone, Copy)]
pub struct DataRow {
    pub p1: u32,
    pub p2: u32,
    pub grp: u8,
    pub mass: f32,
}

impl DataRow {
    // pub fn new(p1: u32, p2: u32, grp: u8, mass: f32) -> Self {
    //     DataRow { p1, p2, grp, mass }
    // }

    pub fn new(p1: u32, p2: u32, grp: u8) -> Self {
        DataRow {
            p1,
            p2,
            grp,
            mass: 1.0,
        }
    }
}

#[derive(Clone)]
pub struct DataCalc {
    pub data: Vec<DataRow>,
}

impl DataCalc {
    pub fn new(data: Vec<DataRow>) -> Self {
        DataCalc { data }
    }

    pub fn extract_from_group(&self, group: u8) -> DataCalc {
        DataCalc::new(
            self.data
                .iter()
                .filter(|r| r.grp == group)
                .cloned()
                .collect(),
        )
    }

    pub fn gravity_center(&self) -> (f32, f32) {
        let rows = self.data.iter();
        let (x_sum, y_sum, mass_sum) = rows.fold((0.0, 0.0, 0.0), |acc, row| {
            (
                acc.0 + row.p1 as f32 * row.mass,
                acc.1 + row.p2 as f32 * row.mass,
                acc.2 + row.mass,
            )
        });

        if mass_sum == 0.0 {
            return (0.0, 0.0);
        }

        (x_sum / mass_sum, y_sum / mass_sum)
    }

    pub fn centered_matrix(&self) -> Matrix<f32> {
        let gravity_center = self.gravity_center();
        Matrix::new(
            self.data
                .iter()
                .map(|r| {
                    vec![
                        r.p1 as f32 - gravity_center.0,
                        r.p2 as f32 - gravity_center.1,
                    ]
                })
                .collect(),
        )
    }

    fn diagonal_mass_matrix(&self) -> Matrix<f32> {
        let size = self.data.len();
        let mut diagonal = vec![vec![0.0; size]; size];

        for (index, row) in self.data.iter().enumerate() {
            diagonal[index][index] = row.mass;
        }

        Matrix::new(diagonal)
    }

    pub fn total_inertia_matrix(&self) -> Matrix<f32> {
        let centered = self.centered_matrix();
        let diagonal_mass_matrix = self.diagonal_mass_matrix();

        // println!("Transposed Centered Matrix:");
        // println!("hatX^T = \n{}", centered.transpose());
        // println!("Diagonal Mass Matrix:");
        // println!("M = \n{}", self.diagonal_mass_matrix());
        // println!("Centered Matrix:");
        // println!("hatX = \n{}", self.centered_matrix());

        centered
            .transpose()
            .multiply(&diagonal_mass_matrix)
            .multiply(&centered)
    }

    pub fn inter_group_inertia_matrix(&self) -> Matrix<f32> {
        let a1 = self.extract_from_group(1);
        let a2 = self.extract_from_group(2);

        let gravity_center: Vec<f32> = vec![self.gravity_center().0, self.gravity_center().1];

        let g = Matrix::new(vec![
            vec![a1.gravity_center().0, a1.gravity_center().1],
            vec![a2.gravity_center().0, a2.gravity_center().1],
        ]);

        let hat_g: Matrix<f32> = Matrix::new(
            g.data
                .iter()
                .map(|row| {
                    row.iter()
                        .enumerate()
                        .map(|(i, &val)| val - gravity_center[i])
                        .collect()
                })
                .collect(),
        );

        let d_g = Matrix::new(vec![
            vec![a1.data.iter().map(|r| r.mass).sum(), 0.0],
            vec![0.0, a2.data.iter().map(|r| r.mass).sum()],
        ]);
        hat_g.transpose().multiply(&d_g).multiply(&hat_g)
    }

    fn normalize_vector(&self, vector: (f32, f32)) -> (f32, f32) {
        let magnitude = (vector.0.powi(2) + vector.1.powi(2)).sqrt();
        if magnitude == 0.0 {
            return (0.0, 0.0);
        }
        (vector.0 / magnitude, vector.1 / magnitude)
    }

    pub fn total_inertia_along(&self, direction: (f32, f32)) -> f32 {
        let direction = self.normalize_vector(direction);
        let vector = Matrix::new(vec![vec![direction.0], vec![direction.1]]);
        let total_inertia_matrix = vector
            .transpose()
            .multiply(&self.total_inertia_matrix())
            .multiply(&vector);
        total_inertia_matrix.data[0][0]
    }

    pub fn discriminating_power(&self, direction: (f32, f32)) -> f32 {
        let total_inertia = self.total_inertia_along(direction);
        let direction = self.normalize_vector(direction);
        let vector = Matrix::new(vec![vec![direction.0], vec![direction.1]]);
        let inter_group_inertia = vector
            .transpose()
            .multiply(&self.inter_group_inertia_matrix())
            .multiply(&vector);
        inter_group_inertia.data[0][0] / total_inertia
    }

    pub fn inertia_following(&self, direction: (f32, f32)) -> f32 {
        self.data
            .iter()
            .map(|r| r.mass * sq_distance(direction, (r.p1 as f32, r.p2 as f32)))
            .sum()
    }
}

fn sq_distance(vector: (f32, f32), point: (f32, f32)) -> f32 {
    (vector.0 - point.0).powi(2) + (vector.1 - point.1).powi(2)
}

pub fn e2q1(matrix: &Matrix<f32>) -> Matrix<f32> {
    let mat_len = matrix.data.len();
    matrix
        .data
        .iter()
        .map(|r| {
            let r_vec = Matrix::new(vec![r.clone()]);
            r_vec
                .transpose()
                .multiply(&r_vec)
                .scalar_mul(1.0 / mat_len as f32)
        })
        .fold(
            Matrix::new(vec![vec![0.0; matrix.data[0].len()]; matrix.data[0].len()]),
            |acc, m| acc.add(&m),
        )
}
