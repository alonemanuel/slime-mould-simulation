package SlimeMouldApp;

// Imports //

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static SlimeMouldApp.SlimeMouldManager.TILE_SIZE;

/**
 * A class representing a tile, which is the building block of a bigger tile-made world.
 */
public class Tile extends StackPane {
    // Fields //
    /**
     * Types of tiles.
     */
    public static final int EMPTY = 0;
    public static final int FOOD = 1;
    public static final int MOULD = 2;
    private static final int TYPES[] = {EMPTY, FOOD, MOULD};
    public static final String ILLEGAL_TYPE_ERR = "Err: Illegal type.";

    /**
     * 2D location of tile.
     */
    private int _x;
    private int _y;
    /**
     * Type of the tile.
     */
    private int _type;
    /**
     * Border of the tile.
     */
    private Rectangle _border;
    /**
     * Text inside the tile.
     */
    private Text _text;

    // Methods //

    /**
     * Default Ctor for the tile.
     */
    public Tile() {
        this(0, 0);

    }

    /**
     * Ctor for a tile.
     *
     * @param x_pos x position
     * @param y_pos y position
     */
    public Tile(int x_pos, int y_pos) {
        _x = x_pos;
        _y = y_pos;
        _type = EMPTY;
        _text = new Text(".");
        // Init rectangle
        _border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        _border.setStroke(Color.LIGHTGRAY);
        _border.setFill(null);
        getChildren().addAll(_border, _text);
        // Translate location
        setTranslateX(x_pos * TILE_SIZE);
        setTranslateY(y_pos * TILE_SIZE);
    }

    /**
     * @return x location.
     */
    public int getX() {
        return _x;
    }

    /**
     * @return y location.
     */
    public int getY() {
        return _y;
    }

    /**
     * @return type of tile.
     */
    public int getType() {
        return _type;
    }

    /**
     * @return text of tile.
     */
    public Text getText() {
        return _text;
    }

    /**
     * Set x location.
     *
     * @param _x new x loc.
     */
    public void setX(int _x) {
        this._x = _x;
    }

    /**
     * Set y location.
     *
     * @param _y new y loc.
     */
    public void setY(int _y) {
        this._y = _y;
    }

    /**
     * Set type of tile.
     *
     * @param type new type of tile.
     */
    public void setType(int type) throws Exception {
        if (isAllowedType(type)) {
            this._type = type;
        } else {
            throw new Exception(ILLEGAL_TYPE_ERR);
        }
    }

    // Helpers //

    /**
     * @param type type to check.
     * @return true iff type is allowed.
     */
    private boolean isAllowedType(int type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i] == type) {
                return true;
            }
        }
        return false;
    }
}
