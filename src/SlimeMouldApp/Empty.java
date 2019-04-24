package SlimeMouldApp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static SlimeMouldApp.SlimeManager.REPR_SIZE;

public class Empty extends Element {

    public static final Color EMPTY_COLOR = Color.LIGHTGRAY;

    public Empty(int xPos, int yPos) {
        super(xPos, yPos);
    }

    @Override
    public void setElementRepr() {
        _elementRepr = new Circle();
        ((Circle) _elementRepr).setTranslateX(_xPos * REPR_SIZE);
        ((Circle) _elementRepr).setTranslateY(_yPos*REPR_SIZE);
        ((Circle) _elementRepr).setRadius(REPR_SIZE / 2);
        ((Circle) _elementRepr).setFill(EMPTY_COLOR);
    }

    @Override
    public void setType() {
        _type = EMPTY_TYPE;
    }
}
