use std::{
    error::Error,
    io::{BufRead, BufReader, Write},
    net::TcpStream,
};

const POP_SERVER: &str = "localhost";
const POP_PORT: u16 = 110;

fn main() {
    let username = "alin";
    let password = "alin";

    match fetch_emails(username, password) {
        Ok(_) => println!("Email retrieval successful."),
        Err(e) => eprintln!("Error: {}", e),
    }
}

fn fetch_emails(username: &str, password: &str) -> Result<(), Box<dyn Error>> {
    let addr = format!("{}:{}", POP_SERVER, POP_PORT);
    let stream = TcpStream::connect(&addr)?;
    let mut reader = BufReader::new(stream.try_clone()?);
    let mut writer = stream;

    let mut response;
    response = read_server_response(&mut reader)?;
    if !response.trim().starts_with("+OK") {
        return Err(format!("Unexpected server response: {}", response).into());
    }

    writeln!(writer, "USER {}", username)?;
    response = read_server_response(&mut reader)?;
    if !response.trim().starts_with("+OK") {
        return Err("Invalid username".into());
    }

    writeln!(writer, "PASS {}", password)?;
    response = read_server_response(&mut reader)?;
    if !response.trim().starts_with("+OK") {
        return Err("Invalid password".into());
    }

    writeln!(writer, "STAT")?;
    response = read_server_response(&mut reader)?;
    if !response.trim().starts_with("+OK") {
        return Err("Failed to retrieve email statistics".into());
    }

    let parts: Vec<&str> = response.split_whitespace().collect();
    if parts.len() < 2 {
        return Err("Unexpected response format".into());
    }
    let email_count: usize = parts[1].parse()?;
    println!("You have {} emails.", email_count);

    for i in 1..=email_count {
        writeln!(writer, "RETR {}", i)?;
        loop {
            response = read_server_response(&mut reader)?;
            if response.trim() == "." {
                break;
            }
            print!("{}", response);
        }

        writeln!(writer, "DELE {}", i)?;
        response = read_server_response(&mut reader)?;
        if !response.trim().starts_with("+OK") {
            return Err(format!("Failed to delete email {}", i).into());
        }
    }

    writeln!(writer, "QUIT")?;
    response = read_server_response(&mut reader)?;
    if !response.trim().starts_with("+OK") {
        return Err("Failed to quit POP session".into());
    }

    println!("Successfully retrieved and deleted all emails.");

    Ok(())
}

fn read_server_response(reader: &mut BufReader<TcpStream>) -> Result<String, Box<dyn Error>> {
    let mut response = String::new();
    reader.read_line(&mut response)?;
    Ok(response)
}
