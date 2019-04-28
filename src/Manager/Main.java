package Manager;

import Close.ExitMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String PROJECT_NAME = "Slime Mould2 Everything";
    public static final String WINDOW_TITLE = "Slime Logic.Mould Everything";
    Stage window;
    Scene scene1, scene2;
    Button button;

    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting primary stage...");
        Parent root = FXMLLoader.load(getClass().getResource("manager.fxml"));
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    void closeProgram() {
        boolean answer = ExitMenu.display("Exit Menu", "Sure you want to exit?");
        if (answer) {

            System.out.println("File is saved!");
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
