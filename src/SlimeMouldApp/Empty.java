package SlimeMouldApp;

import javafx.scene.text.Text;

public class Empty extends Element {

    public static final String EMPTY_IMG = ".";
    public static final Text EMPTY_TXT = new Text(EMPTY_IMG);

    public Empty(int xPos, int yPos) {
        super(xPos, yPos);
        _text = EMPTY_TXT;
    }
}
