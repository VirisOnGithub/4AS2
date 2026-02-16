use ftprust::client_session::ClientSession;

const HOST: &str = "127.0.0.1";
const PORT: u32 = 2121;

fn main() {
    let mut session = ClientSession::new(HOST, PORT);
    println!("Connecting to {}:{}...", HOST, PORT);
    session.run();
}
