use std::error::Error;
use std::fmt;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::path::Path;

#[derive(Debug, Clone)]
pub struct Facility {
    pub capacity: f64,
    pub cost: f64,
    pub x: f64,
    pub y: f64,
}

#[derive(Debug, Clone)]
pub struct Customer {
    pub demand: f64,
    pub x: f64,
    pub y: f64,
}

#[derive(Debug, Clone)]
pub struct Cost {
    pub customer_id: usize,
    pub facility_id: usize,
    pub cost: f64,
}

#[derive(Debug, Clone)]
pub struct FlpbInstance {
    pub name: String,
    pub comment: Option<String>,
    pub problem: Option<String>,
    pub nb_facilities: usize,
    pub nb_customers: usize,
    pub coordinates: bool,
    pub facilities: Vec<Facility>,
    pub customers: Vec<Customer>,
    pub costs: Vec<Cost>,
}

#[derive(Debug, Clone)]
pub struct ParseFlpbError {
    pub line: usize,
    pub message: String,
}

impl fmt::Display for ParseFlpbError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "line {}: {}", self.line, self.message)
    }
}

impl Error for ParseFlpbError {}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum Section {
    Header,
    Facilities,
    Customers,
    Costs,
}

pub fn parse_flpb_file<P: AsRef<Path>>(path: P) -> Result<FlpbInstance, ParseFlpbError> {
    let file = File::open(path.as_ref()).map_err(|err| ParseFlpbError {
        line: 0,
        message: format!("failed to open file: {}", err),
    })?;
    let reader = BufReader::new(file);

    let mut section = Section::Header;

    let mut name: Option<String> = None;
    let mut comment: Option<String> = None;
    let mut problem: Option<String> = None;
    let mut nb_facilities: Option<usize> = None;
    let mut nb_customers: Option<usize> = None;
    let mut coordinates: Option<bool> = None;

    let mut facilities: Vec<Facility> = Vec::new();
    let mut customers: Vec<Customer> = Vec::new();
    let mut costs: Vec<Cost> = Vec::new();

    for (idx, line) in reader.lines().enumerate() {
        let line_no = idx + 1;
        let line = line.map_err(|err| ParseFlpbError {
            line: line_no,
            message: format!("failed to read line: {}", err),
        })?;
        let trimmed = line.trim();
        if trimmed.is_empty() {
            continue;
        }

        if trimmed.starts_with("FACILITIES ") {
            section = Section::Facilities;
            continue;
        }
        if trimmed.starts_with("CUSTOMERS ") {
            section = Section::Customers;
            continue;
        }
        if trimmed.starts_with("COSTS ") {
            section = Section::Costs;
            continue;
        }

        match section {
            Section::Header => {
                let (key, value) = trimmed.split_once(':').ok_or(ParseFlpbError {
                    line: line_no,
                    message: "expected header key: value".to_string(),
                })?;
                let key = key.trim();
                let value = value.trim();
                match key {
                    "NAME" => name = Some(value.to_string()),
                    "COMMENT" => comment = Some(value.to_string()),
                    "PROBLEM" => problem = Some(value.to_string()),
                    "NB_FACILITIES" => {
                        nb_facilities = Some(parse_usize(value, line_no, "NB_FACILITIES")?)
                    }
                    "NB_CUSTOMERS" => {
                        nb_customers = Some(parse_usize(value, line_no, "NB_CUSTOMERS")?)
                    }
                    "COORDINATES" => coordinates = Some(parse_bool(value, line_no)?),
                    _ => {
                        return Err(ParseFlpbError {
                            line: line_no,
                            message: format!("unknown header key: {}", key),
                        });
                    }
                }
            }
            Section::Facilities => {
                let expected = nb_facilities.ok_or(ParseFlpbError {
                    line: line_no,
                    message: "NB_FACILITIES missing before facilities".to_string(),
                })?;
                if facilities.len() >= expected {
                    return Err(ParseFlpbError {
                        line: line_no,
                        message: "too many facilities for NB_FACILITIES".to_string(),
                    });
                }
                let mut parts = trimmed.split_whitespace();
                let capacity = parse_f64(next_part(&mut parts, line_no)?, line_no, "capacity")?;
                let cost = parse_f64(next_part(&mut parts, line_no)?, line_no, "cost")?;
                let x = parse_f64(next_part(&mut parts, line_no)?, line_no, "x")?;
                let y = parse_f64(next_part(&mut parts, line_no)?, line_no, "y")?;
                if parts.next().is_some() {
                    return Err(ParseFlpbError {
                        line: line_no,
                        message: "facility line has extra fields".to_string(),
                    });
                }
                facilities.push(Facility {
                    capacity,
                    cost,
                    x,
                    y,
                });
            }
            Section::Customers => {
                let expected = nb_customers.ok_or(ParseFlpbError {
                    line: line_no,
                    message: "NB_CUSTOMERS missing before customers".to_string(),
                })?;
                if customers.len() >= expected {
                    return Err(ParseFlpbError {
                        line: line_no,
                        message: "too many customers for NB_CUSTOMERS".to_string(),
                    });
                }
                let mut parts = trimmed.split_whitespace();
                let demand = parse_f64(next_part(&mut parts, line_no)?, line_no, "demand")?;
                let x = parse_f64(next_part(&mut parts, line_no)?, line_no, "x")?;
                let y = parse_f64(next_part(&mut parts, line_no)?, line_no, "y")?;
                if parts.next().is_some() {
                    return Err(ParseFlpbError {
                        line: line_no,
                        message: "customer line has extra fields".to_string(),
                    });
                }
                customers.push(Customer { demand, x, y });
            }
            Section::Costs => {
                let mut parts = trimmed.split_whitespace();
                let customer_id = parse_usize(next_part(&mut parts, line_no)?, line_no, "id_cus")?;
                let facility_id = parse_usize(next_part(&mut parts, line_no)?, line_no, "id_fac")?;
                let cost = parse_f64(next_part(&mut parts, line_no)?, line_no, "cost")?;
                if parts.next().is_some() {
                    return Err(ParseFlpbError {
                        line: line_no,
                        message: "cost line has extra fields".to_string(),
                    });
                }
                costs.push(Cost {
                    customer_id,
                    facility_id,
                    cost,
                });
            }
        }
    }

    let name = name.ok_or(ParseFlpbError {
        line: 0,
        message: "missing NAME".to_string(),
    })?;
    let nb_facilities = nb_facilities.ok_or(ParseFlpbError {
        line: 0,
        message: "missing NB_FACILITIES".to_string(),
    })?;
    let nb_customers = nb_customers.ok_or(ParseFlpbError {
        line: 0,
        message: "missing NB_CUSTOMERS".to_string(),
    })?;
    let coordinates = coordinates.ok_or(ParseFlpbError {
        line: 0,
        message: "missing COORDINATES".to_string(),
    })?;

    if facilities.len() != nb_facilities {
        return Err(ParseFlpbError {
            line: 0,
            message: format!(
                "facilities count mismatch: expected {}, got {}",
                nb_facilities,
                facilities.len()
            ),
        });
    }
    if customers.len() != nb_customers {
        return Err(ParseFlpbError {
            line: 0,
            message: format!(
                "customers count mismatch: expected {}, got {}",
                nb_customers,
                customers.len()
            ),
        });
    }

    if !costs.is_empty() {
        let expected_costs = nb_facilities
            .checked_mul(nb_customers)
            .ok_or(ParseFlpbError {
                line: 0,
                message: "costs count overflow".to_string(),
            })?;
        if costs.len() != expected_costs {
            return Err(ParseFlpbError {
                line: 0,
                message: format!(
                    "costs count mismatch: expected {}, got {}",
                    expected_costs,
                    costs.len()
                ),
            });
        }
    }

    Ok(FlpbInstance {
        name,
        comment,
        problem,
        nb_facilities,
        nb_customers,
        coordinates,
        facilities,
        customers,
        costs,
    })
}

fn next_part<'a>(
    parts: &mut impl Iterator<Item = &'a str>,
    line: usize,
) -> Result<&'a str, ParseFlpbError> {
    parts.next().ok_or(ParseFlpbError {
        line,
        message: "not enough fields".to_string(),
    })
}

fn parse_usize(value: &str, line: usize, field: &str) -> Result<usize, ParseFlpbError> {
    value.parse::<usize>().map_err(|err| ParseFlpbError {
        line,
        message: format!("invalid {}: {}", field, err),
    })
}

fn parse_f64(value: &str, line: usize, field: &str) -> Result<f64, ParseFlpbError> {
    value.parse::<f64>().map_err(|err| ParseFlpbError {
        line,
        message: format!("invalid {}: {}", field, err),
    })
}

fn parse_bool(value: &str, line: usize) -> Result<bool, ParseFlpbError> {
    match value {
        "True" | "true" => Ok(true),
        "False" | "false" => Ok(false),
        _ => Err(ParseFlpbError {
            line,
            message: format!("invalid COORDINATES: {}", value),
        }),
    }
}
