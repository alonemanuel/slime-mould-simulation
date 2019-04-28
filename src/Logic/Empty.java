package Logic;

import javafx.scene.paint.Color;

public class Empty extends Element {

    public static final Color EMPTY_COLOR = Color.LIGHTGRAY;

    public Empty(int xPos, int yPos) {
        super(xPos, yPos);
    }

    @Override
    public void setRepr() {
        setReprDim();
        _elementRepr.setFill(EMPTY_COLOR);
        _elementRepr.setStroke(EMPTY_COLOR);
    }

    @Override
    public void setType() {
        _type = EMPTY_TYPE;
    }
}
