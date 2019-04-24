package SlimeMouldApp;

// Imports //

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A class representing a UI engine capable of creating UI components.
 */
public class UI {

    // Constants //
    public static final String STAGE_TITLE = "Slime Mould Everything";
    public static final String EXIT_BUTTON_TXT = "Exit";

    public static Stage startWindow;
    public static SlimeManager manager;

    /**
     * @return header of application.
     */
    public static Parent getHeader() {
        Text headerText = new Text(STAGE_TITLE);
        StackPane header = new StackPane();
        header.getChildren().add(headerText);
        return header;
    }

    /**
     * @return footer of application.
     */
    public static Parent getFooter() {

        Button exitButton = new Button(EXIT_BUTTON_TXT);
        Button foodSearchButton = new Button("Expand");
        Button restartButton = new Button("Restart");
        foodSearchButton.setOnAction(e -> manager.update());
        exitButton.setOnAction(e -> closeProgram());
        restartButton.setOnAction(e -> manager.restart(startWindow));
        HBox footer = new HBox();
        footer.getChildren().addAll(exitButton, foodSearchButton, restartButton);
        return footer;
    }

    public static BorderPane initialize(Stage _startWindow, SlimeManager _manager) {
        manager = _manager;
        startWindow = _startWindow;
        startWindow.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        startWindow.setTitle(STAGE_TITLE);
        BorderPane borderPane = new BorderPane();
        Parent header = getHeader();
        Parent footer = getFooter();
        borderPane.setTop(header);
        borderPane.setBottom(footer);
        startWindow.setScene(new Scene(borderPane));
        return borderPane;
    }


    private static void closeProgram() {
        boolean answer = ExitMenu.display("Exit Menu", "Sure you want to exit?");
        if (answer) {
            startWindow.close();
        }
    }
}
