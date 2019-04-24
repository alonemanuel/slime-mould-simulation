package SlimeMouldApp;

// Imports //

import javafx.animation.AnimationTimer;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Random;

import static SlimeMouldApp.Element.*;


/**
 * A class representing a Slime Mould2 Manager.
 */
public class SlimeManager {


    // Constants //
    /**
     * Size of tile (height == width == size)
     */
    public static final int REPR_SIZE = 15;
    /**
     * "Image" (=string) of food.
     */
    public static final String FOOD_IMG = "O";
    /**
     * "Image" (=string) of mould.
     */
    public static final String MOULD_IMG = "X";
    /**
     * Width of screen.
     */
    private static final int W = (800 / REPR_SIZE) * REPR_SIZE;
    /**
     * Number of X aligned tiles.
     */
    protected static final int X_TILES = W / REPR_SIZE;
    /**
     * Height of screen.
     */
    private static final int H = (600 / REPR_SIZE) * REPR_SIZE;
    /**
     * Number of Y aligned tiles.
     */
    protected static final int Y_TILES = H / REPR_SIZE;
    /**
     * Number of food items.
     */
    private static final int NUM_OF_FOODS = 8;

    // Fields //
    public NodeMap nodePool;
    /**
     * Grid (2D Tile array) representing world.
     */
    private Element[][] worldGrid;
    /**
     * Pane of Tiles holding all tiles.
     */
    private Pane worldPane;
    private Mould mouldHead;
    private Node foodFound;


    // Methods //

    /**
     * Default ctor.
     */
    public SlimeManager() {
        worldGrid = new Element[X_TILES][Y_TILES];
        nodePool = new NodeMap(X_TILES, Y_TILES);
        worldPane = new Pane();
        System.out.println("Created manager");
    }


    public void update() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveSlime();
            }
        };
        timer.start();
    }

    private void moveSlime() {
        if (mouldHead.didFindFood()) {
//            System.out.println("Found food!");
            AStar astar = new AStar(nodePool, nodePool.getNode(mouldHead._xPos, mouldHead._yPos), foodFound);
            LinkedList<Node> path = astar.search();
            Mould currMould = mouldHead;
            for (Node currNode : path) {

                Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
                // Choose how to act according to the chosen neighbor.
                switch (currNeighbor.getType()) {
                    case EMPTY_TYPE:
                        spreadTo(currMould, currNeighbor);
//                        didSpread = true;
                        break;
                    case FOOD_TYPE:
                        eatFood(currMould, (Food) currNeighbor);
                        foodFound = nodePool.getNode(currNeighbor._xPos, currNeighbor._yPos);
//                        didSpread = true;
                        break;
                    case MOULD_TYPE:
                        currMould = (Mould) currNeighbor;
                        currMould.saturate();
                        break;
                }
            }
        } else {
            findFood();
        }
    }


    private void findFood() {
        Random rand = new Random();
        boolean didSpread = false;
        int xMove, newX, xPos;
        int yMove, newY, yPos;
        Mould currMould = Mould.getMouldHead();
        Element currNeighbor;
        do {
            // Generate the X and Y positions for the next move. Mould will try and move away from the head.
            boolean randPicker = rand.nextBoolean();
            xMove = currMould.generateXMove(randPicker);
            yMove = currMould.generateYMove(randPicker);

            // Get the actual neighbor from the grid of tiles.
            xPos = currMould.getXPos();
            yPos = currMould.getYPos();
            if ((xMove * yMove != 0) || (Math.abs(xMove) + Math.abs(yMove) == 0)) {
                System.err.println("No such thing!");
            }
            // TODO: The code below can enter an endless loop when no available tile exists?
            newX = ((xPos + xMove >= 0) && (xPos + xMove <= X_TILES - 1)) ? xPos + xMove : -1;
            newY = ((yPos + yMove >= 0) && (yPos + yMove <= Y_TILES - 1)) ? yPos + yMove : -1;
            currNeighbor = ((newX < 0) || (newY < 0)) ? null : worldGrid[newX][newY];

            if (currNeighbor == null) {
//                System.out.println("Everything's null!");       // TODO: DEBUG only
                didSpread = true;
                continue;
            }

            // Choose how to act according to the chosen neighbor.
            switch (currNeighbor.getType()) {
                case EMPTY_TYPE:
                    spreadTo(currMould, currNeighbor);
                    didSpread = true;
                    break;
                case FOOD_TYPE:
                    eatFood(currMould, (Food) currNeighbor);
                    foodFound = nodePool.getNode(currNeighbor._xPos, currNeighbor._yPos);
                    didSpread = true;
                    break;
                case MOULD_TYPE:
                    currMould = (Mould) currNeighbor;
                    currMould.saturate();
                    break;
            }


        } while (!didSpread);
    }


    public void eatFood(Mould currMould, Food currFood) {
        // TODO: Manage energy consumption
        spreadTo(currMould, currFood);
        mouldHead.setFoundFood();
    }

    public void spreadTo(Mould currMould, Element currNeighbor) {
        int xPos = currNeighbor.getXPos();
        int yPos = currNeighbor.getYPos();
        Mould toSpread = new Mould(xPos, yPos);
        worldGrid[xPos][yPos] = toSpread;
        worldPane.getChildren().add(toSpread.getElementRepr());
    }


    public void drawElements() {
        for (int x = 0; x < X_TILES; x++) {
            for (int y = 0; y < Y_TILES; y++) {
                worldPane.getChildren().add(worldGrid[x][y].getElementRepr());
            }
        }
    }

    /**
     * Populate world with tiles.
     */
    public void populateElements() {
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Element element = new Empty(x, y);
                worldGrid[x][y] = element;
            }
        }
    }

    /**
     * Populate world with food.
     */
    public void populateFood() {
        Random rand = new Random();
        for (int i = 0; i < NUM_OF_FOODS; i++) {
            int randX = rand.nextInt(X_TILES);
            int randY = rand.nextInt(Y_TILES);
            addFood(randX, randY);
        }
    }

    /**
     * Place mould in world.
     */
    public void placeMould() {
        Element currElem;
        int randX, randY;
        Random rand = new Random();
        // Wait until en empty spot is found.
        do {
            randX = rand.nextInt(X_TILES);
            randY = rand.nextInt(Y_TILES);
            currElem = worldGrid[randX][randY];

        } while (currElem.getType() != Element.EMPTY_TYPE);
        worldGrid[randX][randY] = new Mould(randX, randY);
        mouldHead = Mould.getMouldHead();
    }


    /**
     * Populate world with all needed elements.
     */
    public void populateWorld() {
        System.out.println("Populating world");
        worldPane.setPrefSize(W, H);
        populateElements();
        populateFood();
        placeMould();
    }


    // Helpers //


    /**
     * Adds food to world.
     *
     * @param xPos x pos of food.
     * @param yPos y pos of food.
     */
    private void addFood(int xPos, int yPos) {
        worldGrid[xPos][yPos] = new Food(xPos, yPos);
    }

    public void restart(Stage currWindow) {
        currWindow.close();
        worldGrid = new Element[X_TILES][Y_TILES];
        worldPane = new Pane();
        Mould.restart();
        System.out.println("Created manager");
        start(new Stage());
    }

    /**
     * Starts the show.
     *
     * @param startWindow
     */
    public void start(Stage startWindow) {

        // Initializes UI elements

        BorderPane borderPane = UI.initialize(startWindow, this);
        populateWorld();
        drawElements();
        borderPane.setCenter(worldPane);
        System.out.println("Set center");
//        update();
        System.out.println("Updated");
        // Shows the show
        startWindow.show();
        System.out.println("Showed");
    }
}
