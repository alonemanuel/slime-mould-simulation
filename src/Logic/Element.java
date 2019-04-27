package Logic;

import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public abstract class Element {

    public static final int EMPTY_TYPE = 0;
    public static final int FOOD_TYPE = 1;
    public static final int MOULD_TYPE = 2;


    public Text _text;
    public int _xPos;
    public int _yPos;
    protected int _type;
    protected Shape _elementRepr;

    public Element(int xPos, int yPos) {
        _xPos = xPos;
        _yPos = yPos;
        setType();
        setRepr();
    }

    public Shape getElementRepr() {
        return _elementRepr;
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
}
