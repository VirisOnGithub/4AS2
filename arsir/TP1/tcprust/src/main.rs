use std::{
    fs,
    io::{BufReader, prelude::*},
    net::{TcpListener, TcpStream},
};

fn main() {
    let listener = TcpListener::bind("127.0.0.1:7878").unwrap();

    for stream in listener.incoming() {
        let stream = stream.unwrap();

        handle_connection(stream);
    }
}

fn handle_connection(mut stream: TcpStream) {
    let buf_reader = BufReader::new(&stream);
    let http_request: Vec<_> = buf_reader
        .lines()
        .map(|result| result.unwrap())
        .take_while(|line| !line.is_empty())
        .collect();

    println!("{}", http_request.join("\n") + "\n\n");

    let get_line = &http_request[0];

    let sep = "\r\n";

    if get_line.starts_with("GET") {
        let reg = regex::Regex::new(r"GET\s+(\S+)\s+HTTP/1.1").unwrap();
        let captures = reg.captures(get_line).unwrap();
        let path = &captures[1];
        let status = "HTTP/1.1 200 OK";
        let file_path = if path == "/" {
            "hello.html"
        } else {
            &path[1..]
        };
        println!("Requested path: {}", file_path);
        let contents =
            fs::read_to_string(file_path).unwrap_or_else(|_| "File not found".to_string());
        let response = format!("{status}{sep}{sep}{contents}");
        stream.write_all(response.as_bytes()).unwrap();
    } else {
        let status = "HTTP/1.1 404 Not Found";
        let contents = fs::read_to_string("hello.html").unwrap();

        let response = format!("{status}{sep}{sep}{contents}");

        stream.write_all(response.as_bytes()).unwrap();
    }
}
