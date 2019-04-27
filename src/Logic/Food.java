package Logic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static Manager.SlimeManager.REPR_SIZE;

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
//        _elementRepr = new Circle();
//        ((Circle) _elementRepr).setTranslateX(_xPos*REPR_SIZE);
//        ((Circle) _elementRepr).setTranslateY(_yPos*REPR_SIZE);
//        ((Circle) _elementRepr).setRadius(REPR_SIZE / 2);
//        ((Circle) _elementRepr).setFill(FOOD_COLOR);
        _elementRepr = new Rectangle();
        _elementRepr.setTranslateX(_xPos * REPR_SIZE);
        _elementRepr.setTranslateY(_yPos * REPR_SIZE);
        ((Rectangle) _elementRepr).setHeight(REPR_SIZE);
        ((Rectangle) _elementRepr).setWidth(REPR_SIZE);
        _elementRepr.setFill(FOOD_COLOR);
    }


    @Override
    public void setType() {
        _type = FOOD_TYPE;
    }
}
