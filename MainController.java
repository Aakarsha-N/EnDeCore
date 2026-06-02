import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.input.TransferMode;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
public class MainController {
    // =========================
    // USER + HISTORY
    // =========================
    private String currentUser = Session.getCurrentUser();
    private ListView<String> historyView = new ListView<>();
    // =========================
    // UI ROOT
    // =========================
    private VBox root = new VBox(10);
    private Label userLabel = new Label();
    private ComboBox<String> algoBox = new ComboBox<>();
    private TextField shiftField = new TextField("3");
    private TextField keyField = new TextField();
    private CheckBox ignoreSpaces = new CheckBox("Ignore Spaces");
    private CheckBox ignoreSymbols = new CheckBox("Ignore Symbols");
    private TextArea input = new TextArea();
    private TextArea output = new TextArea();
    private Label status = new Label("Ready");
    // =========================
    // CONSTRUCTOR
    // =========================
    public MainController() {
        userLabel.setText("User: " + currentUser);
        algoBox.getItems().addAll("Caesar", "Vigenere");
        algoBox.setValue("Caesar");
        shiftField.setPromptText("Shift (1-25)");
        keyField.setPromptText("Vigenere Key (letters only)");
        updateFields();
        algoBox.setOnAction(e -> updateFields());
        // Buttons
        Button encryptBtn = new Button("Encrypt");
        Button decryptBtn = new Button("Decrypt");
        Button clearBtn = new Button("Clear");
        Button saveBtn = new Button("Save Output");
        Button logoutBtn = new Button("Logout");
        Button genKeyBtn = new Button("Generate Key");
        encryptBtn.setOnAction(e -> encrypt());
        decryptBtn.setOnAction(e -> decrypt());
        clearBtn.setOnAction(e -> {
            input.clear();
            output.clear();
            status.setText("Cleared");
        });
        logoutBtn.setOnAction(e -> System.exit(0));
        genKeyBtn.setOnAction(e -> keyField.setText(generateKey()));
        saveBtn.setOnAction(e -> saveOutput());
        setupDragDrop();
        // Layout
        HBox options = new HBox(10, shiftField, keyField, ignoreSpaces, ignoreSymbols);
        HBox controls = new HBox(10, encryptBtn, decryptBtn, clearBtn, saveBtn, genKeyBtn, logoutBtn);
        // HISTORY LOAD
        loadHistory();
        // ADD TO ROOT
        root.getChildren().addAll(
                userLabel,
                algoBox,
                options,
                input,
                output,
                controls,
                status,
                new Label("History"),
                historyView
        );
    }
    // VIEW
    public VBox getView() {
        return root;
    }
    // UPDATE FIELDS
    private void updateFields() {
        boolean isCaesar = algoBox.getValue().equals("Caesar");
        shiftField.setVisible(isCaesar);
        keyField.setVisible(!isCaesar);
    }
    // ENCRYPT
    private void encrypt() {
        String text = preprocess(input.getText());
        try {
            if (algoBox.getValue().equals("Caesar")) {
                int shift = Integer.parseInt(shiftField.getText());
                EncryptionAlgorithm algo = new CaesarCipher(shift);
                String result = algo.encrypt(text, "");
                output.setText(result);
                HistoryManager.add(currentUser, text, result);
                loadHistory();
            } else {
                String key = keyField.getText();
                if (!key.matches("[a-zA-Z]+")) {
                    status.setText("Key must contain letters only");
                    return;
                }
                EncryptionAlgorithm algo = new VigenereCipher();
                String result = algo.encrypt(text, key);
                output.setText(result);
                HistoryManager.add(currentUser, text, result);
                loadHistory();
            }
            status.setText("Encrypted");
        } catch (Exception e) {
            status.setText("Error encrypting");
        }
    }
    // DECRYPT
    private void decrypt() {
        String text = preprocess(input.getText());
        try {
            if (algoBox.getValue().equals("Caesar")) {
                int shift = Integer.parseInt(shiftField.getText());
                EncryptionAlgorithm algo = new CaesarCipher(shift);
                String result = algo.decrypt(text, "");
                output.setText(result);
                HistoryManager.add(currentUser, text, result);
                loadHistory();
            } else {
                String key = keyField.getText();
                EncryptionAlgorithm algo = new VigenereCipher();
                String result = algo.decrypt(text, key);
                output.setText(result);
                HistoryManager.add(currentUser, text, result);
                loadHistory();
            }
            status.setText("Decrypted");
        } catch (Exception e) {
            status.setText("Error decrypting");
        }
    }
    // PREPROCESS TEXT
    private String preprocess(String text) {
        if (text == null) return "";
        if (ignoreSpaces.isSelected()) {
            text = text.replace(" ", "");
        }
        if (ignoreSymbols.isSelected()) {
            text = text.replaceAll("[^a-zA-Z0-9 ]", "");
        }
        return text;
    }
    // GENERATE KEY
    private String generateKey() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(letters.charAt(
                    r.nextInt(letters.length())
            ));
        }
        return sb.toString();
    }
    // SAVE OUTPUT
    private void saveOutput() {
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));
            File file = fc.showSaveDialog(null);
            if (file != null) {
                Files.writeString(file.toPath(), output.getText(), StandardOpenOption.CREATE);
                status.setText("Saved");
            }
        } catch (Exception e) {
            status.setText("Save failed");
        }
    }
    // DRAG & DROP
    private void setupDragDrop() {
        input.setOnDragOver(e -> {
            if (e.getDragboard().hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
        });
        input.setOnDragDropped(e -> {
            try {
                File file = e.getDragboard().getFiles().get(0);
                input.setText(Files.readString(file.toPath()));
                status.setText("File loaded");
            } catch (Exception ex) {
                status.setText("Load failed");
            }
        });
    }
    // HISTORY LOAD
    private void loadHistory() {
        List<String> data = HistoryManager.load(currentUser);
        historyView.getItems().setAll(data);
    }
}