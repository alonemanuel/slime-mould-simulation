package SlimeMouldApp;

// Imports //

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

import static SlimeMouldApp.SlimeManager.REPR_SIZE;

/**
 * A class representing a mould element.
 */
public class Mould extends Element {


    // Constants //
    public static final int MAX_ENERGY = 10;
    public static final double vitalityFactor = 0.2;
    public static final double dyingFactor = -0.1;
    public static final double headThreshold = 6;


    public static final int RIGHT_MOVE = 1;
    public static final int LEFT_MOVE = -1;
    public static final int DOWN_MOVE = 1;
    public static final int UP_MOVE = -1;
    public static final String NULL_HEAD_ERR = "Err: Null mould head.";
    public static final String ADJ_NEIGHBOR_ERR = "Err: Neighbor must be adjacent.";

    public static final int DEFAULT_ENERGY = 5;
    public static final double ENERGY_LOSS_FACTOR = 0.2;
    public static final Color MOULD_COLOR = Color.rgb(30, 30, 0);
    public static final Color MOULD_HEAD_COLOR = Color.rgb(182, 181, 0);

    // Statics //
    /**
     * True iff the mould has encountered food.
     */
    public static boolean hasFoundFood;
    /**
     * The head of the entire mould.
     */
    private static Mould mouldHead;

    // Fields //
    /**
     * Energy attribute of the mould. Affects it's darkness.
     */
    private double _energy;

    private DoubleProperty _energyP;

    // Methods //

    public Mould(Node node) {
        this(node.xPos, node.yPos);
    }

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
            _elementRepr.setFill(MOULD_HEAD_COLOR);
            mouldHead = this;
            hasFoundFood = false;
        }
        initEnergy();
    }

    private void initEnergy() {
        _energyP = new SimpleDoubleProperty(DEFAULT_ENERGY);
        _energyP.addListener((v, oldValue, newValue) -> {
            int rg = 255 * _energyP.intValue() / MAX_ENERGY;
            Color newColor = Color.rgb(rg, rg, 0);
            _elementRepr.setFill(newColor);
            _elementRepr.setStroke(newColor);
        });
    }

    /**
     * @return mould head.
     */
    public static Mould getMouldHead() {
        return mouldHead;
    }

    public static void restart() {
        mouldHead = null;
        hasFoundFood = false;
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
//        _elementRepr = new Circle();
//        _elementRepr.setTranslateX(_xPos * REPR_SIZE);
//        _elementRepr.setTranslateY(_yPos * REPR_SIZE);
//        ((Circle) _elementRepr).setRadius(REPR_SIZE / 2);
//        _elementRepr.setFill(MOULD_COLOR);
        _elementRepr = new Rectangle();
        _elementRepr.setTranslateX(_xPos * REPR_SIZE);
        _elementRepr.setTranslateY(_yPos * REPR_SIZE);
        ((Rectangle) _elementRepr).setHeight(REPR_SIZE);
        ((Rectangle) _elementRepr).setWidth(REPR_SIZE);
        _elementRepr.setFill(MOULD_COLOR);
    }

    /**
     * @param randPicker predefined random picker.
     * @return generated x move.
     */
    public int generateXMove(boolean randPicker) {
        int xMove = isLeftOfHead() ? LEFT_MOVE : RIGHT_MOVE;
        return randPicker ? xMove : 0;
    }

    /**
     * @param randPicker predefined random picker.
     * @return generated y move.
     */
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
//        double energyToGive = ENERGY_LOSS_FACTOR * _energy;
        double energyToGive = ENERGY_LOSS_FACTOR;
        _energy -= energyToGive;
        other.setEnergy(other.getEnergy() + energyToGive);
    }

    /**
     * @return true iff mould has found food.
     */
    public boolean didFindFood() {
        return hasFoundFood;
    }

    /**
     * Sets the hasFoundFood field to true.
     */
    public void setFoundFood() {
        hasFoundFood = true;
    }

    /**
     * Gets energy from given mould.
     *
     * @param other mould to get energy from.
     */
    private void getEnergyFrom(Mould other) {
        //        double energyToGive = ENERGY_LOSS_FACTOR * _energy;
        double energyToGet = ENERGY_LOSS_FACTOR;
        _energy += energyToGet;
        other.setEnergy(other.getEnergy() - energyToGet);
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
     * Set energy of mould.
     *
     * @param energy energy to set.
     */
    public void setEnergy(double energy) {
        this._energy = energy;
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

    public void saturate() {
//        _elementRepr.setFill(((Color)_elementRepr.getFill()).brighter());
        Color color = (Color) _elementRepr.getFill();
        System.out.println(color.getBrightness());
        _elementRepr.setFill(color.deriveColor(0, 1, 1.05, 1));
        if (((Color) _elementRepr.getFill()).getBrightness() > 0.3) {
            mouldHead = this;
        }
        //        _elementRepr.setFill(Color.hsb(color.getHue(), Math.sqrt(color.getSaturation()), Math.pow(color.getBrightness(), 2)));
//        _elementRepr.setFill(Color.BLACK);
    }

    public void reenergize() {

    }

}