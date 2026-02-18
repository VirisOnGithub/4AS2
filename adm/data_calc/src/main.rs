use crate::fun::*;
use crate::matrix::*;

mod fun;
mod matrix;

fn main() {
    let data = DataCalc::new(vec![
        DataRow::new(3, 0, 1),
        DataRow::new(0, 1, 1),
        DataRow::new(1, 1, 1),
        DataRow::new(0, 2, 1),
        DataRow::new(4, 2, 2),
        DataRow::new(2, 3, 2),
        DataRow::new(4, 3, 2),
        DataRow::new(2, 4, 2),
    ]);

    let a1 = data.extract_from_group(1);
    let a2 = data.extract_from_group(2);

    println!("Exercise 1");

    println!("1. Gravity center:");
    println!("\tg(E)  = {:?}", data.gravity_center());
    println!("\tg(A1) = {:?}", a1.gravity_center());
    println!("\tg(A2) = {:?}", a2.gravity_center());
    let g = Matrix::new(vec![
        vec![a1.gravity_center().0, a1.gravity_center().1],
        vec![a2.gravity_center().0, a2.gravity_center().1],
    ]);
    println!("Gravity Matrix:");
    println!("G = \n{}", g);

    println!("2. Centered Matrix:");
    println!("hatX = \n{}", data.centered_matrix());
    println!("hatY = \n{}", a1.centered_matrix());
    println!("hatZ = \n{}", a2.centered_matrix());

    println!("3. Total inertia matrix:");
    println!("I = \n{}", data.total_inertia_matrix());

    println!("4. Intra-group inertia:");
    println!("I1 = \n{}", a1.total_inertia_matrix());
    println!("I2 = \n{}", a2.total_inertia_matrix());

    let w = a1.total_inertia_matrix().add(&a2.total_inertia_matrix());
    println!("W = I1 + I2 = \n{}", w);

    println!("Inter-group inertia:");
    println!("B = \n{}", data.inter_group_inertia_matrix());

    println!("5. Total inertia along (1, 1)");
    println!("I(1, 1) = {}", data.total_inertia_along((1.0, 1.0)));

    println!("6. Discriminating power:");
    println!("p = {:.2}", data.discriminating_power((1.0, 1.0)));

    println!("7. Inertia following (1, -1)");
    let direction = (1.0, -1.0);
    println!("I_b = {}", data.inertia_following(direction));

    println!("Exercise 2");

    let mat = Matrix::new(vec![
        vec![1.0, 0.0, -1.0],
        vec![0.0, 1.0, -1.0],
        vec![-1.0, 1.0, 0.0],
        vec![0.0, -1.0, 1.0],
        vec![-1.0, 0.0, 1.0],
        vec![1.0, -1.0, 0.0],
    ]);

    let v = e2q1(&mat);

    println!("1. V = 1/6*\n{}", v.scalar_mul(6.0));
}
