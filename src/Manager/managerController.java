package Manager;

import Close.ExitMenu;
import Logic.Mould;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class managerController implements Initializable {
    public static final String TKY = "Tokyo";
    public static final String TLV = "Tel Aviv";
    public static final String JLM = "Jerusaslem";
    public static final String PRS = "Paris";
    public static final String OWN = "Add your own";
    public static final String RDM = "Randomize";
    public Button button1;
    @FXML
    BorderPane borderPane;
    SlimeManager manager;
    @FXML
    ComboBox foodChoiceBox;
    @FXML
    Pane pane;
    @FXML
    Button exitButton;
    @FXML
    Button restartButton;


    ObservableList<String> foodChoiceList = FXCollections.observableArrayList(TKY, TLV,
            JLM, PRS, OWN, RDM);

    public void closeProgram() {
        boolean answer = ExitMenu.display("Exit Menu", "Sure you want to exit?");
        if (answer) {
            ((Stage) exitButton.getScene().getWindow()).close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SlimeManager.log("Loading user data");
        manager = new SlimeManager(pane);
        foodChoiceBox.setItems(foodChoiceList);
        manager.populateElements();

    }

    public void updateManager() {
        manager.update();

    }

    public void placeFood() {
        if (foodChoiceBox.getValue() == null) {

            return;
        }
        switch ((String)foodChoiceBox.getValue()) {
            case RDM:
                manager.populateFood();
        }
    }

    public void placeMould() {
        manager.placeMould();

    }

    public void restartButton() {
        pane.getChildren().clear();
        Mould.restart();
        manager.restart();
        foodChoiceBox.setValue(null);
        foodChoiceBox.setPromptText("Place found");
        manager = new SlimeManager(pane);
        manager.populateElements();
    }
}
