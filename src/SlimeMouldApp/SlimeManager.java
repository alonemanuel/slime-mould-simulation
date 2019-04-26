package SlimeMouldApp;

// Imports //

import javafx.animation.AnimationTimer;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static SlimeMouldApp.Element.FOOD_TYPE;
import static SlimeMouldApp.Element.MOULD_TYPE;


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
    /**
     * Reenergization factor (in what rate should moulds reenergize?)
     */
    private static final int REENERG_FACTOR = 100;

    // Fields //
    /**
     * Pool of Nodes that form the world.
     */
    public NodeMap nodePool;
    /**
     * Grid (2D Tile array) representing world.
     */
    private Element[][] worldGrid;
    /**
     * Pane of Tiles holding all tiles.
     */
    private Pane worldPane;


    /**
     * Current spreading node.
     */
    private LinkedList<Node> nextSpreadNodes;


    /**
     * Set of foods already found.
     */
    private HashSet<Node> foodsFound;
    /**
     * Set of mould heads.
     */
    private HashMap<Node, AStar> mouldHeads;
    /**
     * Current reenergization rate value.
     */
    private int reenergRate = 0;

    // Methods //

    /**
     * Default ctor.
     */
    public SlimeManager() {
        worldGrid = new Element[X_TILES][Y_TILES];
        nodePool = new NodeMap(X_TILES, Y_TILES);
        worldPane = new Pane();
        foodsFound = new HashSet<>();
        mouldHeads = new HashMap<>();
        System.out.println("Created manager");
    }


    /**
     * Update function that sets the animation timer.
     */
    public void update() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // For each node head, eat and spread.
                for (Node head : mouldHeads.keySet()) {
                    spread(head);
                }
                reenergizeMoulds();
            }
        };
        timer.start();
    }


    /**
     * Reenergize moulds in a given rate (If it happens each frame it might be costly.
     */
    public void reenergizeMoulds() {
        // If we haven't reached the reenerg factor yet, update reenerg rate.
        if (reenergRate < REENERG_FACTOR) {
            reenergRate++;
            return;
        }
        // Else
        reenergRate = 1;
        for (int x = 0; x < X_TILES; x++) {
            for (int y = 0; y < Y_TILES; y++) {
                // Go over all *moulds* and reenergize.
                if (worldGrid[x][y].getType() != MOULD_TYPE) {
                    continue;
                }
                ((Mould) worldGrid[x][y]).reenergize();
            }
        }
    }

    /**
     * Eat food from found foods.
     *
     * @param head head of mould to eat from.
     */
    public void spread(Node head) {
//        // Do not try and eat if no food has already been found.
//        if (!Mould.hasFoundFood) {
//            return;
//        }
        // If not in middle of eating, initialize eating.
        if ((mouldHeads.get(head) == null) || !mouldHeads.get(head).isInMiddleOfSpread) {
            // Init eating start to be the currently received head.
            Node spreadStart = head;
            // Fetch the eating goal.
            Node spreadGoal = getNodeAwayFrom(spreadStart);
            // Init an A* for the eating task.
            AStar currAStar = new AStar(nodePool, spreadStart, spreadGoal);
            mouldHeads.put(head, currAStar);
            currAStar.isInMiddleOfSpread = true;
            // Start searching.
            nextSpreadNodes = currAStar.search();

            // If in a middle of eating, continue to do so.
        } else {
            AStar currAStar = mouldHeads.get(head);
            // Get next node from eatStar.
            nextSpreadNodes = currAStar.search();
            // If we reached our eatGoal, stop eating.
            if ((nextSpreadNodes.size() == 1) && (nextSpreadNodes.peek() == currAStar.spreadGoal)) {
                currAStar.isInMiddleOfSpread = false;
                Mould.hasFoundFood = true; // TODO: this is the general spreading case
            }

        }

        // Spread! Like the inner mould in you begs you to do.
        for (Node node : nextSpreadNodes) {
            spreadTo(node);
        }


    }


    /**
     * Gets the food closest to the subject.
     */
    private Node getFoodClosestTo(Node subject) {
        // Init min dist to be infinity.
        double minDistance = Double.POSITIVE_INFINITY;
        double currDistance;
        AtomicReference<Node> minFood = null;

        // Run through all food found and get minimal.
        for (Node currFood : foodsFound) {
            // If minFood hasn't been set yet, make it the first food traversed.
            if (minFood.get() == null) {
                minFood.set(currFood);
                minDistance = currFood.getManhattanTo(minFood.get());
                continue;
            }
            currDistance = subject.getManhattanTo(currFood);
            if (currDistance <= minDistance) {
                minFood.set(currFood);
                minDistance = currDistance;
            }
        }
        return minFood.get();
    }

    /**
     * Get (random) node to spread to that is away from awayFrom.
     */
    private Node getNodeAwayFrom(Node awayFrom) {
        // TODO: Make better (and actually something)
        Node nodeAway;
        int x, y, xSign, ySign;
        Random rand = new Random();
        do {
            x = rand.nextInt(4) + 1;
            y = rand.nextInt(4) + 1;
            xSign = rand.nextBoolean() ? 1 : -1;
            ySign = rand.nextBoolean() ? 1 : -1;
            nodeAway = nodePool.getNode(awayFrom.xPos + (xSign * x), awayFrom.yPos + (ySign * y));
        } while ((nodeAway == null) || (worldGrid[x][y].getType() == MOULD_TYPE));
        return nodeAway;
    }


    /**
     * Spreads to the given node (spreading = moving to this specific node, not by a path)
     */
    private void spreadTo(Node nodeToSpread) {
        int xPos = nodeToSpread.xPos;
        int yPos = nodeToSpread.yPos;
        Element currElem = worldGrid[xPos][yPos];
        if (currElem.getType() == MOULD_TYPE) {
            ((Mould) currElem).upEnergy();
        }
        // Eat food! TODO: handle energy transfer
        if (currElem.getType() == FOOD_TYPE) {
            foodsFound.add(nodeToSpread);
        }
        Mould toSpread = new Mould(xPos, yPos);
        worldGrid[xPos][yPos] = toSpread;
        worldGrid[xPos][yPos].setType();
        replaceElement(currElem, toSpread);
    }

    /**
     * Puts toAdd instead of toRemove in world pane.
     */
    private void replaceElement(Element toRemove, Element toAdd) {
        worldPane.getChildren().add(toAdd.getElementRepr());
        worldPane.getChildren().remove(toRemove.getElementRepr());
    }

    /**
     * Adds all elements to the worldPane.
     */
    public void drawElements() {
        System.out.println("Drawing elements");
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
        System.out.println("Populating elements");
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
        System.out.println("Populating food");
        Random rand = new Random();
        for (int i = 0; i < NUM_OF_FOODS; i++) {
            int randX = rand.nextInt(X_TILES);
            int randY = rand.nextInt(Y_TILES);
            Food foodToPlace = new Food(randX, randY);
            worldGrid[randX][randY] = foodToPlace;
            worldGrid[randX][randY].setType();
        }
    }

    /**
     * Place mould in world.
     */
    public void placeMould() {
        System.out.println("Placing mould");
        Element currElem;
        int randX, randY;
        Random rand = new Random();
        // Wait until en empty spot is found.
        do {
            randX = rand.nextInt(X_TILES);
            randY = rand.nextInt(Y_TILES);
            currElem = worldGrid[randX][randY];

        } while (currElem.getType() != Element.EMPTY_TYPE);
        Mould mouldToPlace = new Mould(randX, randY);
        worldGrid[randX][randY] = mouldToPlace;
        worldGrid[randX][randY].setType();
        mouldHeads.put(nodePool.getNode(randX, randY), null);
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
        System.out.println("Starting");

        // Initializes UI elements
        BorderPane borderPane = UI.initialize(startWindow, this);
        populateWorld();
        drawElements();
        borderPane.setCenter(worldPane);
        // Shows the show
        startWindow.show();
    }
}
