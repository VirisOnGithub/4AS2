import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileSystem {
    private File rootDir;
    private File currentDir;

    FileSystem(File rootDir) {
        this.rootDir = rootDir;
        this.currentDir = rootDir;
    }

    void setRoot(File newRoot) {
        this.rootDir = newRoot;
        this.currentDir = newRoot;
    }

    CwdResult changeDir(String arg) {
        try {
            if (arg.equals("/")) {
                currentDir = rootDir;
                return new CwdResult(true, "/");
            }

            File candidate = new File(currentDir, arg).getCanonicalFile();
            String rootPath = rootDir.getCanonicalPath();
            String candPath = candidate.getCanonicalPath();
            if (!candPath.startsWith(rootPath)) {
                return new CwdResult(false, "Access Denied");
            }
            if (!candidate.exists() || !candidate.isDirectory()) {
                return new CwdResult(false, "Directory does not exist: " + arg);
            }
            currentDir = candidate;
            String rel = ".";
            if (!currentDir.getCanonicalPath().equals(rootPath)) {
                rel = currentDir.getCanonicalPath().substring(rootPath.length());
            }
            return new CwdResult(true, rel);
        } catch (IOException ex) {
            return new CwdResult(false, "CWD error: " + ex.getMessage());
        }
    }

    List<String> listEntries() {
        List<String> entries = new ArrayList<>();
        File[] list = currentDir.listFiles();
        if (list != null) {
            for (File f : list) {
                String name = f.getName();
                if (f.isDirectory())
                    name += "/";
                entries.add(name);
            }
        }
        return entries;
    }

    File resolveLocalFile(String name) throws IOException {
        File target = new File(currentDir, name).getCanonicalFile();
        String rootPath = rootDir.getCanonicalPath();
        String targPath = target.getCanonicalPath();
        if (!targPath.startsWith(rootPath))
            return null;
        if (!target.exists() || !target.isFile())
            return null;
        return target;
    }

    void resetToRoot() {
        currentDir = rootDir;
    }

    static final class CwdResult {
        final boolean success;
        final String message;

        CwdResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
