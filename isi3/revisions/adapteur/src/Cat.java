import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Cat {
    public String fileToString(String filename) {
        ArrayList<String> fileParts = new ArrayList<>();
        File myFile = new File(filename);
        try (Scanner reader = new Scanner(myFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                fileParts.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return String.join("\n", fileParts);
    }
}
