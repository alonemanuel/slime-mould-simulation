package Logic;

import javafx.scene.paint.Color;

import java.util.Random;

import static Manager.SlimeManager.log;

/**
 * A class representing a mould element.
 */
public class Mould extends Element {

	// Constants //
	/**
	 * Move consts.
	 */
	public static final int RIGHT_MOVE = 1;
	public static final int LEFT_MOVE = -1;
	public static final int DOWN_MOVE = 1;
	public static final int UP_MOVE = -1;
	/**
	 * Default color for mould.
	 */
	public static final Color MOULD_COLOR = Color.YELLOW;

	// Statics //
	/**
	 * The head of the entire mould.
	 */
	/**
	 * True iff the mould has encountered food.
	 */
	private static boolean hasFoundFood;

	public int timesPast;
	public static int maxTimesPast;

	// Methods //

	/**
	 * Ctor.
	 *
	 * @param xPos x position of the mould.
	 * @param yPos y position of the mould.
	 */
	public Mould(int xPos, int yPos) {
		super(xPos, yPos);
		// Inits the mould head to be the first created mould.
			hasFoundFood = false;
		timesPast = 1;
	}

	/**
	 * @return mould head.
	 */


	public static void restart() {
		hasFoundFood = false;
	}

	/**
	 * @return true iff mould has found food.
	 */
	public static boolean didFindFood() {
		return hasFoundFood;
	}

	/**
	 * Sets the hasFoundFood field to true.
	 */
	public static void setFoundFood(boolean didFind) {
		hasFoundFood = didFind;
	}

	/**
	 * set type of element.
	 */
	@Override
	public void setType() {
		_type = MOULD_TYPE;
	}

	/**
	 * Set representation of element.
	 */
	@Override
	public void setRepr() {
		setReprDim();
		_elementRepr.setFill(MOULD_COLOR);
		_elementRepr.setStroke(MOULD_COLOR);
	}

	/**
	 * @param randPicker predefined random picker.
	 * @return generated x move.
	 */
	public int generateXMove(boolean randPicker, Mould head) {
		int xMove = isLeftOfHead(head) ? LEFT_MOVE : RIGHT_MOVE;
		return randPicker ? xMove : 0;
	}

	/**
	 * @param randPicker predefined random picker.
	 * @return generated y move.
	 */
	public int generateYMove(boolean randPicker, Mould head) {
		int yMove = isBelowHead(head) ? DOWN_MOVE : UP_MOVE;
		return randPicker ? 0 : yMove;
	}

	/**
	 * @return true if mould is left of headMould, random if mould is headMould.
	 */
	private boolean isLeftOfHead(Mould head) {
		Random rand = new Random();
		return (_xPos == head.getXPos()) ? rand.nextBoolean() : _xPos < head.getXPos();
	}

	/**
	 * @return true if mould is below headMould, random if mould is headMould.
	 */
	private boolean isBelowHead(Mould head) {
		Random rand = new Random();
		return (_yPos == head.getYPos()) ? rand.nextBoolean() : _yPos > head.getYPos();
	}
}