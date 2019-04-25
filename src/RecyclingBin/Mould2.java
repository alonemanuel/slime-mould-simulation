//package SlimeMouldApp;
//
//// Imports //
//
//import javafx.scene.text.Text;
//
//import java.util.Random;
//
//import static SlimeMouldApp.SlimeMouldManager.*;
//
///**
// * A class representing a mould element.
// */
//public class Mould2 extends Element {
//
//    // Constants //
//    public static final int RIGHT_MOVE = 1;
//    public static final int LEFT_MOVE = -1;
//    public static final String NULL_HEAD_ERR = "Err: Null mould head.";
//    public static final int DOWN_MOVE = 1;
//    public static final int UP_MOVE = -1;
//    public static final String ADJ_NEIGHBOR_ERR = "Err: Neighbor must be adjacent.";
//    public static final Text MOULD_TXT = new Text(MOULD_IMG);
//    public static final int DEFAULT_ENERGY = 10;
//    public static final double ENERGY_LOSS_FACTOR = 0.2;
//
//    // Statics //
//    /**
//     * The head of the entire mould.
//     */
//    static Mould mouldHead;
//    /**
//     * True iff the mould has encountered food.
//     */
//    static boolean hasFoundFood;
//
//    // Fields //
//    /**
//     * Energy attribute of the mould. Affects it's darkness.
//     */
//    private double _energy;
//
//    // Methods //
//
//    /**
//     * Ctor.
//     *
//     * @param xPos x position of the mould.
//     * @param yPos y position of the mould.
//     */
//    public Mould(int xPos, int yPos) {
//        super(xPos, yPos);
//        // Inits the mould head to be the first created mould.
//        if (mouldHead == null) {
//            mouldHead = this;
//            hasFoundFood = false;
//        }
//        _text = MOULD_TXT;
//        _energy = DEFAULT_ENERGY;
//    }
//
//    /**
//     * Searches for food across the world.
//     *
//     * @param worldGrid the grid of tiles representing the world.
//     */
//    public void searchForFood(Tile[][] worldGrid) throws SlimeMouldException {
//        Random rand = new Random();
//
//        // Generate the X and Y positions for the next move. Mould will try and move away from the head.
//        int xMove = isLeftOfHead() ? LEFT_MOVE : RIGHT_MOVE;
//        int yMove = isBelowHead() ? DOWN_MOVE : UP_MOVE;
//        boolean randMove = rand.nextBoolean();
//        xMove = randMove ? xMove : 0;
//        yMove = randMove ? 0 : yMove;
//
//        // Get the actual neighbor from the grid of tiles.
//        Tile randNeighbor = getNeighbor(worldGrid, xMove, yMove);
//        if (randNeighbor == null) {
//            randNeighbor = getNeighbor(worldGrid, yMove, xMove);
//            if (randNeighbor == null) {
//                System.out.println("Everything's null!");       // TODO: DEBUG only
//            }
//        }
//
//        // Choose how to act according to the chosen neighbor.
//        switch (randNeighbor.getType()) {
//            case Tile.EMPTY:
//                spreadTo(randNeighbor);
//                break;
//            case Tile.FOOD:
//                eatFood(randNeighbor);
//                break;
//            case Tile.MOULD:
//                pulsateTo(randNeighbor, worldGrid);
//                break;
//        }
//
//        // Repeat the search if food hasn't been found yet.
//        if (!hasFoundFood) {
//            mouldHead.searchForFood(worldGrid);
//        }
//    }
//
//    /**
//     * Pulsate (more than just a move) to the neighboring tile.
//     *
//     * @param tile      tile to pulsate to.
//     * @param worldGrid grid of tiles.
//     */
//    private void pulsateTo(Tile tile, Tile[][] worldGrid) {
//        Mould toPulsate = (Mould) tile.getElement();
//        giveEnergyTo(toPulsate);
//        toPulsate.searchForFood(worldGrid);
//        getEnergyFrom(toPulsate);
//    }
//
//    /**
//     * Gives energy to given mould.
//     *
//     * @param other mould to give energy to.
//     */
//    private void giveEnergyTo(Mould other) {
////        double energyToGive = ENERGY_LOSS_FACTOR * _energy;
//        double energyToGive = ENERGY_LOSS_FACTOR;
//        _energy -= energyToGive;
//        other.setEnergy(other.getEnergy() + energyToGive);
//    }
//
//    /**
//     * Gets energy from given mould.
//     *
//     * @param other mould to get energy from.
//     */
//    private void getEnergyFrom(Mould other) {
//        //        double energyToGive = ENERGY_LOSS_FACTOR * _energy;
//        double energyToGet = ENERGY_LOSS_FACTOR;
//        _energy += energyToGet;
//        other.setEnergy(other.getEnergy() - energyToGet);
//    }
//
//    /**
//     * Set energy of mould.
//     *
//     * @param energy energy to set.
//     */
//    public void setEnergy(double energy) {
//        this._energy = energy;
//    }
//
//    /**
//     * Get energy of mould.
//     *
//     * @return energy of mould.
//     */
//    public double getEnergy() {
//        return _energy;
//    }
//
//    /**
//     * Spread to neighboring non-mould tile.
//     *
//     * @param neighbor non-mould tile.
//     */
//    private void spreadTo(Tile neighbor) throws SlimeMouldException {
//        neighbor.becomeMould();
//    }
//
//    /**
//     * Eats food.
//     *
//     * @param neighbor food-type tile.
//     */
//    private void eatFood(Tile neighbor) throws SlimeMouldException {
//        //TODO: Energy should grow!
//        hasFoundFood = true;
//        neighbor.becomeMould();
//    }
//
//    /**
//     * Get neighbor according to given pos.
//     *
//     * @param worldGrid grid of tiles.
//     * @param xDelta    x delta of neighbor.
//     * @param yDelta    y delta of neighbor.
//     * @return neighbor (tile) on grid.
//     */
//    private Tile getNeighbor(Tile[][] worldGrid, int xDelta, int yDelta) throws SlimeMouldException {
////        // Defines the nearby neighbors
////        Tile downNeighbor = (_yPos == Y_TILES - 1) ? null : worldGrid[_xPos][_yPos + 1];
////        Tile upNeighbor = (_yPos == 0) ? null : worldGrid[_xPos][_yPos - 1];
////        Tile rightNeighbor = (_xPos == X_TILES - 1) ? null : worldGrid[_xPos + 1][_yPos];
////        Tile leftNeighbor = (_xPos == 0) ? null : worldGrid[_xPos - 1][_yPos];
//
//        if ((xDelta * yDelta != 0) || (Math.abs(xDelta) + Math.abs(yDelta) == 0)) {
//            throw new SlimeMouldException(ADJ_NEIGHBOR_ERR);
//        }
//        int newX = ((_xPos + xDelta >= 0) && (_xPos + xDelta <= X_TILES - 1)) ? _xPos + xDelta : _xPos;
//        int newY = ((_yPos + yDelta >= 0) && (_yPos + yDelta <= Y_TILES - 1)) ? _yPos + yDelta : _yPos;
//        return ((newX < 0) || (newY < 0)) ? null : worldGrid[newX][newY];
////
////        if (xDelta != 0) {
////            return (xDelta > 0) ? rightNeighbor : leftNeighbor;
////        } else {
////            return (yDelta > 0) ? downNeighbor : upNeighbor;
////        }
//    }
//
//    /**
//     * @return true if mould is left of headMould, random if mould is headMould.
//     */
//    private boolean isLeftOfHead() throws SlimeMouldException {
//        Random rand = new Random();
//        if (mouldHead == null) {
//            throw new SlimeMouldException(NULL_HEAD_ERR);
//        }
//        return (_xPos == mouldHead.getXPos()) ? rand.nextBoolean() : _xPos < mouldHead.getXPos();
//    }
//
//    /**
//     * @return true if mould is below headMould, random if mould is headMould.
//     */
//    private boolean isBelowHead() throws SlimeMouldException {
//        Random rand = new Random();
//        if (mouldHead == null) {
//            throw new SlimeMouldException(NULL_HEAD_ERR);
//        }
//        return (_yPos == mouldHead.getYPos()) ? rand.nextBoolean() : _yPos > mouldHead.getYPos();
//    }
//
//}
