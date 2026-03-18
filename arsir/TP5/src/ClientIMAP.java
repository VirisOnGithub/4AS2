import java.io.*;
import java.net.*;

//exo 3
public class ClientIMAP {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int tagCounter = 1;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Commande non reconnue");
            return;

        }

        String server = args[0];
        String user = args[1];
        String pwd = args[2];

        ClientIMAP client = new ClientIMAP();
        try {
            client.run(server, user, pwd);

        } catch (IOException e) {
            System.out.println("Erreur lors de la connexion :" + e.getMessage());
            e.printStackTrace();
        }
    }
    private String nextTag() {
            return "a" + (tagCounter++);
        }
        //envoi une commande IMAP et retourne le tag utilisé
    private String sendCommand(String command) {
            String tag = nextTag();
            String fullCommand = tag + " " + command;
            System.out.println("Client: " + fullCommand);
            out.println(fullCommand);
            out.flush();
            return tag;
        }

    private String readResponse(String tag) throws IOException {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                System.out.println("Server:" + line);
                sb.append(line).append("\n");

                if (line.startsWith(tag + " ")) {
                    break;
                }
            }
            return sb.toString();
        }

    public void run(String server, String user, String pwd) throws IOException {
                socket = new Socket(server, 143);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                //lire message accueil
                String greeting = in.readLine();
                System.out.println("Server :" + greeting);

                //authentification
                String tag = sendCommand("LOGIN " + user + " " + pwd);
                String response = readResponse(tag);
                if (!response.contains(tag + " OK")) {
                    System.err.println("Échec de l'authentification.");
                    close();
                    return;
                }

                //selectionner INBOX
                tag = sendCommand("SELECT INBOX");
                response = readResponse(tag);
                if (!response.contains(tag + " OK")) {
                    System.err.println("échec de la sélection de INBOX.");
                    close();
                    return;

                }
                //recherche des mails non lus
                tag = sendCommand("SEARCH UNSEEN");
                response = readResponse(tag);
                int[] messageIds = parseSearchResponse(response);
                if (messageIds.length == 0) {
                    System.out.println("Aucun message non lu");
                    //tag = sendCommand("CLOSE");
                    //readResponse(tag);
                    //tag = sendCommand("LOGOUT");
                    //readResponse(tag);
                    close();
                    return;
                }
                System.out.println("Vous avez " + messageIds.length + " mail(s) non lu(s)");

                for (int id : messageIds) {
                    System.out.println("Mail n°" + id);
                    tag = sendCommand("FETCH " + id + " BODY[]");
                    response = readResponse(tag);

                    System.out.println(response);
                }
                //marquer comme supprimé
                for (int id : messageIds) {
                    tag = sendCommand("STORE " + id + " +FLAGS (\\Deleted)");
                    readResponse(tag);
                }

                //supprimer définitivement les mails flagué Deleted
                tag = sendCommand("CLOSE");
                readResponse(tag);
                tag = sendCommand("LOGOUT");
                readResponse(tag);
                close();
    }

    private int[] parseSearchResponse(String response){
                    for (String line : response.split("\n")) {
                        if (line.startsWith("* SEARCH")){
                            String nums = line.substring("* SEARCH".length()).trim();
                            if (nums.isEmpty()) {
                                return new int[0];
                            }
                            String[] parts = nums.split("\\s+");
                            int[] ids = new int[parts.length];
                            for (int i = 0; i < parts.length; i++) {
                                ids[i] = Integer.parseInt(parts[i]);
                            }
                            return ids;
                        }
                    }
                    return new int[0];
                }
     private void close() throws IOException {
                    if (socket != null && !socket.isClosed()) {
                        in.close();
                        out.close();
                        socket.close();
        }

    }
}