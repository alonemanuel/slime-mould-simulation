package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SlimeMouldManager {
    public static final int TILE_SIZE = 40;
    private static final int W = TILE_SIZE * 20;
    private static final int H = TILE_SIZE * 15;
    private static final int X_TILES = W / TILE_SIZE;
    private static final int Y_TILES = H / TILE_SIZE;

    private static final int NUM_OF_FOODS = 5;
    public static final String FOOD_IMG = "O";
    public static final String MOULD_IMG = "X";

    private Tile[][] worldGrid;
    private Pane worldPane;

    public SlimeMouldManager() {
        worldGrid = new Tile[X_TILES][Y_TILES];
        worldPane = new Pane();

    }

    public void populateTiles() {
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y);
                worldGrid[x][y] = tile;
                worldPane.getChildren().add(tile);
            }
        }
    }

    // Initialize World //

    public void populateFood() {

        Random rand = new Random();
        for (int i=0;i < NUM_OF_FOODS;i++) {
            int randX = rand.nextInt(X_TILES);
            int randY = rand.nextInt(Y_TILES);
            addFood(randX, randY);
        }
    }

    public void placeMould() {

        int randX, randY;
        Random rand = new Random();
        Tile tile;
        do {
            randX = rand.nextInt(X_TILES);
            randY = rand.nextInt(Y_TILES);
            tile = worldGrid[randX][randY];

        } while (tile.getType() != Tile.EMPTY);

        tile.setType(Tile.MOULD);
        tile.getText().setText(MOULD_IMG);
    }


    public void populateWorld() {
        worldPane.setPrefSize(W, H);
        populateTiles();
        populateFood();
        placeMould();
    }

    // Helpers //
    private void addFood(int xPos, int yPos) {
        Tile tile = worldGrid[xPos][yPos];
        tile.setType(Tile.FOOD);
        tile.getText().setText(FOOD_IMG);
    }


    public Parent getRootStruct() {
        BorderPane borderPane = new BorderPane();
        populateWorld();
        borderPane.setCenter(worldPane);
        return borderPane;
    }

    public void start(Stage primaryStage) {

        Scene scene = new Scene(getRootStruct());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
