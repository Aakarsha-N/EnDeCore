import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
public class FileService {
    // ENCRYPT FILE
    public static void encryptFile(
            File input,
            File output,
            EncryptionAlgorithm algo,
            String key,
            ProgressBar bar,
            Label status
    ) throws Exception {
        List<String> lines =
                Files.readAllLines(input.toPath());
        StringBuilder result =
                new StringBuilder();
        int total = lines.size();
        for (int i = 0; i < total; i++) {
            String encrypted =
                    algo.encrypt(lines.get(i), key);
            result.append(encrypted).append("\n");
            final double progressValue =
                    (double) (i + 1) / total;
            final int current = i + 1;
            Platform.runLater(() -> {
                bar.setProgress(progressValue);
                status.setText(
                        "Encrypting " + current + "/" + total
                );
            });
            Thread.sleep(10);
        }
        Files.writeString(
                output.toPath(),
                result.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
        Platform.runLater(() ->
                status.setText("Encryption Completed"));
    }
    // DECRYPT FILE
    public static void decryptFile(
            File input,
            File output,
            EncryptionAlgorithm algo,
            String key,
            ProgressBar bar,
            Label status
    ) throws Exception {
        List<String> lines = Files.readAllLines(input.toPath());
        StringBuilder result = new StringBuilder();
        int total = lines.size();
        for (int i = 0; i < total; i++) {
            String decrypted = algo.decrypt(lines.get(i), key);
            result.append(decrypted).append("\n");
            final double progressValue = (double) (i + 1) / total;
            final int current = i + 1;
            Platform.runLater(() -> {
                bar.setProgress(progressValue);
                status.setText("Decrypting " + current + "/" + total);
            });
            Thread.sleep(10);
        }
        Files.writeString(output.toPath(), result.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Platform.runLater(() -> status.setText("Decryption Completed"));
    }
    // READ FILE (optional helper)
    public static String readFile(File file) throws Exception {
        return Files.readString(file.toPath());
    }
    // WRITE FILE (optional helper)
    public static void writeFile(File file, String data) throws Exception {
        Files.writeString(file.toPath(), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}