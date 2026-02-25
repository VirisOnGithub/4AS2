use std::collections::HashMap;
use std::io::{BufRead, BufReader, Write};
use std::net::TcpStream;

const SMTP_SERVER: &str = "localhost";
const SMTP_PORT: u16 = 25;

fn main() {
    let mut destinataires: HashMap<&str, &str> = HashMap::new();
    destinataires.insert("Sofian", "sofian@localhost");
    destinataires.insert("Alin", "alin@localhost");

    for (nom, email) in &destinataires {
        let email_from = "clement@localhost";
        match envoyer_email(nom, email, email_from) {
            Ok(_) => println!("Email envoyé avec succès à {} ({})", nom, email),
            Err(e) => eprintln!("Erreur lors de l'envoi à {}: {}", nom, e),
        }
    }
}

fn envoyer_email(nom: &str, email_destinataire: &str, email_from: &str) -> std::io::Result<()> {
    let addr = format!("{}:{}", SMTP_SERVER, SMTP_PORT);
    let stream = TcpStream::connect(&addr)?;
    let mut reader = BufReader::new(stream.try_clone()?);
    let mut writer = stream;

    let mut reponse = String::new();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "HELO client")?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "MAIL FROM:<{}>", email_from)?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "RCPT TO:<{}>", email_destinataire)?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "DATA")?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "From: {}", email_from)?;
    writeln!(writer, "To: {}", email_destinataire)?;
    writeln!(writer, "Subject: Bonjour!")?;
    writeln!(writer, "Content-Type: text/plain; charset=UTF-8")?;
    writeln!(writer)?;
    writeln!(writer, "Bonjour {} !", nom)?;
    writeln!(writer, "Clément")?;
    writeln!(writer, ".")?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    writeln!(writer, "QUIT")?;
    reponse.clear();
    reader.read_line(&mut reponse)?;
    print!("Serveur: {}", reponse);

    Ok(())
}
