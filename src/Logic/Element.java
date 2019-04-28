package Logic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import static Manager.SlimeManager.REPR_SIZE;

public abstract class Element {

    public static final int EMPTY_TYPE = 0;
    public static final int FOOD_TYPE = 1;
    public static final int MOULD_TYPE = 2;


    public Text _text;
    public int _xPos;
    public int _yPos;
    protected int _type;
    protected Rectangle _elementRepr;

    public Element(int xPos, int yPos) {
        _xPos = xPos;
        _yPos = yPos;
        setType();
        setRepr();
    }

    public Shape getElementRepr() {
        return _elementRepr;
    }

    public void setReprDim() {
        _elementRepr = new Rectangle();
        _elementRepr.setTranslateX(_xPos * REPR_SIZE);
        _elementRepr.setTranslateY(_yPos * REPR_SIZE);
        _elementRepr.setHeight(REPR_SIZE);
        _elementRepr.setWidth(REPR_SIZE);
    }

    public abstract void setRepr();

    public abstract void setType();

    public int getType() {
        return _type;
    }

    public int getXPos() {
        return _xPos;
    }

    public int getYPos() {
        return _yPos;
    }

    public void set_xPos(int _xPos) {
        this._xPos = _xPos;
    }

    public void set_yPos(int _yPos) {
        this._yPos = _yPos;
    }

    public void saturate() {
        Color color = (Color) _elementRepr.getFill();
        _elementRepr.setFill(color.deriveColor(0, 1, 0.95, 1.4));
        _elementRepr.setStroke(color.deriveColor(0, 1, 0.95, 1.4));

    }

    public void desaturate() {
        Color color = (Color) _elementRepr.getFill();
        _elementRepr.setFill(color.deriveColor(0, 1, 1.001, 0.99));
        _elementRepr.setStroke(color.deriveColor(0, 1, 1.001, 0.99));

    }
}
