import java.io.File;
import java.util.Map;

public class POPDB {
    private final Map<String, String> users;
    private static final String MAIL_DIR = "mail";

    public POPDB() {
        this.users = Map.of(
            "u", "p",
            "u2", "p2"
        );

        createMailDir();
    }

    public String authenticate(String username, String password) {
        return users.getOrDefault(username, null).equals(password) ? username : null;
    }

    private void createMailDir() {
        File dir = new File(MAIL_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (String user : users.keySet()) {
            File userDir = new File(dir, user);
            if (!userDir.exists()) {
                userDir.mkdir();
            }
        }
    }

    public File getUserMailDir(String username) {
        return new File(MAIL_DIR, username);
    }
}
