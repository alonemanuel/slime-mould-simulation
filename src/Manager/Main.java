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
        Parent root = FXMLLoader.load(getClass().getResource("Manager/manager.fxml"));
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root));
//        primaryStage.sizeToScene();
        primaryStage.show();
//        primaryStage.setMinWidth(primaryStage.getWidth());
//        primaryStage.setMinHeight(primaryStage.getHeight());
//
//        this.stage = primaryStage;
//
//        Parent root = FXMLLoader.load(getClass().getResource("SlimeMouldApp.fxml"));
//        primaryStage.setTitle("Hello world");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
////
//
//        window = primaryStage;
//        window.setTitle(PROJECT_NAME);
//        window.setOnCloseRequest(e -> {
//            e.consume();
//            closeProgram();
//        });
//        HBox topMenu = new HBox();
//        Button buttonA = new Button("File");
//        Button buttonB = new Button("Edit");
//        Button buttonC = new Button("View");
//        topMenu.getChildren().addAll(buttonA, buttonB, buttonC);
//
//        VBox leftMenu = new VBox();
//        Button buttonD = new Button("CHANGE STYLE");
////        buttonD.setOnAction(e->{
////            scene2.getStylesheets().add("./SlimeMouldApp/Viper.css");
////        });
//
//        Button buttonE = new Button("E");
//        Button buttonF = new Button("F");
//        leftMenu.getChildren().addAll(buttonD, buttonE, buttonF);
//
//        Button exitButton = new Button("Exit");
//        exitButton.setOnAction(e -> closeProgram());
//        // layout 2
//        StackPane bottomMenu = new StackPane();
//        bottomMenu.getChildren().add(exitButton);
//
//
//        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(topMenu);
//        borderPane.setLeft(leftMenu);
//        borderPane.setBottom(bottomMenu);
//
//        scene2 = new Scene(borderPane, 600, 300);
//
//
//        window.setScene(scene2);
//        window.setTitle(PROJECT_NAME);
//        window.show();
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
