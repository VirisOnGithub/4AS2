use std::{
    net::UdpSocket,
    time::{SystemTime, UNIX_EPOCH},
};

const PORT: u32 = 12345;

fn main() -> std::io::Result<()> {
    let socket = UdpSocket::bind(format!("127.0.0.1:{PORT}"))?;

    let mut buf = [0; 1];

    let (amt, src) = socket.recv_from(&mut buf)?;
    let now = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_millis();

    socket.send_to(now.to_string().as_bytes(), src)?;

    Ok(())
}
