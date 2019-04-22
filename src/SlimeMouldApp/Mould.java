package SlimeMouldApp;

import javafx.scene.text.Text;

import java.util.Random;

import static SlimeMouldApp.SlimeMouldManager.*;

public class Mould extends Element {

    public static final int RIGHT_MOVE = 1;
    public static final int LEFT_MOVE = -1;
    public static final String NULL_HEAD_ERR = "Err: Null mould head.";
    public static final int DOWN_MOVE = 1;
    public static final int UP_MOVE = -1;
    public static final String ADJ_NEIGHBOR_ERR = "Err: Neighbor must be adjacent.";

    public static final Text MOULD_TXT = new Text(MOULD_IMG);

    static Mould mouldHead;
    static boolean hasFoundFood;

    public Mould(int xPos, int yPos) {
        super(xPos, yPos);
        if (mouldHead == null) {
            mouldHead = this;
            hasFoundFood = false;
        }
        _text = MOULD_TXT;
    }

    public void searchForFood(Tile[][] worldGrid) throws Exception {

        int xMove = isLeftOfHead() ? LEFT_MOVE : RIGHT_MOVE;
        int yMove = isBelowHead() ? DOWN_MOVE : UP_MOVE;
        Random rand = new Random();
        boolean randMove = rand.nextBoolean();
        xMove = randMove ? xMove : 0;
        yMove = randMove ? 0 : yMove;
        Tile randNeighbor = getNeighbor(worldGrid, xMove, yMove);
        switch (randNeighbor.getType()) {
            case Tile.EMPTY:
                spreadTo(randNeighbor);
                break;
            case Tile.FOOD:
                eatFood(randNeighbor);
                break;
            case Tile.MOULD:
                ((Mould) randNeighbor.getElement()).searchForFood(worldGrid);
                break;
        }
    }

    private void spreadTo(Tile neighbor) throws Exception {
        neighbor.becomeMould();
    }

    private void eatFood(Tile neighbor) throws Exception {
        //TODO: Energy should grow!
        hasFoundFood = true;
        neighbor.becomeMould();
    }

    private Tile getNeighbor(Tile[][] worldGrid, int xDelta, int yDelta) throws Exception {

        Tile downNeighbor = (_yPos == Y_TILES - 1) ? null : worldGrid[_xPos][_yPos + 1];
        Tile upNeighbor = (_yPos == 0) ? null : worldGrid[_xPos][_yPos - 1];
        Tile rightNeighbor = (_xPos == X_TILES - 1) ? null : worldGrid[_xPos + 1][_yPos];
        Tile leftNeighbor = (_xPos == 0) ? null : worldGrid[_xPos - 1][_yPos];

        if ((xDelta * yDelta != 0) || (Math.abs(xDelta) + Math.abs(yDelta) == 0)) {
            throw new SlimeMouldException(ADJ_NEIGHBOR_ERR);
        }
        if (xDelta != 0) {
            return (xDelta > 0) ? rightNeighbor : leftNeighbor;
        } else {
            return (yDelta > 0) ? downNeighbor : upNeighbor;
        }
    }

    private boolean isLeftOfHead() throws Exception {
        if (mouldHead == null) {
            throw new SlimeMouldException(NULL_HEAD_ERR);
        }
        return _xPos < mouldHead.get_xPos();
    }

    private boolean isBelowHead() throws Exception {
        if (mouldHead == null) {
            throw new SlimeMouldException(NULL_HEAD_ERR);
        }
        return _yPos > mouldHead.get_yPos();
    }

}
