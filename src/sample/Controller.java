package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button button1;

    public void handleButtonClick(){
        System.out.println("You just clicked me.");
        button1.setText("Stop touchin me");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Loading user data...");
    }
}
