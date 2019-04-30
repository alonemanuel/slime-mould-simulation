package Logic;

import javafx.scene.paint.Color;

public class Empty extends Element {

    public static final Color EMPTY_COLOR = Color.BLACK;

    public Empty(int xPos, int yPos) {
        super(xPos, yPos);
    }

    @Override
    public void setRepr() {
        setReprDim();
        _elementRepr.setFill(EMPTY_COLOR.deriveColor(1,1,1,0.7));
        _elementRepr.setStroke(EMPTY_COLOR.deriveColor(1,1,1,0.7));
    }

    @Override
    public void setType() {
        _type = EMPTY_TYPE;
    }
}
