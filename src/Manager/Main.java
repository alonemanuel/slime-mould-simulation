package Manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class which starts the app.
 */
public class Main extends Application {

	public static final String WINDOW_TITLE = "Slime Mould Everything";

	/**
	 * Launches the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Gets the graphics data from fxml file and prompts it.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		SlimeManager.log("Starting primary stage");
		Parent root = FXMLLoader.load(getClass().getResource("manager.fxml"));
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
