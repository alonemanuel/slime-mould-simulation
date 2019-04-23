package SlimeMouldApp;

// Imports //

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static SlimeMouldApp.SlimeMouldManager.MOULD_IMG;
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


//    protected Tile leftNeighbor;
//    protected Tile rightNeighbor;
//    protected Tile upNeighbor;
//    protected Tile downNeighbor;

    /**
     * Type of the tile.
     */
    private int _type;

    private Element _element;

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
     * @param xPos x position
     * @param yPos y position
     */
    public Tile(int xPos, int yPos) {
        _type = EMPTY;
        _text = new Text();
        setElement(new Empty(xPos, yPos));
//        _text.textProperty().bind(_element._text.textProperty());
        // Init rectangle
        _border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        _border.setStroke(Color.LIGHTGRAY);
        _border.setFill(null);
        getChildren().addAll(_border, _text);
        // Translate location
        setTranslateX(xPos * TILE_SIZE);
        setTranslateY(yPos * TILE_SIZE);
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
     * Set type of tile.
     *
     * @param type new type of tile.
     */
    public void setType(int type) throws SlimeMouldException {
        if (isAllowedType(type)) {
            this._type = type;
        } else {
            throw new SlimeMouldException(ILLEGAL_TYPE_ERR);
        }
    }

    public void setElement(Element element) {
        _element = element;
        _text.setText(element._text.getText());
    }

    public void makeTileDark() {
        _border.setFill(Color.DARKGRAY);
    }

    public void becomeMould() throws SlimeMouldException {
        setType(Tile.MOULD);
        setElement(new Mould(_element.get_xPos(), _element.get_yPos()));
//        getText().setText(MOULD_IMG);
    }

    public Element getElement() {
        return _element;
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
