package Logic;

import javafx.scene.paint.Color;

/**
 * Class representing food.
 */
public class Food extends Element {
	public static final Color FOOD_COLOR = Color.SANDYBROWN;

	public Food(int xPos, int yPos) {
		super(xPos, yPos);
	}

	/**
	 * Sets representation of food.
	 */
	@Override
	public void setRepr() {
		setReprDim();
		_elementRepr.setFill(FOOD_COLOR);
		_elementRepr.setStroke(FOOD_COLOR);
	}

	/**
	 * Sets type of food.
	 */
	@Override
	public void setType() {
		_type = FOOD_TYPE;
	}
}
