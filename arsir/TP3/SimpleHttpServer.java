import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class SimpleHttpServer {
    private static int PORT = 8000;
    private static final String SITE_A_HOST = "localhost";
    private static final String SITE_A_ROOT = "public";
    private static final String SITE_B_HOST = "alt.localhost";
    private static final String SITE_B_ROOT = "public_alt";

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            try {
                PORT = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Port invalide, utilisation du port par défaut: " + PORT);
            }
        }

        ensureWebRoot(SITE_A_ROOT);
        ensureWebRoot(SITE_B_ROOT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println(
                    "Site A: http://" + SITE_A_HOST + ":" + PORT + "/ -> " + new File(SITE_A_ROOT).getAbsolutePath());
            System.out.println(
                    "Site B: http://" + SITE_B_HOST + ":" + PORT + "/ -> " + new File(SITE_B_ROOT).getAbsolutePath());
            System.out.println("Accédez à http://localhost:" + PORT + "/");

            ExecutorService threadPool = java.util.concurrent.Executors.newCachedThreadPool();

            while (true) {
                Socket client = serverSocket.accept();
                threadPool.submit(() -> {
                    try {
                        handleClient(client);
                    } catch (IOException e) {
                        System.err.println("Erreur lors du traitement de la requête: " + e.getMessage());
                    }
                });
            }
        }
    }

    private static void handleClient(Socket client) throws IOException {
        try (client;
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            Request request = readRequest(reader, in);

            // Ici tu as le contrôle total du message reçu
            System.out.println("--- Requête reçue ---");
            System.out.println(request.method + " " + request.path + " " + request.version);
            request.headers.forEach((k, v) -> System.out.println(k + ": " + v));
            if (!request.body.isEmpty()) {
                System.out.println("\n" + request.body);
            }

            if (!"GET".equals(request.method) && !"HEAD".equals(request.method)) {
                writeResponse(out, 405, "Method Not Allowed", "text/plain; charset=UTF-8",
                        "Méthode non supportée".getBytes(StandardCharsets.UTF_8), true);
                return;
            }

            String hostHeader = getHeaderIgnoreCase(request.headers, "Host");
            if (hostHeader == null || hostHeader.isBlank()) {
                writeResponse(out, 400, "Bad Request", "text/plain; charset=UTF-8",
                        "En-tête Host manquant".getBytes(StandardCharsets.UTF_8), true);
                return;
            }

            String webRoot = resolveWebRoot(hostHeader);
            if (webRoot == null) {
                writeResponse(out, 404, "Not Found", "text/plain; charset=UTF-8",
                        "Site inconnu".getBytes(StandardCharsets.UTF_8), true);
                return;
            }

            String targetPath = sanitizePath(request.path);
            if (targetPath.equals("/")) {
                targetPath = "/index.html";
            }

            Path rootPath = Paths.get(webRoot).toAbsolutePath().normalize();
            Path filePath = Paths.get(webRoot, targetPath).normalize();
            if (!filePath.toAbsolutePath().normalize().startsWith(rootPath)) {
                writeResponse(out, 403, "Forbidden", "text/plain; charset=UTF-8",
                        "Accès interdit".getBytes(StandardCharsets.UTF_8), true);
                return;
            }

            File file = filePath.toFile();
            if (file.isDirectory()) {
                File indexFile = new File(file, "index.html");
                file = indexFile;
                filePath = indexFile.toPath();
            }

            if (!file.exists() || file.isDirectory()) {
                writeResponse(out, 404, "Not Found", "text/html; charset=UTF-8",
                        ("<html><body><h1>404 - Page non trouvée</h1>" +
                                "<p>Le fichier " + targetPath + " n'existe pas.</p>" +
                                "</body></html>").getBytes(StandardCharsets.UTF_8),
                        true);
                return;
            }

            String contentType = getContentType(file.getName());
            byte[] content = Files.readAllBytes(filePath);
            boolean includeBody = "GET".equals(request.method);
            writeResponse(out, 200, "OK", contentType, content, includeBody);
        }
    }

    private static Request readRequest(BufferedReader reader, InputStream in) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isBlank()) {
            return new Request("", "", "", new LinkedHashMap<>(), "");
        }

        String[] parts = requestLine.split(" ");
        String method = parts.length > 0 ? parts[0] : "";
        String path = parts.length > 1 ? parts[1] : "";
        String version = parts.length > 2 ? parts[2] : "";

        Map<String, String> headers = new LinkedHashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(':');
            if (idx > 0) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                headers.put(key, value);
            }
        }

        int contentLength = 0;
        String cl = headers.getOrDefault("Content-Length", headers.getOrDefault("content-length", "0"));
        try {
            contentLength = Integer.parseInt(cl);
        } catch (NumberFormatException ignored) {
            contentLength = 0;
        }

        String body = "";
        if (contentLength > 0) {
            byte[] buf = in.readNBytes(contentLength);
            body = new String(buf, StandardCharsets.UTF_8);
        }

        return new Request(method, path, version, headers, body);
    }

    private static void writeResponse(OutputStream out, int statusCode, String reason,
            String contentType, byte[] bodyBytes, boolean includeBody) throws IOException {
        String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(reason).append("\r\n");
        sb.append("Date: ").append(date).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(bodyBytes.length).append("\r\n");
        sb.append("Connection: close\r\n");
        sb.append("\r\n");

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        if (includeBody) {
            out.write(bodyBytes);
        }
        out.flush();
    }

    private static String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".json")) {
            return "application/json";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }

    private static void ensureWebRoot(String root) {
        File webRootDir = new File(root);
        if (!webRootDir.exists()) {
            System.out.println("Création du dossier: " + root);
            webRootDir.mkdirs();
        }
    }

    private static String resolveWebRoot(String hostHeader) {
        String host = hostHeader.trim().toLowerCase();
        int colonIdx = host.indexOf(':');
        if (colonIdx >= 0) {
            host = host.substring(0, colonIdx);
        }

        if (SITE_A_HOST.equalsIgnoreCase(host)) {
            return SITE_A_ROOT;
        }
        if (SITE_B_HOST.equalsIgnoreCase(host)) {
            return SITE_B_ROOT;
        }
        return null;
    }

    private static String getHeaderIgnoreCase(Map<String, String> headers, String name) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static String sanitizePath(String rawPath) {
        String path = rawPath;
        int queryIdx = path.indexOf('?');
        if (queryIdx >= 0) {
            path = path.substring(0, queryIdx);
        }

        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '%' && i + 2 < path.length()) {
                String hex = path.substring(i + 1, i + 3);
                try {
                    decoded.append((char) Integer.parseInt(hex, 16));
                    i += 2;
                } catch (NumberFormatException ignored) {
                    decoded.append(c);
                }
            } else {
                decoded.append(c);
            }
        }

        return decoded.toString();
    }

    private static class Request {
        final String method;
        final String path;
        final String version;
        final Map<String, String> headers;
        final String body;

        Request(String method, String path, String version, Map<String, String> headers, String body) {
            this.method = method;
            this.path = path;
            this.version = version;
            this.headers = headers;
            this.body = body;
        }
    }
}