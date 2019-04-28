package Logic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static Manager.SlimeManager.REPR_SIZE;

/**
 * Class representing an element.
 */
public abstract class Element {

	/**
	 * Type enumerations.
	 */
	public static final int EMPTY_TYPE = 0;
	public static final int FOOD_TYPE = 1;
	public static final int MOULD_TYPE = 2;
	public static final double SAT_BRIGHT_FACTOR = 0.95;
	public static final double SAT_OPAQ_FACTOR = 1.4;
	public static final double DESAT_BRIGHT_FACTOR = 1.001;
	public static final double DESAT_OPAQ_FACTOR = 0.99;

	/**
	 * X position of element.
	 */
	public int _xPos;
	/**
	 * Y position of element.
	 */
	public int _yPos;
	/**
	 * Type enumeration of element.
	 */
	protected int _type;
	/**
	 * Element representation.
	 */
	protected Rectangle _elementRepr;

	/**
	 * Constructor.
	 */
	public Element(int xPos, int yPos) {
		_xPos = xPos;
		_yPos = yPos;
		setType();
		setRepr();
	}

	/**
	 * @return Returns element representation.
	 */
	public Shape getElementRepr() {
		return _elementRepr;
	}

	/**
	 * Sets the dimensions of the element.
	 */
	public void setReprDim() {
		_elementRepr = new Rectangle();
		_elementRepr.setTranslateX(_xPos * REPR_SIZE);
		_elementRepr.setTranslateY(_yPos * REPR_SIZE);
		_elementRepr.setHeight(REPR_SIZE);
		_elementRepr.setWidth(REPR_SIZE);
	}

	/**
	 * Sets representation.
	 */
	public abstract void setRepr();

	/**
	 * Sets the type of the element.
	 */
	public abstract void setType();

	/**
	 * @return Type of element.
	 */
	public int getType() {
		return _type;
	}

	/**
	 * X position of element.
	 *
	 * @return
	 */
	public int getXPos() {
		return _xPos;
	}

	/**
	 * Y position of element.
	 *
	 * @return
	 */
	public int getYPos() {
		return _yPos;
	}

	/**
	 * Saturates element's representation.
	 */
	public void saturate() {
		Color color = (Color) _elementRepr.getFill();
		_elementRepr.setFill(color.deriveColor(0, 1, 1, SAT_OPAQ_FACTOR));
		_elementRepr.setStroke(color.deriveColor(0, 1, 1, SAT_OPAQ_FACTOR));
	}

	/**
	 * Desaturates element's representation.
	 */
	public void desaturate() {
		Color color = (Color) _elementRepr.getFill();
		_elementRepr.setFill(color.deriveColor(0, 1, 1, DESAT_OPAQ_FACTOR));
		_elementRepr.setStroke(color.deriveColor(0, 1, 1, DESAT_OPAQ_FACTOR/2));
	}
}
