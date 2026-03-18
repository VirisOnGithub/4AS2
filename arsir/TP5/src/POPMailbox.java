import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class POPMailbox {
    private final File path;
    private List<File> messages;
    public Set<Integer> deletedMessages;

    public POPMailbox(File path) {
        assert path != null : "Path cannot be null";
        this.path = path;
        this.messages = new ArrayList<>();
        this.deletedMessages = new HashSet<>();
        
        loadMessages();
    }

    private void loadMessages() {
        if (!path.exists() || !path.isDirectory()) {
            path.mkdir();
        }

        File[] files = path.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
            this.messages = Arrays.asList(files);
        }
    }

    public int getMessageCount() {
        return messages.size() - deletedMessages.size();
    }

    public long getTotalSize() {
        return messages.stream()
                .filter(file -> !deletedMessages.contains(messages.indexOf(file)))
                .mapToLong(File::length)
                .sum();
    }

    public List<String> listMessages() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            if (!deletedMessages.contains(i)) {
                File file = messages.get(i);
                result.add((i + 1) + " " + file.length());
            }
        }
        return result;
    }

    public String getMessageInfo(int messageNumber) {
        int index = messageNumber - 1;
        if (index < 0 || index >= messages.size() || deletedMessages.contains(index)) {
            return null;
        }
        File file = messages.get(index);
        return messageNumber + " " + file.length();
    }

    public String getMessageContent(int messageNumber) throws IOException {
        int index = messageNumber - 1;
        if (index < 0 || index >= messages.size() || deletedMessages.contains(index)) {
            return null;
        }

        File file = messages.get(index);
        return readFileContent(file);
    }

    public String getMessageTopLines(int messageNumber, int lineCount) throws IOException {
        int index = messageNumber - 1;
        if (index < 0 || index >= messages.size() || deletedMessages.contains(index)) {
            return null;
        }

        File file = messages.get(index);
        return readTopLines(file, lineCount);
    }

    public boolean deleteMessage(int messageNumber) {
        int index = messageNumber - 1;
        if (index < 0 || index >= messages.size()) {
            return false;
        }

        if (!deletedMessages.contains(index)) {
            deletedMessages.add(index);
            return true;
        }

        return false;
    }

    public void resetDeletions() {
        deletedMessages.clear();
    }

    public void commitDeletions() throws IOException {
        // Trier les indices en ordre décroissant pour éviter les problèmes d'indices
        List<Integer> sortedIndices = new ArrayList<>(deletedMessages);
        Collections.sort(sortedIndices, Collections.reverseOrder());

        for (int index : sortedIndices) {
            if (index < messages.size()) {
                File file = messages.get(index);
                if (file.exists()) {
                    if (!file.delete()) {
                        System.err.println("Impossible de supprimer: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private String readFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    private String readTopLines(File file, int lineCount) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < lineCount) {
                result.append(line).append("\n");
                count++;
            }
        }
        return result.toString().trim();
    }
}
