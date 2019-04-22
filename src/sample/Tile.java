package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static sample.SlimeMouldManager.TILE_SIZE;

public class Tile extends StackPane {

    public static final int EMPTY = 0;
    public static final int FOOD = 1;
    public static final int MOULD = 2;
    private static final int TYPES[] = {EMPTY, FOOD, MOULD};

    private int _x;
    private int _y;
    private int _type;

    private Rectangle _border;
    private Text _text;

    public Tile() {
        this(0, 0);

    }

    public Tile(int x_pos, int y_pos) {
        _x = x_pos;
        _y = y_pos;
        _type = EMPTY;
        _text = new Text(".");
        _border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        _border.setStroke(Color.LIGHTGRAY);
        _border.setFill(null);
        getChildren().addAll(_border, _text);
        setTranslateX(x_pos * TILE_SIZE);
        setTranslateY(y_pos * TILE_SIZE);
    }


    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }

    public int getType() {
        return _type;
    }

    public Text getText() {
        return _text;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

    public void setType(int type) {
        if (isAllowedType(type)) {
            this._type = _type;
        } else {
            // TODO: raise error
        }
    }

    private boolean isAllowedType(int type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == type) {
                return true;
            }
        }
        return false;
    }
}
