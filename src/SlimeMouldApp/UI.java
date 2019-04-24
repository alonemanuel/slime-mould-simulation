package SlimeMouldApp;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class UI {

    public static final String STAGE_TITLE = "Slime Mould Everything";
    public static final String EXIT_BUTTON_TXT = "Exit";
    public static final String FOOD_SEARCH_TXT = "Search for food";
    public static final String NOT_EAT_ERR = "Err: Could not eat food.";
    public static Stage startWindow;

    public static Parent getHeader() {
        Text headerText = new Text("SLIME MOULD EVERYTHING");
        StackPane header = new StackPane();
        header.getChildren().add(headerText);
        return header;
    }

    public static Parent getFooter(){

        Button exitButton = new Button(EXIT_BUTTON_TXT);
//        Button foodSearchButton = new Button(FOOD_SEARCH_TXT);
//        foodSearchButton.setOnAction(e-> {
//            try {
//                manager.run();
//            } catch (SlimeMouldException ex) {
//                throw new SlimeMouldException(NOT_EAT_ERR);
//            }
//        });
        exitButton.setOnAction(e->closeProgram());
        HBox footer = new HBox();
        footer.getChildren().addAll(exitButton);
        return footer;
    }

    public static BorderPane initialize(Stage _startWindow) {
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
