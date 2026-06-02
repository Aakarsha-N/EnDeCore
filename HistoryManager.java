import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
public class HistoryManager {
    private static final String BASE_DIR = "users";
    // ADD HISTORY (SAVE TO FILE)
    public static void add(String user, String input, String output) {
        try {
            Path userDir = Paths.get(BASE_DIR, user);
            Files.createDirectories(userDir);
            Path file = userDir.resolve("history.txt");
            String entry = java.time.LocalDateTime.now()
                            + " | " + input
                            + " → " + output
                            + System.lineSeparator();
            Files.writeString(file, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD HISTORY (ON LOGIN)
    public static List<String> load(String user) {
        List<String> history = new ArrayList<>();
        try {
            Path file = Paths.get(BASE_DIR, user, "history.txt");
            if (!Files.exists(file)) {
                return history;
            }
            history = Files.readAllLines(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    // CLEAR HISTORY
    public static void clear(String user) {
        try {
            Path file = Paths.get(BASE_DIR, user, "history.txt");
            Files.writeString(file, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}