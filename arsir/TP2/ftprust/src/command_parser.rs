pub enum Commands {
    User(String),
    Pass(String),
    Pasv,
    List,
    Cwd(String),
    Retr(String),
    Quit,
}

pub fn parse_command(input: &str) -> Option<Commands> {
    let mut parts = input.splitn(2, ' ');
    let command = parts.next()?.to_uppercase();
    let argument = parts.next().unwrap_or("").to_string();

    match command.as_str() {
        "USER" => Some(Commands::User(argument)),
        "PASS" => Some(Commands::Pass(argument)),
        "PASV" => Some(Commands::Pasv),
        "LIST" => Some(Commands::List),
        "CWD" => Some(Commands::Cwd(argument)),
        "RETR" => Some(Commands::Retr(argument)),
        "QUIT" => Some(Commands::Quit),
        _ => None,
    }
}
