package Logic;

import javafx.scene.paint.Color;

public class Food extends Element {
    public static final double DEFAULT_CALORIES = 5.;
    public static final Color FOOD_COLOR = Color.DARKGREEN;
    double _calories;

    public Food(int xPos, int yPos) {
        this(xPos, yPos, DEFAULT_CALORIES);
    }

    public Food(int xPos, int yPos, double calories) {
        super(xPos, yPos);
        _calories = calories;
    }

    @Override
    public void setRepr() {
        setReprDim();
        _elementRepr.setFill(FOOD_COLOR);
        _elementRepr.setStroke(FOOD_COLOR);
    }


    @Override
    public void setType() {
        _type = FOOD_TYPE;
    }
}
