package SlimeMouldApp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static SlimeMouldApp.SlimeManager.REPR_SIZE;

public class Empty extends Element {

    public static final Color EMPTY_COLOR = Color.BLACK;

    public Empty(int xPos, int yPos) {
        super(xPos, yPos);
    }

    @Override
    public void setRepr() {
//        _elementRepr = new Circle();
//        ((Circle) _elementRepr).setTranslateX(_xPos * REPR_SIZE);
//        ((Circle) _elementRepr).setTranslateY(_yPos*REPR_SIZE);
//        ((Circle) _elementRepr).setRadius(REPR_SIZE / 2);
//        ((Circle) _elementRepr).setFill(EMPTY_COLOR);
        _elementRepr = new Rectangle();
        _elementRepr.setTranslateX(_xPos * REPR_SIZE);
        _elementRepr.setTranslateY(_yPos * REPR_SIZE);
        ((Rectangle) _elementRepr).setHeight(REPR_SIZE);
        ((Rectangle) _elementRepr).setWidth(REPR_SIZE);
        _elementRepr.setFill(EMPTY_COLOR);
        _elementRepr.setStroke(EMPTY_COLOR);
    }

    @Override
    public void setType() {
        _type = EMPTY_TYPE;
    }
}
