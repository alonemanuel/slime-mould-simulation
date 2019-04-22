package SlimeMouldApp;

// Imports //

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;


/**
 * A class representing a Slime Mould Manager.
 */
public class SlimeMouldManager {
    // Constants //
    /**
     * Size of tile (height == width == size)
     */
    public static final int TILE_SIZE = 40;
    /**
     * Width of screen.
     */
    private static final int W = TILE_SIZE * 20;
    /**
     * Height of screen.
     */
    private static final int H = TILE_SIZE * 15;
    /**
     * Number of X aligned tiles.
     */
    private static final int X_TILES = W / TILE_SIZE;
    /**
     * Number of Y aligned tiles.
     */
    private static final int Y_TILES = H / TILE_SIZE;

    /**
     * Number of food items.
     */
    private static final int NUM_OF_FOODS = 5;
    /**
     * "Image" (=string) of food.
     */
    public static final String FOOD_IMG = "O";
    /**
     * "Image" (=string) of mould.
     */
    public static final String MOULD_IMG = "X";

    // Fields //
    /**
     * Grid (2D Tile array) representing world.
     */
    private Tile[][] worldGrid;
    /**
     * Pane of Tiles holding all tiles.
     */
    private Pane worldPane;

    // Methods //

    /**
     * Default ctor.
     */
    public SlimeMouldManager() {
        worldGrid = new Tile[X_TILES][Y_TILES];
        worldPane = new Pane();
    }

    /**
     * Populate world with tiles.
     */
    public void populateTiles() {
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y);
                worldGrid[x][y] = tile;
                worldPane.getChildren().add(tile);
            }
        }
    }

    /**
     * Populate world with food.
     */
    public void populateFood() throws Exception {
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
    public void placeMould() throws Exception {

        int randX, randY;
        Random rand = new Random();
        Tile tile;
        // Wait until en empty spot is found.
        do {
            randX = rand.nextInt(X_TILES);
            randY = rand.nextInt(Y_TILES);
            tile = worldGrid[randX][randY];

        } while (tile.getType() != Tile.EMPTY);

        tile.setType(Tile.MOULD);
        tile.getText().setText(MOULD_IMG);
    }


    /**
     * Populate world with all needed elements.
     */
    public void populateWorld() throws Exception {
        worldPane.setPrefSize(W, H);
        populateTiles();
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
    private void addFood(int xPos, int yPos) throws Exception {
        Tile tile = worldGrid[xPos][yPos];
        tile.setType(Tile.FOOD);
        tile.getText().setText(FOOD_IMG);
    }


    /**
     * @return root structure of app.
     */
    public Parent getRootStruct() throws Exception {
        BorderPane borderPane = new BorderPane();
        populateWorld();
        // Sets world in center of border pane.
        borderPane.setCenter(worldPane);
        return borderPane;
    }

    /**
     * Starts the show.
     *
     * @param primaryStage
     */
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(getRootStruct());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}