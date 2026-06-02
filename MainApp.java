import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        AuthService auth = new AuthService();
        if (!auth.loginScreen()) {
            System.exit(0);
        }
        MainController controller = new MainController();
        Scene scene = new Scene(controller.getView(), 900, 650);
        stage.setTitle("EnDeCrypt");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}