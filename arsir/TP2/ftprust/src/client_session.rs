use std::{
    io::{self, BufRead, BufReader, Read, Write},
    net::TcpStream,
};

use crate::command_parser::{Commands, parse_command};
pub struct ClientSession {
    host: String,
    port: u32,
    authenticated: bool,
    data_socket: Option<TcpStream>,
    has_entered_username: bool,
}

pub fn write_to_server(mut s: TcpStream, message: &str) {
    use std::io::Write;
    let message = format!("{}\n", message);
    if let Err(e) = s.write_all(message.as_bytes()) {
        println!("Couldn't contact server: {e}");
    };
    s.flush().expect("Failed to flush stream");
}

impl ClientSession {
    pub fn new(host: &str, port: u32) -> Self {
        ClientSession {
            host: host.to_string(),
            port,
            authenticated: false,
            data_socket: None,
            has_entered_username: false,
        }
    }

    pub fn handle_command(&mut self, input: &str, stream: &TcpStream) {
        // Parse the command and optionally handle it locally before sending to server
        match parse_command(input) {
            Some(cmd) => match cmd {
                Commands::User(_) => {
                    write_to_server(stream.try_clone().unwrap(), input);
                    if let Some(r) = get_server_reponse(stream) {
                        println!("Server response: {}", r);
                        if r.starts_with("331") {
                            self.has_entered_username = true;
                        } else {
                            eprintln!("Unexpected response to USER command: {}", r);
                        }
                    }
                }
                Commands::Pass(_) => {
                    if !self.has_entered_username {
                        eprintln!("Please enter username first using USER command.");
                        return;
                    }
                    write_to_server(stream.try_clone().unwrap(), input);
                    if let Some(r) = get_server_reponse(stream) {
                        println!("Server response: {}", r);
                        if r.starts_with("230") {
                            self.authenticated = true;
                        } else {
                            eprintln!("Authentication failed: {}", r);
                        }
                    }
                }
                Commands::Pasv => {
                    if !self.authenticated {
                        eprintln!("Authentication Error");
                        return;
                    }
                    write_to_server(stream.try_clone().unwrap(), input);
                    if let Some(r) = get_server_reponse(stream) {
                        if r.starts_with("227") {
                            let unformatted_port: Vec<&str> = r[4..].split(",").collect();
                            if unformatted_port.len() == 6 {
                                let ip = format!(
                                    "{}.{}.{}.{}",
                                    unformatted_port[0],
                                    unformatted_port[1],
                                    unformatted_port[2],
                                    unformatted_port[3]
                                );
                                let p1: u16 = unformatted_port[4].parse().unwrap_or(0);
                                let p2: u16 = unformatted_port[5].parse().unwrap_or(0);
                                let data_port = p1 * 256 + p2;
                                println!("Connecting to data port: {}", data_port);
                                self.data_socket = Some(
                                    TcpStream::connect(format!("{}:{}", ip, data_port))
                                        .expect("Failed to connect to data port"),
                                );
                                println!(
                                    "Data socket established on {}",
                                    self.data_socket.as_ref().unwrap().peer_addr().unwrap()
                                );
                            } else {
                                eprintln!("Unexpected PASV response format: {}", r);
                            }
                        } else {
                            eprintln!("Failed to enter passive mode: {}", r);
                        }
                    }
                }
                Commands::List => {
                    if !self.authenticated {
                        eprintln!("Authentication Error");
                        return;
                    }
                    if self.data_socket.is_none() {
                        eprintln!("Data socket not established. Use PASV command first.");
                        return;
                    }
                    write_to_server(stream.try_clone().unwrap(), input);
                    if let Some(r) = get_server_reponse(stream) {
                        println!("Server response: {}", r);
                        if r.starts_with("150") {
                            if let Some(data_stream) = &mut self.data_socket {
                                let mut data_reader = BufReader::new(data_stream);
                                let mut data_response = String::new();
                                data_reader
                                    .read_to_string(&mut data_response)
                                    .expect("Failed to read from data socket");
                                println!("Directory listing:\n{}", data_response);
                                for line in data_response.split(";") {
                                    println!("{}", line);
                                }
                            } else {
                                eprintln!("Data socket not established.");
                            }
                        }
                    }
                }
                Commands::Cwd(_) => {
                    if !self.authenticated {
                        eprintln!("Authentication Error");
                        return;
                    }
                    write_to_server(stream.try_clone().unwrap(), input);
                }
                Commands::Retr(file_name) => {
                    if !self.authenticated {
                        eprintln!("Authentication Error");
                        return;
                    }
                    if self.data_socket.is_none() {
                        eprintln!("Data socket not established. Use PASV command first.");
                        return;
                    }
                    write_to_server(stream.try_clone().unwrap(), input);
                    if let Some(r) = get_server_reponse(stream) {
                        println!("Server response: {}", r);
                        if r.starts_with("150") {
                            if let Some(data_stream) = &mut self.data_socket {
                                let mut data_reader = BufReader::new(data_stream);
                                let mut file_data = Vec::new();
                                data_reader.read_to_end(&mut file_data).unwrap_or(0);
                                let mut local_file = std::fs::File::create(&file_name)
                                    .expect("Failed to create local file");
                                local_file
                                    .write_all(&file_data)
                                    .expect("Failed to write to local file");
                                println!("File '{}' downloaded successfully.", file_name);
                            } else {
                                eprintln!("Data socket not established.");
                            }
                        }
                    }
                }
                Commands::Quit => {
                    write_to_server(stream.try_clone().unwrap(), input);
                    println!("Quitting client session.");
                    std::process::exit(0);
                }
            },
            None => {
                println!("Invalid command.");
            }
        }
    }

    pub fn run(&mut self) {
        println!("Running client session...");
        let address = format!("{}:{}", self.host, self.port);
        match std::net::TcpStream::connect(&address) {
            Ok(stream) => {
                println!("Connected to server at {}", address);
                let mut reader = BufReader::new(&stream);

                loop {
                    print!("C>");
                    io::stdout().flush().expect("Failed to flush stdout");
                    let mut input = String::new();
                    std::io::stdin()
                        .read_line(&mut input)
                        .expect("Failed to read input");
                    let input = input.trim();
                    self.handle_command(input, &stream);
                }
            }
            Err(e) => {
                eprintln!("Failed to connect to server: {}", e);
            }
        }
    }
}

fn get_server_reponse(stream: &TcpStream) -> Option<String> {
    let mut reader = BufReader::new(stream);
    let mut response = String::new();
    match reader.read_line(&mut response) {
        Ok(0) => {
            println!("Server closed the connection.");
            None
        }
        Ok(_) => Some(response.trim().to_string()),
        Err(e) => {
            eprintln!("Failed to read from server: {}", e);
            None
        }
    }
}
