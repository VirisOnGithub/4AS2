pub struct Matrix<A> {
    pub data: Vec<Vec<A>>,
}

impl<A: Copy> Matrix<A> {
    pub fn new(data: Vec<Vec<A>>) -> Self {
        Matrix { data }
    }

    pub fn transpose(&self) -> Matrix<A> {
        let transposed_data = (0..self.data[0].len())
            .map(|i| self.data.iter().map(|row| row[i]).collect())
            .collect();
        Matrix::new(transposed_data)
    }

    pub fn multiply(&self, other: &Matrix<A>) -> Matrix<A>
    where
        A: std::ops::Mul<Output = A> + std::ops::Add<Output = A> + Default + Copy,
    {
        let mut result = vec![vec![A::default(); other.data[0].len()]; self.data.len()];
        for i in 0..self.data.len() {
            for j in 0..other.data[0].len() {
                for k in 0..self.data[0].len() {
                    result[i][j] = result[i][j] + self.data[i][k] * other.data[k][j];
                }
            }
        }
        Matrix::new(result)
    }

    pub fn add(&self, other: &Matrix<A>) -> Matrix<A>
    where
        A: std::ops::Add<Output = A> + Copy,
    {
        let result = self
            .data
            .iter()
            .zip(other.data.iter())
            .map(|(row1, row2)| {
                row1.iter()
                    .zip(row2.iter())
                    .map(|(item1, item2)| *item1 + *item2)
                    .collect()
            })
            .collect();
        Matrix::new(result)
    }
}

impl<A: std::fmt::Display> std::fmt::Display for Matrix<A> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        let max_len = self
            .data
            .iter()
            .flat_map(|row| row.iter())
            .map(|item| format!("{}", item).len())
            .max()
            .unwrap_or(0);
        write!(
            f,
            "+{}+\n",
            "-".repeat((max_len + 1) * self.data[0].len() + 1)
        )?;
        for row in &self.data {
            write!(f, "| ")?;
            for item in row {
                write!(f, "{:>width$} ", item, width = max_len)?;
            }
            writeln!(f, "|")?;
        }
        write!(
            f,
            "+{}+",
            "-".repeat((max_len + 1) * self.data[0].len() + 1)
        )?;
        Ok(())
    }
}
