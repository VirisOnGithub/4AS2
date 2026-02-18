use ftprust::command_parser::{Commands, parse_command};
use std::net::TcpListener;
use std::path::PathBuf;
use std::{
    io::{BufRead, BufReader, Write},
    net::TcpStream,
};

const HOST: &str = "127.0.0.1";
const PORT: u32 = 2121;

fn main() {
    println!("Starting FTP server on {}:{}...", HOST, PORT);
    let listener = std::net::TcpListener::bind(format!("{}:{}", HOST, PORT))
        .expect("Failed to bind to address");
    for stream in listener.incoming() {
        match stream {
            Ok(stream) => {
                println!("New client connected: {}", stream.peer_addr().unwrap());
                let mut ftp_server = FtpServer::new(stream);
                std::thread::spawn(move || {
                    ftp_server.handle_connection();
                });
            }
            Err(e) => {
                eprintln!("Failed to accept client: {}", e);
            }
        }
    }
}

struct FtpServer {
    stream: TcpStream,
    user: Option<String>,
    authenticated: bool,
    authorized_users: Vec<(String, String)>,
    data_socket: Option<TcpStream>,
    base_cwd: PathBuf,
    cwd: PathBuf,
}

impl FtpServer {
    fn new(stream: TcpStream) -> Self {
        let cwd = std::env::current_dir().unwrap().join("../Data");
        FtpServer {
            stream,
            user: None,
            authenticated: false,
            authorized_users: vec![("foo".to_string(), "bar".to_string())],
            data_socket: None,
            base_cwd: cwd.clone(),
            cwd,
        }
    }

    fn handle_connection(&mut self) {
        let mut reader = BufReader::new(self.stream.try_clone().expect("Failed to clone stream"));
        let mut buffer = String::new();
        loop {
            buffer.clear();
            match reader.read_line(&mut buffer) {
                Ok(0) => {
                    println!("Client disconnected: {}", self.stream.peer_addr().unwrap());
                    break;
                }
                Ok(_) => {
                    let message = buffer.trim();
                    println!("Received from client: {}", message);
                    self.handle_message(message);
                }
                Err(e) => {
                    eprintln!("Failed to read from client: {}", e);
                    break;
                }
            }
        }
    }

    fn handle_message(&mut self, message: &str) {
        match parse_command(message) {
            Some(cmd) => match cmd {
                Commands::User(username) => {
                    if self.authorized_users.iter().any(|(u, _)| u == &username) {
                        self.user = Some(username.clone());
                        // création du dossier user si pas déjà existant
                        let user_dir = self.cwd.join(&username);
                        if !user_dir.exists() {
                            if let Err(e) = std::fs::create_dir_all(&user_dir) {
                                eprintln!("Failed to create user directory: {}", e);
                            }
                        }
                        send_to_client(&mut self.stream, "331 Username OK, need password");
                    } else {
                        send_to_client(&mut self.stream, "530 Invalid username");
                    }
                }
                Commands::Pass(passwd) => {
                    if let Some(ref user) = self.user {
                        if self
                            .authorized_users
                            .iter()
                            .any(|(u, p)| u == user && p == &passwd)
                        {
                            self.authenticated = true;
                            self.cwd = std::fs::canonicalize(self.base_cwd.join(user)).unwrap();
                            self.base_cwd = self.cwd.clone();
                            send_to_client(&mut self.stream, "230 User logged in");
                        } else {
                            send_to_client(&mut self.stream, "530 Invalid password");
                        }
                    } else {
                        send_to_client(&mut self.stream, "503 Login with USER first");
                    }
                }
                Commands::Pasv => {
                    if !self.authenticated {
                        send_to_client(&mut self.stream, "530 Please login first");
                        return;
                    }

                    self.data_socket = match TcpListener::bind("127.0.0.1:0") {
                        Ok(listener) => {
                            let local_addr = listener.local_addr().unwrap();
                            println!("Data connection listening on {}", local_addr);
                            send_to_client(
                                &mut self.stream,
                                &format!(
                                    "227 127,0,0,1,{},{}",
                                    local_addr.port() / 256,
                                    local_addr.port() % 256
                                ),
                            );
                            println!(
                                "numbers sent: 127,0,0,1,{},{}",
                                local_addr.port() / 256,
                                local_addr.port() % 256
                            );
                            match listener.accept() {
                                Ok((data_stream, _)) => Some(data_stream),
                                Err(e) => {
                                    eprintln!("Failed to accept data connection: {}", e);
                                    None
                                }
                            }
                        }
                        Err(e) => {
                            eprintln!("Failed to bind data socket: {}", e);
                            None
                        }
                    };
                }
                Commands::List => {
                    if !self.authenticated {
                        send_to_client(&mut self.stream, "530 Please login first");
                        return;
                    }
                    if let Some(ref mut data_socket) = self.data_socket {
                        send_to_client(&mut self.stream, "150 Here comes the directory listing");
                        let listing = std::fs::read_dir(&self.cwd)
                            .unwrap()
                            .filter_map(|entry| entry.ok())
                            .map(|entry| entry.file_name().into_string().unwrap_or_default())
                            .collect::<Vec<String>>()
                            .join(";");
                        let listing_bytes = listing.as_bytes();
                        let listing_len = listing_bytes.len() as u32;
                        if let Err(e) = data_socket.write_all(&listing_len.to_be_bytes()) {
                            eprintln!("Failed to send directory length: {}", e);
                        }
                        if let Err(e) = data_socket.write_all(listing_bytes) {
                            eprintln!("Failed to send directory listing: {}", e);
                        }
                        send_to_client(&mut self.stream, "226 Directory send OK");
                    } else {
                        send_to_client(
                            &mut self.stream,
                            "425 Can't open data connection, try PASV first",
                        );
                    }
                }
                Commands::Cwd(new_dir) => {
                    if !self.authenticated {
                        send_to_client(&mut self.stream, "530 Please login first");
                        return;
                    }
                    let new_path = self.cwd.join(new_dir);
                    println!("Attempting to change directory to: {}", new_path.display());
                    println!("Base directory: {}", self.base_cwd.display());
                    if new_path.is_dir()
                        && std::fs::canonicalize(&new_path)
                            .unwrap()
                            .starts_with(&self.base_cwd)
                    {
                        self.cwd = new_path;
                        send_to_client(&mut self.stream, "250 Directory successfully changed");
                    } else {
                        send_to_client(&mut self.stream, "550 Failed to change directory");
                    }
                }
                Commands::Retr(filename) => {
                    if !self.authenticated {
                        send_to_client(&mut self.stream, "530 Please login first");
                        return;
                    }
                    if self.data_socket.is_none() {
                        send_to_client(
                            &mut self.stream,
                            "425 Can't open data connection, try PASV first",
                        );
                        return;
                    }
                    send_to_client(&mut self.stream, "150 Opening data connection");
                    match std::fs::read(self.cwd.join(filename)) {
                        Ok(content) => {
                            if let Some(ref mut data_socket) = self.data_socket {
                                let content_len = content.len() as u32;
                                if let Err(e) = data_socket.write_all(&content_len.to_be_bytes()) {
                                    eprintln!("Failed to send file length: {}", e);
                                }
                                if let Err(e) = data_socket.write_all(&content) {
                                    eprintln!("Failed to send file: {}", e);
                                }
                            }
                        }
                        Err(_) => {
                            send_to_client(&mut self.stream, "550 Failed to read file");
                            return;
                        }
                    }
                    send_to_client(&mut self.stream, "226 Transfer complete");
                }
                Commands::Quit => {
                    send_to_client(&mut self.stream, "221 Goodbye");
                    println!("Client disconnected: {}", self.stream.peer_addr().unwrap());
                    std::process::exit(0);
                }
            },
            None => {
                eprintln!("Invalid command received: {}", message);
                send_to_client(&mut self.stream, "Invalid command");
            }
        }
    }
}

fn send_to_client(stream: &mut std::net::TcpStream, message: &str) {
    if let Err(e) = stream.write_all(format!("{}\n", message).as_bytes()) {
        eprintln!("Failed to send message to client: {}", e);
    }
}
