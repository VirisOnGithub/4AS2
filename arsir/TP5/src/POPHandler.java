import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class POPHandler implements Runnable {
    private Socket socket;
    private POPDB db;
    private PrintWriter writer;
    private BufferedReader reader;
    private String pendingUser;
    private POPMailbox mailbox;

    public POPHandler(Socket serverSocket, POPDB db) throws IOException {
        this.socket = serverSocket;
        this.db = db;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.mailbox = null;
    }

    @Override
    public void run() {
        try {
            sendMessage("+OK POP3 server ready");

            String line;
            while ((line = reader.readLine()) != null) {
                Command command;
                try {
                    processCommand(line);
                } catch (IllegalArgumentException e) {
                    sendMessage("-ERR " + e.getMessage());
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage() + "\n Maybe client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processCommand(String line) {
        Command command = Command.parseCommand(line);
        switch (command.getType()) {
            case USER -> handleUser(command.getArgs());
            case PASS -> handlePass(command.getArgs());
            case STAT -> handleStat();
            case LIST -> handleList(command.getArgs());
            case RETR -> handleRetr(command.getArgs());
            case TOP -> handleTop(command.getArgs());
            case DELE -> handleDele(command.getArgs());
            case RSET -> handleRset();
            case NOOP -> handleNoop();
            case QUIT -> handleQuit();
        }
    }

    private void handleList(ArrayList<String> args) {
        if (mailbox == null) {
            sendMessage("-ERR You must authenticate first");
            return;
        }
        if (args.isEmpty()) {
            List<String> messages = mailbox.listMessages();
            sendMessage("+OK");
            for (String message : messages) {
                sendMessage(message);
            }
            sendMessage(".");
        } else {
            try {
                int messageNumber = Integer.parseInt(args.getFirst());
                String messageInfo = mailbox.getMessageInfo(messageNumber);
                if (messageInfo != null) {
                    sendMessage("+OK " + messageInfo);
                } else {
                    sendMessage("-ERR No such message");
                }
            } catch (NumberFormatException e) {
                sendMessage("-ERR Invalid message number");
            }
        }
    }

    private void handleDele(ArrayList<String> args) {
        if (mailbox == null) {
            sendMessage("-ERR Authentification requise");
            return;
        }

        if (args.size() != 1) {
            sendMessage("-ERR DELE command requires exactly 1 argument");
            return;
        }

        try {
            int messageNumber = Integer.parseInt(args.getFirst());
            if (mailbox.deleteMessage(messageNumber)) {
                sendMessage("+OK Message supprimé");
            } else {
                sendMessage("-ERR Message non trouvé");
            }
        } catch (NumberFormatException e) {
            sendMessage("-ERR Argument invalide");
        }
    }

    private void handleQuit() {
        if (mailbox != null) {
            try {
                mailbox.commitDeletions();
            } catch (IOException e) {
                System.err.println("Erreur lors de la validation des suppressions: " + e.getMessage());
            }
        }
        sendMessage("+OK Au revoir");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du socket: " + e.getMessage());
        }
    }

    private void handleNoop() {
        sendMessage("+OK");
    }

    private void handleRset() {
        if (mailbox == null) {
            sendMessage("-ERR Authentification requise");
            return;
        }

        mailbox.resetDeletions();
        sendMessage("+OK Réinitialisé");
    }

    private void handleTop(ArrayList<String> args) {
        if (mailbox == null) {
            sendMessage("-ERR You must authenticate first");
            return;
        }
        if (args.size() != 2) {
            sendMessage("-ERR TOP command requires exactly 2 arguments");
            return;
        }
        try {
            int messageNumber = Integer.parseInt(args.get(0));
            int lineCount = Integer.parseInt(args.get(1));
            String content = mailbox.getMessageTopLines(messageNumber, lineCount);
            if (content != null) {
                sendMessage("+OK " + content.length() + " bytes");
                sendMessage(content);
                sendMessage(".");
            } else {
                sendMessage("-ERR No such message");
            }
        } catch (NumberFormatException e) {
            sendMessage("-ERR Invalid message number or line count");
        } catch (IOException e) {
            sendMessage("-ERR Error reading message content");
        }
    }

    private void handleRetr(ArrayList<String> args) {
        if (mailbox == null) {
            sendMessage("-ERR You must authenticate first");
            return;
        }
        if (args.size() != 1) {
            sendMessage("-ERR RETR command requires exactly 1 argument");
            return;
        }
        try {
            int messageNumber = Integer.parseInt(args.get(0));
            String content = mailbox.getMessageContent(messageNumber);
            if (content != null) {
                sendMessage("+OK " + content.length() + " bytes");
                sendMessage(content);
                sendMessage(".");
            } else {
                sendMessage("-ERR No such message");
            }
        } catch (NumberFormatException e) {
            sendMessage("-ERR Invalid message number");
        } catch (IOException e) {
            sendMessage("-ERR Error reading message content");
        }
    }

    private void handleStat() {
        if (mailbox == null) {
            sendMessage("-ERR You must authenticate first");
            return;
        }
        int messageCount = mailbox.getMessageCount();
        long totalSize = mailbox.getTotalSize();
        sendMessage("+OK " + messageCount + " " + totalSize);
    }

    private void handlePass(ArrayList<String> args) {
        if (pendingUser == null) {
            sendMessage("-ERR PASS command requires USER command first");
            return;
        }
        if (args.size() != 1) {
            sendMessage("-ERR PASS command requires exactly 1 argument");
            return;
        }
        String password = args.get(0);
        String authenticatedUser = db.authenticate(pendingUser, password);
        if (authenticatedUser != null) {
            mailbox = new POPMailbox(db.getUserMailDir(authenticatedUser));
            sendMessage("+OK Authentication successful, welcome " + authenticatedUser);
        } else {
            sendMessage("-ERR Authentication failed");
        }
    }

    private void handleUser(ArrayList<String> args) {
        if (args.size() != 1) {
            sendMessage("-ERR USER command requires exactly 1 argument");
            return;
        }
        pendingUser = args.get(0);
        sendMessage("+OK User name accepted, sned PASS command");
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
