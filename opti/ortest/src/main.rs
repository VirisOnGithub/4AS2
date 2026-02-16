mod flpb_parser;
mod gui;
mod test;
use crate::test::demo;
use good_lp::{Solution, SolverModel, constraint, default_solver, solvers, variable, variables};
use std::error::Error;

// fn main() -> Result<(), Box<dyn Error>> {
//     variables! {
//         vars:
//                a <= 1;
//           2 <= b <= 4;
//     } // variables can also be added dynamically with ProblemVariables::add
//     let solution = vars
//         .maximise(10 * (a - b / 5) - b)
//         .using(default_solver) // IBM's coin_cbc by default
//         .with(constraint!(a + 2 <= b))
//         .with(constraint!(1 + a >= 4 - b)) // .with_all(iter) is also available
//         .solve()?;
//     println!("a={}   b={}", solution.value(a), solution.value(b));
//     println!("a + b = {}", solution.eval(a + b));
//     Ok(())
// }

fn vrouuum_kachoooooowwww() -> Result<(), Box<dyn Error>> {
    variables! {
        vars:
            0 <= x (integer)<= 1000000;
            0 <= y (integer)<= 1000000;
    }

    let solution = vars
        .maximise(5000 * x + 4500 * y)
        .using(default_solver)
        .with(constraint!(6 * x + 5 * y <= 6000))
        .with(constraint!(x + 2 * y <= 1500))
        .with(constraint!(x <= 800))
        .solve()?;
    println!("x={}, y={}", solution.value(x), solution.value(y));
    Ok(())
}

fn je_suis_pauvre() -> Result<(), Box<dyn Error>> {
    let n = 5;
    variables! {
        vars:
            0 <= x[n] (integer) <= 1;
    }
    let c = [210, 90, 300, 30, 70];
    let solution = vars
        .maximise((0..n).map(|i| c[i] * x[i]).sum::<good_lp::Expression>())
        .using(default_solver)
        .with(constraint!(
            75 * x[0] + 43 * x[1] + 56 * x[2] + 12 * x[3] + 31 * x[4] <= 100
        ))
        .solve()?;
    println!(
        "x1={}, x2={}, x3={}, x4={}, x5={}",
        solution.value(x[0]),
        solution.value(x[1]),
        solution.value(x[2]),
        solution.value(x[3]),
        solution.value(x[4]),
    );
    Ok(())
}

fn couper_decaler_couper_decaler() -> Result<(), Box<dyn Error>> {
    let n = 15;
    variables! {
        vars:
            0 <= x[n] (integer)<= 1000000;
    }

    let solution = vars
        .minimise(
            150 * x[0]
                + 150 * x[1]
                + 150 * x[2]
                + 150 * x[3]
                + 150 * x[4]
                + 150 * x[5]
                + 200 * x[6]
                + 200 * x[7]
                + 200 * x[8]
                + 200 * x[9]
                + 200 * x[10]
                + 200 * x[11]
                + 200 * x[12]
                + 200 * x[13]
                + 200 * x[14],
        )
        .using(default_solver)
        .with(constraint!(
            2 * x[2]
                + 2 * x[4]
                + 3 * x[5]
                + x[8]
                + 3 * x[9]
                + 5 * x[11]
                + x[12]
                + 2 * x[13]
                + 3 * x[14]
                >= 432
        ))
        .with(constraint!(
            x[1] + 2 * x[3] + x[4] + x[6] + 2 * x[7] + 3 * x[10] + x[12] + 2 * x[13] + x[14] >= 500
        ))
        .with(constraint!(
            2 * x[0] + x[1] + x[2] + 2 * x[6] + x[7] + 2 * x[8] + x[9] + x[12] >= 400
        ))
        .solve()?;
    println!(
        "x1={}, x2={}, x3={}, x4={}, x5={}, x6={}, x7={}, x8={}, x9={}, x10={}, x11={}, x12={}, x13={}, x14={}, x15={}",
        solution.value(x[0]),
        solution.value(x[1]),
        solution.value(x[2]),
        solution.value(x[3]),
        solution.value(x[4]),
        solution.value(x[5]),
        solution.value(x[6]),
        solution.value(x[7]),
        solution.value(x[8]),
        solution.value(x[9]),
        solution.value(x[10]),
        solution.value(x[11]),
        solution.value(x[12]),
        solution.value(x[13]),
        solution.value(x[14]),
    );
    Ok(())
}

fn casse_pas_les_couilles_et_conduis() -> Result<Vec<usize>, Box<dyn Error>> {
    let data = demo()?;
    // Soit C un ensemble de n communes pouvant êtres des sites d'installations potentielles.
    // Ouvrir un site j coûte $f_j$ et servir une commune i à partir du site j coûte $c_{i,j}$.
    // Chaque commune doit être servie par un site ouvert.
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
        .with_all(
            (0..n).map(|i| {
                constraint!((0..m).map(|j| y[i * m + j]).sum::<good_lp::Expression>() == 1)
            }),
        )
        .with_all((0..n).flat_map(|i| {
            (0..m).map({
                let yclone = y.clone();
                let xclone = x.clone();
                move |j| constraint!(yclone[i * m + j] <= xclone[j])
            })
        }))
        .solve()?;

    let mut opened_sites = vec![];
    for (j, var) in x.iter().enumerate().take(m) {
        if solution.value(*var) > 0.5 {
            opened_sites.push(j);
        }
    }
    Ok(opened_sites)
}

pub(crate) fn build_cost_matrix(
    data: &flpb_parser::FlpbInstance,
) -> Result<Vec<Vec<f64>>, Box<dyn Error>> {
    let n = data.customers.len();
    let m = data.facilities.len();
    let mut matrix = vec![vec![0.0; m]; n];

    for cost in &data.costs {
        if cost.customer_id == 0 || cost.customer_id > n {
            return Err(format!("customer id out of range: {}", cost.customer_id).into());
        }
        if cost.facility_id == 0 || cost.facility_id > m {
            return Err(format!("facility id out of range: {}", cost.facility_id).into());
        }
        let i = cost.customer_id - 1;
        let j = cost.facility_id - 1;
        matrix[i][j] = cost.cost;
    }

    Ok(matrix)
}

fn petits_k() -> Result<(), Box<dyn Error>> {
    println!("VROUUUUUM");
    vrouuum_kachoooooowwww()?;

    println!("NOUS SOMMES PAUVRES");
    je_suis_pauvre()?;

    println!("JE COUPE ET JE DECALE");
    couper_decaler_couper_decaler()?;

    println!("JE CASSE LES BURNES ET JE DRIVE");
    let opened_sites = casse_pas_les_couilles_et_conduis()?;
    println!("Sites ouverts: {:?}", opened_sites);
    Ok(())
}

fn main() -> Result<(), Box<dyn Error>> {
    gui::run_gui()?;

    // Posons $x_j = 1$ si le site j est ouvert, $0$ sinon.
    // Posons $y_{i,j} = 1$ si la commune i est servie par le site j, $0$ sinon.
    // Il faut minimiser la fonction objectif: $z = \sum_{j=1}^{m} f_j x_j + \sum_{i=1}^{n} \sum_{j=1}^{m} c_{i,j} y_{i,j}$
    // Sous les contraintes:
    // $$
    // \begin{cases}
    // \sum_{j=1}^{m} y_{i,j} = 1 \quad \forall i \in C \\
    // y_{i,j} \leq x_j \quad \forall i \in C, j \in C \\
    // x_j \in \{0,1\} \quad \forall j \in F \\
    // y_{i,j} \in \{0,1\} \quad \forall i \in C, j \in C
    // \end{cases}
    // $$

    // let data = demo()?;
    Ok(())
}
