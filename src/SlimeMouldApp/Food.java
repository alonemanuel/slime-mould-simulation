package SlimeMouldApp;

import javafx.scene.text.Text;

import static SlimeMouldApp.SlimeMouldManager.FOOD_IMG;

public class Food extends Element {
    public static final double DEFAULT_CALORIES = 5.;
    public static final Text FOOD_TXT = new Text(FOOD_IMG);
    double _calories;

    public Food(int xPos, int yPos) {
        this(xPos, yPos, DEFAULT_CALORIES);
    }

    public Food(int xPos, int yPos, double _calories) {
        super(xPos, yPos);
        _calories = _calories;
        _text = FOOD_TXT;
    }
}
