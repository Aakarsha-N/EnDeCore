import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class AuthService {
    private static final Path USERS_FILE = Paths.get("data", "users.txt");
    private HashMap<String, String> users = new HashMap<>();
    public boolean loginScreen() {
        try {
            ChoiceDialog<String> choice = new ChoiceDialog<>("Login", "Login", "Sign Up");
            choice.setHeaderText("Select Option");
            String option = choice.showAndWait().orElse("Login");
            TextInputDialog userDialog = new TextInputDialog();
            userDialog.setHeaderText("Username");
            String username = userDialog.showAndWait().orElse("");
            TextInputDialog passDialog = new TextInputDialog();
            passDialog.setHeaderText("Password");
            String password = passDialog.showAndWait().orElse("");
            if (username.isEmpty() || password.isEmpty()) {
                showMessage("Username and Password required.");
                return false;
            }
            if (option.equals("Sign Up")) {
                signup(username, password);
                showMessage("Account created successfully.\nPlease login.");
                return false;
            }
            if (login(username, password)) {
                showMessage("Login Successful!");
                return true;
            }
            showMessage("Invalid username or password.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Authentication Error");
            return false;
        }
    }

    private void loadUsers() throws Exception {
        users.clear();
        if (!Files.exists(USERS_FILE)) {
            return;
        }
        List<String> lines = Files.readAllLines(USERS_FILE);
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                users.put(parts[0], parts[1]);
            }
        }
    }

    private void signup(String username, String password) throws Exception {
        loadUsers();
        if (users.containsKey(username)) {
            showMessage("Username already exists!");
            return;
        }
        String hash = PasswordUtil.hash(password);
        Files.createDirectories(Paths.get("data", username));
        Files.writeString(USERS_FILE, username + ":" + hash + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private boolean login(String username, String password) throws Exception {
        loadUsers();
        if (!users.containsKey(username)) {
            return false;
        }
        String storedHash = users.get(username);
        String enteredHash = PasswordUtil.hash(password);
        if (storedHash.equals(enteredHash)) {
            Session.setCurrentUser(username);
            return true;
        }
        return false;
    }
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}