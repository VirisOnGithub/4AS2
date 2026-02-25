use nalgebra::{Matrix3, Matrix6x3};

fn main() {
    let data = Matrix6x3::new(
        26.0, 36.0, 62.0, 23.0, 32.0, 48.0, 14.0, 25.0, 45.0, 33.0, 45.0, 52.0, 24.0, 34.0, 57.0,
        18.0, 30.0, 41.0,
    );

    println!("Matrix: \n{}", data);

    println!("1. Moyenne de chaque colonne:");
    let mean = data.row_mean();
    for i in 0..data.ncols() {
        println!("Colonne {}: {:.3}", i + 1, mean[i]);
    }

    println!("2. Variance de chaque colonne:");
    let variance = data.row_variance();
    for i in 0..data.ncols() {
        println!("Colonne {}: {:.3}", i + 1, variance[i]);
    }

    println!("3. Ecart type de chaque colonne:");
    let std_dev = data.row_variance().map(|v| (v as f64).sqrt());
    for i in 0..data.ncols() {
        println!("Colonne {}: {:.3}", i + 1, std_dev[i]);
    }

    println!("4. Données transformées");
    // for i in 0..data.ncols() {
    //     for j in 0..data.nrows() {
    //         let transformed_value =
    //             (data[(j, i)] - mean[i]) / (std_dev[i] * (data.nrows() as f64).sqrt());
    //         print!("{:.3} ", transformed_value);
    //     }
    //     println!();
    // }

    let mut transformed = Matrix6x3::zeros();
    for i in 0..data.ncols() {
        for j in 0..data.nrows() {
            transformed[(j, i)] =
                (data[(j, i)] - mean[i]) / (std_dev[i] * (data.nrows() as f64).sqrt());
        }
    }

    println!("{:.3}", transformed);

    println!("5. Matrice de dispersion:");

    // for i in 0..data.ncols() {
    //     for j in 0..data.ncols() {
    //         let covariance = (0..data.nrows())
    //             .map(|k| (data[(k, i)] - mean[i]) * (data[(k, j)] - mean[j]))
    //             .sum::<f64>()
    //             / (data.nrows() as f64);
    //         let corr = covariance / (std_dev[i] * std_dev[j]);
    //         print!("{:.3} ", corr);
    //     }
    // }
    let mut correlation_matrix = Matrix3::zeros();

    for i in 0..data.ncols() {
        for j in 0..data.ncols() {
            let covariance = (0..data.nrows())
                .map(|k| (data[(k, i)] - mean[i]) * (data[(k, j)] - mean[j]))
                .sum::<f64>()
                / (data.nrows() as f64);
            let corr = covariance / (std_dev[i] * std_dev[j]);
            correlation_matrix[(i, j)] = corr;
        }
    }

    println!("{:.3}", correlation_matrix);

    println!("6. Valeurs propres :");
    let eigen = correlation_matrix.eigenvalues().unwrap();
    let sorted_eigen: Vec<f64> = {
        let mut eigen_vec = eigen.iter().cloned().collect::<Vec<f64>>();
        eigen_vec.sort_by(|a, b| b.partial_cmp(a).unwrap());
        eigen_vec
    };
    let summed_eigen: f64 = sorted_eigen.iter().sum();
    let eigeinvalues_matrix = Matrix3::new(
        sorted_eigen[0],
        sorted_eigen[0],
        sorted_eigen[0] / summed_eigen,
        sorted_eigen[1],
        sorted_eigen[0] + sorted_eigen[1],
        (sorted_eigen[0] + sorted_eigen[1]) / summed_eigen,
        sorted_eigen[2],
        sorted_eigen[0] + sorted_eigen[1] + sorted_eigen[2],
        1.0,
    );

    println!("{:.5}", eigeinvalues_matrix);

    // for i in 0..eigen.len() {
    //     println!("Valeur propre {}: {:.3}", i + 1, eigen[i]);
    // }

    // println!("7. Vecteurs propres :");
    // let eigenvectors = correlation_matrix.eigenvectors();
    // for i in 0..eigenvectors.ncols() {
    //     println!("Vecteur propre {}: \n{}", i + 1, eigenvectors.column(i));
    // }
}
