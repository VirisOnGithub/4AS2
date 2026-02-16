use std::{net::UdpSocket, time::Duration};

const PORT: u32 = 12345;

fn main() -> std::io::Result<()> {
    let socket = UdpSocket::bind("0.0.0.0:0")?;

    socket.set_read_timeout(Some(Duration::from_secs(2)))?;

    let serv_addr = format!("127.0.0.1:{PORT}");
    let send_buf = [0, 1];
    socket.send_to(&send_buf, serv_addr)?;

    let mut recv_buf = [0; 1024];

    match socket.recv_from(&mut recv_buf) {
        Ok((amt, _src)) => {
            match String::from_utf8_lossy(&recv_buf[..amt])
                .trim()
                .to_string()
                .parse::<i64>()
            {
                Ok(timestr) => println!("Server time is: {timestr}"),
                Err(_) => eprintln!("String parse error"),
            }
        }
        Err(_) => eprintln!("Socket error"),
    };

    Ok(())
}
