package Close;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExitMenu {

    static boolean answer;

    public static boolean display(String title, String msg) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label lbl1 = new Label();
        lbl1.setText(msg);

        Button yesButton= new Button("yes");
        Button noButton= new Button("no");

        yesButton.setOnAction(e->{
            answer = true;
            window.close();
        });
        noButton.setOnAction(e->{
            answer = false;
            window.close();
        });


        HBox layout = new HBox(10);
        layout.getChildren().addAll(lbl1, noButton, yesButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


        return answer;
    }

}
