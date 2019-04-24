package SlimeMouldApp;

// Imports //

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Random;

import static SlimeMouldApp.SlimeManager.REPR_SIZE;

/**
 * A class representing a mould element.
 */
public class Mould extends Element {

    // Constants //
    public static final int RIGHT_MOVE = 1;
    public static final int LEFT_MOVE = -1;
    public static final String NULL_HEAD_ERR = "Err: Null mould head.";
    public static final int DOWN_MOVE = 1;
    public static final int UP_MOVE = -1;
    public static final String ADJ_NEIGHBOR_ERR = "Err: Neighbor must be adjacent.";
    public static final int DEFAULT_ENERGY = 10;
    public static final double ENERGY_TRANSFER_FACTOR = 0.2;
    public static final Color MOULD_COLOR = Color.YELLOW;

    // Statics //
    /**
     * The head of the entire mould.
     */
    public static Mould mouldHead;
    /**
     * True iff the mould has encountered food.
     */
    static boolean hasFoundFood;

    // Fields //
    /**
     * Energy attribute of the mould. Affects it's darkness.
     */
    private double _energy;

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
        if (mouldHead == null) {
//            _elementRepr.setFill();   // TODO headMould
            mouldHead = this;
            hasFoundFood = false;
        }
        _energy = DEFAULT_ENERGY;
    }

    @Override
    public void setType() {
        _type = MOULD_TYPE;
    }

    @Override
    public void setElementRepr() {
        _elementRepr = new Circle();
        ((Circle) _elementRepr).setTranslateX(_xPos*REPR_SIZE);
        ((Circle) _elementRepr).setTranslateY(_yPos*REPR_SIZE);
        ((Circle) _elementRepr).setRadius(REPR_SIZE / 2);
        ((Circle) _elementRepr).setFill(MOULD_COLOR);
    }

    public int generateXMove(boolean randPicker) {
        int xMove = isLeftOfHead() ? LEFT_MOVE : RIGHT_MOVE;
        return randPicker ? xMove : 0;
    }

    public int generateYMove(boolean randPicker) {
        int yMove = isBelowHead() ? DOWN_MOVE : UP_MOVE;
        return randPicker ? 0 : yMove;
    }


    /**
     * Gives energy to given mould.
     *
     * @param other mould to give energy to.
     */
    private void giveEnergyTo(Mould other) {
//        double energyToGive = ENERGY_TRANSFER_FACTOR * _energy;
        double energyToGive = ENERGY_TRANSFER_FACTOR;
        _energy -= energyToGive;
        other.setEnergy(other.getEnergy() + energyToGive);
    }

    public boolean didFindFood() {
        return hasFoundFood;
    }

    public void setHasFoundFood() {
        hasFoundFood = true;
    }

    /**
     * Gets energy from given mould.
     *
     * @param other mould to get energy from.
     */
    private void getEnergyFrom(Mould other) {
        //        double energyToGive = ENERGY_TRANSFER_FACTOR * _energy;
        double energyToGet = ENERGY_TRANSFER_FACTOR;
        _energy += energyToGet;
        other.setEnergy(other.getEnergy() - energyToGet);
    }

    /**
     * Set energy of mould.
     *
     * @param energy energy to set.
     */
    public void setEnergy(double energy) {
        this._energy = energy;
    }

    /**
     * Get energy of mould.
     *
     * @return energy of mould.
     */
    public double getEnergy() {
        return _energy;
    }


    /**
     * @return true if mould is left of headMould, random if mould is headMould.
     */
    private boolean isLeftOfHead() throws SlimeMouldException {
        Random rand = new Random();
        if (mouldHead == null) {
            throw new SlimeMouldException(NULL_HEAD_ERR);
        }
        return (_xPos == mouldHead.getXPos()) ? rand.nextBoolean() : _xPos < mouldHead.getXPos();
    }

    /**
     * @return true if mould is below headMould, random if mould is headMould.
     */
    private boolean isBelowHead() throws SlimeMouldException {
        Random rand = new Random();
        if (mouldHead == null) {
            throw new SlimeMouldException(NULL_HEAD_ERR);
        }
        return (_yPos == mouldHead.getYPos()) ? rand.nextBoolean() : _yPos > mouldHead.getYPos();
    }

}
