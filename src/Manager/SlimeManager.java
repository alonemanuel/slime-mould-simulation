package Manager;

// Imports //

import Logic.*;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import static Logic.Element.*;

/**
 * A class representing a Slime Mould2 Manager.
 */
public class SlimeManager {

	/**
	 * Size of tile (height == width == size)
	 */
	public static final int REPR_SIZE = 8;

	// Constants //
	/**
	 * Rate of expansion - how many tiles get covered in a single frame.
	 */
	public static final int EXPANSION_RATE = 1;
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
	private static final int NUM_OF_FOODS = 12;
	/**
	 * Below this thresh, moulds disappear.
	 */
	private static final double DISAPPEAR_THRESH = 0.11;
	private static final int FRAME_THRESH = 10;
	public static final int SPACING_FACTOR = 7;
	public static HashSet<Element> veins;
	/**
	 * Pool of nodes.
	 */
	public NodeMap nodePool;
	// Fields //
	HashMap<Mould, LinkedList<Node>> headsToAdd;
	private int frame = 0;
	/**
	 * Grid (2D Tile array) representing world.
	 */
	private Element[][] worldGrid;
	/**
	 * Pane of Tiles holding all tiles.
	 */
	private Pane worldPane;
	/**
	 * The currently found foods.
	 */
	private HashMap<Element, LinkedList<Node>> foodsFound;
	private HashSet<Node> allFoods;
	/**
	 * Mould heads.
	 */
	private HashMap<Mould, LinkedList<Node>> mouldHeads;
	private LinkedList<Mould> edges;
	private Mould mouldHead;
	private LinkedList<Mould> edgesToRemove;
	private LinkedList<Mould> edgesToAdd;
	/**
	 * Animation timer of the program.
	 */
	AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long l) {
			moveSlime();
		}
	};
	private boolean didGetFood = false;
	private boolean didFindAllFood = false;

	// Methods //

	/**
	 * Default ctor.
	 */
	public SlimeManager(Pane pane) {
		veins = new HashSet<>();
		edges = new LinkedList<>();
		edgesToRemove = new LinkedList<>();
		edgesToAdd = new LinkedList<>();
		allFoods = new HashSet<>();

		worldGrid = new Element[X_TILES][Y_TILES];
		nodePool = new NodeMap(X_TILES, Y_TILES);
		worldPane = pane;
		foodsFound = new HashMap<>();
		mouldHeads = new HashMap<>();
		headsToAdd = new HashMap<>();
		log("Created manager");
	}

	/**
	 * Logs the message to the console.
	 *
	 * @param logMsg msg to be logged.
	 */
	public static void log(String logMsg) {
		System.out.println("Log: " + logMsg);
	}

	/**
	 * Updates the timer and thus the program.
	 */
	public void update() {
		log("Updated");
		timer.start();
	}

	private void moveSlime() {
		// Before each movement, reenergize moulds.
		// Move according to the expansion rate.
		getFood();
		if (frame == 0) {
			frame = 0;
			reenergizeMoulds();
			if (edges.isEmpty()) {
				edges.addFirst(mouldHead);
			}
			searchForFood();
		} else {
			frame++;
		}
	}

	private void getFood() {
		HashSet<Element> toRemove = new HashSet<>();
		for (Element food : foodsFound.keySet()) {
			if (foodsFound.get(food).isEmpty()) {

				toRemove.add(food);
			} else {
				// Get next node from A*s path.
				Node currNode = foodsFound.get(food).pop();
				Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
				veins.add(currNeighbor);
				spreadTo(currNeighbor);  // TODO: Should be spawnTO?
			}
		}
		foodsFound.keySet().removeAll(toRemove);
	}

	private void searchForFood() {
		edgesToRemove.clear();
		edgesToAdd.clear();
		boolean foundFood = false;
		for (Mould edge : edges) {
			for (int i = 0; i < 3; i++) {

				foundFood = expandEdge(edge);
				if (foundFood) {
					break;
				}
			}
			if (foundFood) {
				break;
			}
		}
		edges.removeAll(edgesToRemove);
		edges.addAll(edgesToAdd);
	}

	private boolean expandEdge(Mould edge) {
		Random rand = new Random();
		int xMove, newX, xPos;
		int yMove, newY, yPos;
		Mould currMould = edge;
		Element currNeighbor;
		// Generate the X and Y positions for the next move. Logic.Mould will try and move away from the head.
		boolean randPicker = rand.nextBoolean();
		xMove = currMould.generateXMove(randPicker, edge);
		yMove = currMould.generateYMove(randPicker, edge);

		// Get the actual neighbor from the grid of tiles.
		xPos = currMould.getXPos();
		yPos = currMould.getYPos();
		// TODO: The code below can enter an endless loop when no available tile exists?
		newX = ((xPos + xMove >= 0) && (xPos + xMove <= X_TILES - 1)) ? xPos + xMove : -1;
		newY = ((yPos + yMove >= 0) && (yPos + yMove <= Y_TILES - 1)) ? yPos + yMove : -1;
		currNeighbor = ((newX < 0) || (newY < 0)) ? null : worldGrid[newX][newY];
		edgesToRemove.add(edge);
		if (currNeighbor == null) {
			return false;
		}
		switch (currNeighbor.getType()) {
			case MOULD_TYPE:
				if (allFoods.contains(nodePool.getNode(currNeighbor))) {
					fauxEatFood(currNeighbor);
				} else if ((Math.abs(edges.size() - edgesToRemove.size()) == 0)) {
// TODO: What about the slime not able to pass through walls?
					edgesToAdd.add((Mould) currNeighbor);
				}
				return false;
			case EMPTY_TYPE:
				Mould spawned = spawnTo(currNeighbor);
				edgesToAdd.add(spawned);
				return false;
			case FOOD_TYPE:
				edgeEatFood((Food) currNeighbor);
				return true;
		}
		return false;
	}

	private Mould spawnTo(Element toSpawnTo) {
		int xPos = toSpawnTo.getXPos();
		int yPos = toSpawnTo.getYPos();
		Mould toSpread = new Mould(xPos, yPos);
		replace(toSpread);    // TODO: replace instead of place?
		return toSpread;
	}

	private void edgeEatFood(Food food) {
		log("Eating food");

		allFoods.add(nodePool.getNode(food));
		AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(mouldHead),
				nodePool.getNode(food), allFoods.size() == NUM_OF_FOODS);
		foodsFound.put(food, astar.search());
		// Set food as the new head
		mouldHead = spawnTo(food);
		edgesToRemove.addAll(edges);
		edgesToAdd.clear();
		edgesToAdd.add(mouldHead);
	}

	private void fauxEatFood(Element fauxFood) {
		log("Faux eating food");

		AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(mouldHead),
				nodePool.getNode(fauxFood), allFoods.size() == NUM_OF_FOODS);
		foodsFound.put(fauxFood, astar.search());
		// Set food as the new head
		mouldHead = spawnTo(fauxFood);
		edgesToRemove.addAll(edges);
		edgesToAdd.clear();
		edgesToAdd.add(mouldHead);
	}

	private void spreadTo(Element toSpread) {
		// Choose how to act according to the chosen neighbor.
		switch (toSpread.getType()) {
			case EMPTY_TYPE:
				spreadToEmpty(toSpread);
				break;
			case FOOD_TYPE:
				eatFood((Food) toSpread);
				//TODO: Handle concurrency
				foodsFound.put((Food) toSpread, null);
				break;
			case MOULD_TYPE:
				spreadToMould((Mould) toSpread);
				break;
		}
	}

	/**
	 * Expands
	 */
	private void spreadToMould(Mould mould) {
		mould.timesPast++;
		mould.saturate();
		if (veins.contains(mould)) {
			mould.makeBold();
		}
	}

	/**
	 * Eats given food found.
	 */
	public void eatFood(Food currFood) {
		Mould.setFoundFood(true);
//		spreadToEmpty(currFood);
//		currFood.desaturate();
//		if (((Color) currFood.getElementRepr().getFill()).getOpacity() < DISAPPEAR_THRESH) {
//			Mould newMould = new Mould(currFood._xPos, currFood._yPos);
//			headsToAdd.put(newMould, null);
//			replace(newMould);
//			Mould.setFoundFood(false);
//		}
	}

	public void spreadToEmpty(Element currNeighbor) {
		int xPos = currNeighbor.getXPos();
		int yPos = currNeighbor.getYPos();
		Mould toSpread = new Mould(xPos, yPos);
		replace(toSpread);    // TODO: replace instead of place?
	}

	/**
	 * Goes over all moulds and normalizes them (energy-wise).
	 */
	private void reenergizeMoulds() {
		for (int x = 0; x < X_TILES; x++) {
			for (int y = 0; y < Y_TILES; y++) {
				Element currElem = worldGrid[x][y];
				if (currElem.getType() == MOULD_TYPE && !veins.contains(currElem)) {
					currElem.desaturate();
					// If current elements reaches a minimal threshold opacity, it should disappear.
					if (((Color) currElem.getElementRepr().getFill()).getOpacity() < DISAPPEAR_THRESH) {
						replace(new Empty(x, y));
					}
				}
			}
		}
	}

	private void replace(Element toPlace) {
		Element toRemove = worldGrid[toPlace._xPos][toPlace._yPos];
		worldPane.getChildren().remove(toRemove.getElementRepr());
		place(toPlace);
	}

	private void place(Element toPlace) {
		worldGrid[toPlace._xPos][toPlace._yPos] = toPlace;
		if (toPlace.getType() == FOOD_TYPE) {

			worldPane.getChildren().add(toPlace.getElementRepr());
		} else {
			worldPane.getChildren().add(0, toPlace.getElementRepr());
		}
	}

	/**
	 * Populate world with tiles.
	 */
	public void populateElements() {
		for (int y = 0; y < Y_TILES; y++) {
			for (int x = 0; x < X_TILES; x++) {
				place(new Empty(x, y));
			}
		}
	}

	/**
	 * Populate world with food.
	 */
	public void populateFood() {
		Random rand = new Random();
		HashSet<Food> foodsPlaced = new HashSet<>();
		boolean canPlace = true;
		while (foodsPlaced.size() < NUM_OF_FOODS) {
			canPlace = true;
			//		for (int i = 0; i < NUM_OF_FOODS; i++) {
			int randX = rand.nextInt(X_TILES);
			int randY = rand.nextInt(Y_TILES);
			Food toPlace = new Food(randX, randY);
			for (Food placed : foodsPlaced) {
				if (nodePool.getNode(placed).getManhattanTo(nodePool.getNode(toPlace)) < SPACING_FACTOR) {
					canPlace = false;
					break;
				}
			}
			if (canPlace) {
				foodsPlaced.add(toPlace);
				replace(toPlace);
				putOatmeal(toPlace);
			}
		}
	}

	private void putOatmeal(Food food) {
		Food currFood = new Food(food._xPos, food._yPos);
		Food leftFood = new Food(food._xPos - 1, food._yPos);
		Food rightFood = new Food(food._xPos + 1, food._yPos);
		Food upFood = new Food(food._xPos, food._yPos - 1);
		Food downFood = new Food(food._xPos, food._yPos + 1);
		Rectangle currRect = currFood.getElementRepr();
		Rectangle leftRect = leftFood.getElementRepr();
		Rectangle rightRect = rightFood.getElementRepr();
		Rectangle upRect = upFood.getElementRepr();
		Rectangle downRect = downFood.getElementRepr();
		currRect.setFill(Food.FOOD_COLOR.deriveColor(1, 1, 0.7, 1));
		leftRect.setFill(Food.FOOD_COLOR.deriveColor(1, 1, 0.7, 1));
		rightRect.setFill(Food.FOOD_COLOR.deriveColor(1, 1, 0.7, 1));
		upRect.setFill(Food.FOOD_COLOR.deriveColor(1, 1, 0.7, 1));
		downRect.setFill(Food.FOOD_COLOR.deriveColor(1, 1, 0.7, 1));
		worldPane.getChildren().addAll(currRect, leftRect, rightRect, upRect, downRect);
		//		Image oatmealImg = new Image("file:..\\Photos\\oat_opaq.png");
//		ImageView oatmealIV = new ImageView();
//		oatmealIV.setImage(oatmealImg);
//		oatmealIV.setPreserveRatio(true);
//		oatmealIV.setX(food.getElementRepr().getLayoutX());
//		oatmealIV.setY(food.getElementRepr().getLayoutY());
//		worldPane.getChildren().clear();
//		worldPane.getChildren().add(oatmealIV);
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
		} while (currElem.getType() != EMPTY_TYPE);
		Mould head = new Mould(randX, randY);
		mouldHeads.put(head, null);
		mouldHead = head;
		replace(head);
	}

	public void restart() {
		timer.stop();
	}
////////////////////////////////////////
//
//	/**
//	 * Moves slime.
//	 */
//	private void moveSlime2() {
//		// Before each movement, reenergize moulds.
//		reenergizeMoulds();
//		// Move according to the expansion rate.
//		for (int i = 0; i < EXPANSION_RATE; i++) {
//			getFood();
//			searchForFood();
//		}
//	}
//
//	private void getFood2() {
//		for (Food food : foodsFound.keySet()) {
//			getSpecificFood(food);
//		}
//	}
//
//	private void getSpecificFood(Food food) {
//		LinkedList<Node> currAStarPath = foodsFound.get(food);
//		if (currAStarPath == null || currAStarPath.size() == 0) {
//			AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(mouldHead),
//					nodePool.getNode(food), false);
//			currAStarPath = astar.search();
//			foodsFound.put(food, currAStarPath);
//		}
//		// Get next node from A*s path.
//		Node currNode = currAStarPath.pop();
//		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
//		spreadTo(currNeighbor);
//	}
//
//	private void searchForFood2() {
//		for (Mould head : mouldHeads.keySet()) {
//			specificExpand3(head);
//		}
//		mouldHeads.putAll(headsToAdd);
//		headsToAdd.clear();
//	}
//
//	private void specificExpand2(Mould head) {
//		frame++;
//		LinkedList<Node> closedSet = new LinkedList<>();
//		HashSet<Node> discovered = new HashSet<>();
//		closedSet.addFirst(nodePool.getNode(head));
//		Random rand = new Random();
//		while (!closedSet.isEmpty()) {
//			Node currNode = closedSet.poll();
//			if (currNode.getManhattanTo(nodePool.getNode(head)) > frame) {
//				return;
//			}
//			for (Node neighbor : currNode.neighbors) {
//				if (!discovered.contains(neighbor)) {
//					discovered.add(neighbor);
//					closedSet.addFirst(neighbor);
//					if (rand.nextBoolean()) {
//					}
//					spreadTo(worldGrid[neighbor.xPos][neighbor.yPos]);
//				}
//			}
//		}
//	}
//
//	private void specificExpand3(Mould head) {
//		LinkedList<Node> currBFSPath = mouldHeads.get(head);
//		if (currBFSPath == null || currBFSPath.size() == 0) {
//			Node toExpandTo = getExpansionNode(head);
//			BFS bfs = new BFS(worldGrid, nodePool, nodePool.getNode(head), toExpandTo, true);
//			currBFSPath = bfs.search();
//			mouldHeads.put(head, currBFSPath);
//		}
//		// Get next node from A*s path.
//		Node currNode = currBFSPath.pop();
//		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
//		spreadTo(currNeighbor);
//	}
//
//	private void specificExpand(Mould head) {
//		LinkedList<Node> currAStarPath = mouldHeads.get(head);
//		if (currAStarPath == null || currAStarPath.size() == 0) {
//			Node toExpandTo = getExpansionNode(head);
//			AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(head), toExpandTo, true);
//			currAStarPath = astar.search();
//			mouldHeads.put(head, currAStarPath);
//		}
//		// Get next node from A*s path.
//		Node currNode = currAStarPath.pop();
//		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
//		spreadTo(currNeighbor);
//	}
//
//	private Node getExpansionNode(Mould head) {
//		Random rand = new Random();
//		int xMove, newX, xPos;
//		int yMove, newY, yPos;
//		Mould currMould = head;
//		Element currNeighbor;
//		do {
//			// Generate the X and Y positions for the next move. Logic.Mould will try and move away from the head.
//			boolean randPicker = rand.nextBoolean();
//			xMove = currMould.generateXMove(randPicker, head) * rand.nextInt(3);
//			yMove = currMould.generateYMove(randPicker, head) * rand.nextInt(3);
//
//			// Get the actual neighbor from the grid of tiles.
//			xPos = currMould.getXPos();
//			yPos = currMould.getYPos();
////			if ((xMove * yMove != 0) || (Math.abs(xMove) + Math.abs(yMove) == 0)) {
////				log("No such thing!");
////			}
//			// TODO: The code below can enter an endless loop when no available tile exists?
//			newX = ((xPos + xMove >= 0) && (xPos + xMove <= X_TILES - 1)) ? xPos + xMove : -1;
//			newY = ((yPos + yMove >= 0) && (yPos + yMove <= Y_TILES - 1)) ? yPos + yMove : -1;
//			currNeighbor = ((newX < 0) || (newY < 0)) ? null : worldGrid[newX][newY];
//			if (currNeighbor == null) {
//				continue;
//			} else if (currNeighbor.getType() != MOULD_TYPE) {
//				return nodePool.getNode(currNeighbor);
//			} else {
//				currMould = (Mould) currNeighbor;
//			}
//			spreadTo(currNeighbor);
//		} while (true);
//	}
//
//
//

//

//

//

//

//

//

//
//// Helpers //
//

//
}
