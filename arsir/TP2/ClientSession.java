import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSession {
    private final String host;
    private final int port;

    public ClientSession(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try (Socket controlSocket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new java.io.InputStreamReader(controlSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(
                        new java.io.OutputStreamWriter(controlSocket.getOutputStream()))) {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String line;
            ServerSocket pendingServer = null;
            Socket dataSocket = null;
            String pasvHost = null;
            int pasvPort = -1;
            boolean authenticated = false;

            String welcome = in.readLine();
            if (welcome != null && !welcome.isEmpty()) {
                System.out.println("S: " + welcome);
            }

            while (true) {
                System.out.print("C> ");
                line = stdin.readLine();
                if (line == null)
                    break;
                String trimmed = line.trim();
                String trimmedUpper = trimmed.toUpperCase();

                if (trimmed.equalsIgnoreCase("PASV")) {
                    if (!authenticated) {
                        System.out.println("Authentification Error");
                        continue;
                    }
                    out.write("PASV\r\n");
                    out.flush();
                    String resp = in.readLine();
                    System.out.println("S: " + resp);
                    if (resp != null && resp.startsWith("227")) {
                        String nums = resp.substring(4).trim();
                        String[] parts = nums.split(",");
                        if (parts.length == 6) {
                            String host = String.format("%s.%s.%s.%s", parts[0].trim(), parts[1].trim(),
                                    parts[2].trim(),
                                    parts[3].trim());
                            int e = Integer.parseInt(parts[4].trim());
                            int f = Integer.parseInt(parts[5].trim());
                            int dataPort = e * 256 + f;
                            pasvHost = host;
                            pasvPort = dataPort;
                            dataSocket = new Socket(host, dataPort);
                            System.out.println("Connected data socket to " + host + ":" + dataPort);
                        }
                    }
                    continue;
                }

                if (trimmed.equalsIgnoreCase("PORT")) {
                    if (!authenticated) {
                        System.out.println("Authentification Error");
                        continue;
                    }
                    if (pendingServer != null) {
                        try {
                            pendingServer.close();
                        } catch (Exception ex) {
                        }
                    }
                    ServerSocket ss = new ServerSocket(0, 1, controlSocket.getLocalAddress());
                    pendingServer = ss;
                    byte[] addr = controlSocket.getLocalAddress().getAddress();
                    int p = ss.getLocalPort();
                    int e = (p >> 8) & 0xFF;
                    int f = p & 0xFF;
                    String portArg = String.format("%d,%d,%d,%d,%d,%d", addr[0] & 0xFF, addr[1] & 0xFF, addr[2] & 0xFF,
                            addr[3] & 0xFF, e, f);
                    out.write("PORT " + portArg + "\r\n");
                    out.flush();
                    System.out.println("C: PORT " + portArg);
                    String resp = in.readLine();
                    System.out.println("S: " + resp);
                    continue;
                }

                if (trimmedUpper.startsWith("LIST")) {
                    if (!authenticated) {
                        System.out.println("Authentification Error");
                        continue;
                    }
                    if (dataSocket == null && pendingServer == null) {
                        if (pasvHost != null && pasvPort > 0) {
                            dataSocket = new Socket(pasvHost, pasvPort);
                        } else {
                            System.out.println("No data connection prepared. Use PASV or PORT first.");
                            out.write("LIST\r\n");
                            out.flush();
                            System.out.println("S: " + in.readLine());
                            continue;
                        }
                    }

                    out.write("LIST\r\n");
                    out.flush();
                    String resp150 = in.readLine();
                    System.out.println("S: " + resp150);

                    Socket ds = null;
                    if (dataSocket != null) {
                        ds = dataSocket;
                    } else if (pendingServer != null) {
                        ds = pendingServer.accept();
                        System.out.println("Accepted data connection from server: " + ds.getRemoteSocketAddress());
                    }

                    if (ds != null) {
                        try (BufferedReader dataIn = new BufferedReader(new InputStreamReader(ds.getInputStream()))) {
                            List<String> entries = dataIn.lines().toList();
                            int maxEntryLength = entries.stream().mapToInt(String::length).max().orElse(0);

                            System.out.println("+" + "-".repeat(maxEntryLength + 2) + "+");
                            for (String entry : entries) {
                                System.out.println("| " + entry + " ".repeat(maxEntryLength - entry.length()) + " |");
                            }
                            System.out.println("+" + "-".repeat(maxEntryLength + 2) + "+");

                        } catch (IOException ex) {
                            System.out.println("Error reading data: " + ex.getMessage());
                        } finally {
                            try {
                                ds.close();
                            } catch (Exception ex) {
                            }
                            dataSocket = null;
                        }
                    }

                    String resp226 = in.readLine();
                    System.out.println("S: " + resp226);
                    continue;
                }

                if (trimmedUpper.startsWith("RETR")) {
                    if (!authenticated) {
                        System.out.println("Authentification Error");
                        continue;
                    }
                    String[] tok = trimmed.split("\\s+", 2);
                    if (tok.length < 2) {
                        System.out.println("Usage: RETR <filename>");
                        continue;
                    }
                    String filename = tok[1];

                    if (dataSocket == null && pendingServer == null) {
                        if (pasvHost != null && pasvPort > 0) {
                            dataSocket = new Socket(pasvHost, pasvPort);
                        } else {
                            System.out.println("No data connection prepared. Use PASV or PORT first.");
                            out.write(trimmed + "\r\n");
                            out.flush();
                            System.out.println("S: " + in.readLine());
                            continue;
                        }
                    }

                    out.write(trimmed + "\r\n");
                    out.flush();
                    String resp150 = in.readLine();
                    System.out.println("S: " + resp150);

                    Socket ds = null;
                    if (dataSocket != null) {
                        ds = dataSocket;
                    } else if (pendingServer != null) {
                        ds = pendingServer.accept();
                        System.out.println("Accepted data connection from server: " + ds.getRemoteSocketAddress());
                    }

                    if (ds != null) {
                        File outFile = new File(filename);
                        try (InputStream dataIn = ds.getInputStream();
                                FileOutputStream fos = new FileOutputStream(outFile)) {
                            byte[] buf = new byte[8192];
                            int r;
                            while ((r = dataIn.read(buf)) != -1) {
                                fos.write(buf, 0, r);
                            }
                            fos.flush();
                            System.out.println("Saved file to " + outFile.getAbsolutePath());
                        } catch (IOException ex) {
                            System.out.println("Error receiving file: " + ex.getMessage());
                        } finally {
                            try {
                                ds.close();
                            } catch (Exception ex) {
                            }
                            dataSocket = null;
                        }
                    }

                    String resp226 = in.readLine();
                    System.out.println("S: " + resp226);
                    continue;
                }

                out.write(line + "\r\n");
                out.flush();
                String resp = in.readLine();
                if (resp == null)
                    break;
                System.out.println("S: " + resp);
                if (trimmedUpper.startsWith("USER ")) {
                    authenticated = false;
                    pasvHost = null;
                    pasvPort = -1;
                } else if (trimmedUpper.startsWith("PASS ")) {
                    authenticated = resp.startsWith("200");
                } else if (trimmedUpper.equals("DECO")) {
                    authenticated = false;
                    pasvHost = null;
                    pasvPort = -1;
                    if (dataSocket != null) {
                        try {
                            dataSocket.close();
                        } catch (Exception ex) {
                        }
                        dataSocket = null;
                    }
                    if (pendingServer != null) {
                        try {
                            pendingServer.close();
                        } catch (Exception ex) {
                        }
                        pendingServer = null;
                    }
                }
                if (trimmed.equalsIgnoreCase("QUIT"))
                    break;
            }

            if (pendingServer != null)
                try {
                    pendingServer.close();
                } catch (Exception e) {
                }
            if (dataSocket != null)
                try {
                    dataSocket.close();
                } catch (Exception e) {
                }

        } catch (Exception e) {
            if (e instanceof IOException) {
                System.out.println("Connection error: " + e.getMessage());
            } else {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void sendCommand(String command, BufferedWriter out) throws Exception {
        out.write(command + "\r\n");
        out.flush();
    }
}
