package Manager;

// Imports //

import Logic.*;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

import static Logic.Element.*;

/**
 * A class representing a Slime Mould2 Manager.
 */
public class SlimeManager {

	/**
	 * Size of tile (height == width == size)
	 */
	public static final int REPR_SIZE = 10;

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
	private static final int NUM_OF_FOODS = 8;
	/**
	 * Below this thresh, moulds disappear.
	 */
	private static final double DISAPPEAR_THRESH = 0.001;
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
	private HashMap<Food, LinkedList<Node>> foodsFound;
	/**
	 * Mould heads.
	 */
	private HashMap<Mould, LinkedList<Node>> mouldHeads;
	private HashSet<Mould> vains;
	private LinkedList<Mould> edges;
	private Mould mouldHead;
	private LinkedList<Mould> edgesToRemove;
	private LinkedList<Mould> edgesToAdd;
	private boolean didGetFood = false;
	/**
	 * Animation timer of the program.
	 */
	AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long l) {
			moveSlime();
		}
	};

	// Methods //

	/**
	 * Default ctor.
	 */
	public SlimeManager(Pane pane) {
		vains = new HashSet<>();
		edges = new LinkedList<>();
		edgesToRemove = new LinkedList<>();
		edgesToAdd = new LinkedList<>();

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
		reenergizeMoulds();
		// Move according to the expansion rate.
		for (int i = 0; i < EXPANSION_RATE; i++) {
			if (edges.isEmpty()) {
				edges.addFirst(mouldHead);
			}
			getFood5();
			searchForFood5();
		}
	}

	private void getFood5() {
		HashSet<Food> toRemove = new HashSet<>();
		for (Food food : foodsFound.keySet()) {
			if (foodsFound.get(food).isEmpty()) {
				toRemove.add(food);
			} else {
			// Get next node from A*s path.
			Node currNode = foodsFound.get(food).pop();
			Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
			spreadTo(currNeighbor);  // TODO: Should be spawnTO?
				vains.add((Mould) currNeighbor);
			}
		}
		foodsFound.keySet().removeAll(toRemove);
	}

	private void searchForFood5() {
		edgesToRemove.clear();
		edgesToAdd.clear();
		for (Mould edge : edges) {
			for (int i = 0; i < 3; i++) {

			expandEdge(edge);
			}
		}
		edges.removeAll(edgesToRemove);
		edges.addAll(edgesToAdd);
	}

	private void expandEdge(Mould edge) {
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
			return;
		}
		switch (currNeighbor.getType()) {
			case MOULD_TYPE:
				return;
			case EMPTY_TYPE:
				Mould spawned = spawnTo(currNeighbor);
				edgesToAdd.add(spawned);
				return;
			case FOOD_TYPE:
				edgeEatFood((Food) currNeighbor);
				return;
		}
	}

	private Mould spawnTo(Element toSpawnTo) {
		int xPos = toSpawnTo.getXPos();
		int yPos = toSpawnTo.getYPos();
		Mould toSpread = new Mould(xPos, yPos);
		replace(toSpread);    // TODO: replace instead of place?
		return toSpread;
	}

	private void edgeEatFood(Food food) {
		AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(mouldHead),
				nodePool.getNode(food), false);
		foodsFound.put(food, astar.search());
		// Set food as the new head
		mouldHead = spawnTo(food);
		edgesToRemove.addAll(edges);
		edgesToAdd.add(mouldHead);
	}

////////////////////////////////////////

	/**
	 * Moves slime.
	 */
	private void moveSlime2() {
		// Before each movement, reenergize moulds.
		reenergizeMoulds();
		// Move according to the expansion rate.
		for (int i = 0; i < EXPANSION_RATE; i++) {
			getFood();
			searchForFood();
		}
	}

	private void getFood() {
		for (Food food : foodsFound.keySet()) {
			getSpecificFood(food);
		}
	}

	private void getSpecificFood(Food food) {
		LinkedList<Node> currAStarPath = foodsFound.get(food);
		if (currAStarPath == null || currAStarPath.size() == 0) {
			AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(mouldHead),
					nodePool.getNode(food), false);
			currAStarPath = astar.search();
			foodsFound.put(food, currAStarPath);
		}
		// Get next node from A*s path.
		Node currNode = currAStarPath.pop();
		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
		spreadTo(currNeighbor);
	}

	private void searchForFood() {
		for (Mould head : mouldHeads.keySet()) {
			specificExpand3(head);
		}
		mouldHeads.putAll(headsToAdd);
		headsToAdd.clear();
	}

	private void specificExpand2(Mould head) {
		frame++;
		LinkedList<Node> closedSet = new LinkedList<>();
		HashSet<Node> discovered = new HashSet<>();
		closedSet.addFirst(nodePool.getNode(head));
		Random rand = new Random();
		while (!closedSet.isEmpty()) {
			Node currNode = closedSet.poll();
			if (currNode.getManhattanTo(nodePool.getNode(head)) > frame) {
				return;
			}
			for (Node neighbor : currNode.neighbors) {
				if (!discovered.contains(neighbor)) {
					discovered.add(neighbor);
					closedSet.addFirst(neighbor);
					if (rand.nextBoolean()) {
					}
					spreadTo(worldGrid[neighbor.xPos][neighbor.yPos]);
				}
			}
		}
	}

	private void specificExpand3(Mould head) {
		LinkedList<Node> currBFSPath = mouldHeads.get(head);
		if (currBFSPath == null || currBFSPath.size() == 0) {
			Node toExpandTo = getExpansionNode(head);
			BFS bfs = new BFS(worldGrid, nodePool, nodePool.getNode(head), toExpandTo, true);
			currBFSPath = bfs.search();
			mouldHeads.put(head, currBFSPath);
		}
		// Get next node from A*s path.
		Node currNode = currBFSPath.pop();
		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
		spreadTo(currNeighbor);
	}

	private void specificExpand(Mould head) {
		LinkedList<Node> currAStarPath = mouldHeads.get(head);
		if (currAStarPath == null || currAStarPath.size() == 0) {
			Node toExpandTo = getExpansionNode(head);
			AStar astar = new AStar(worldGrid, nodePool, nodePool.getNode(head), toExpandTo, true);
			currAStarPath = astar.search();
			mouldHeads.put(head, currAStarPath);
		}
		// Get next node from A*s path.
		Node currNode = currAStarPath.pop();
		Element currNeighbor = worldGrid[currNode.xPos][currNode.yPos];
		spreadTo(currNeighbor);
	}

	private Node getExpansionNode(Mould head) {
		Random rand = new Random();
		int xMove, newX, xPos;
		int yMove, newY, yPos;
		Mould currMould = head;
		Element currNeighbor;
		do {
			// Generate the X and Y positions for the next move. Logic.Mould will try and move away from the head.
			boolean randPicker = rand.nextBoolean();
			xMove = currMould.generateXMove(randPicker, head) * rand.nextInt(3);
			yMove = currMould.generateYMove(randPicker, head) * rand.nextInt(3);

			// Get the actual neighbor from the grid of tiles.
			xPos = currMould.getXPos();
			yPos = currMould.getYPos();
//			if ((xMove * yMove != 0) || (Math.abs(xMove) + Math.abs(yMove) == 0)) {
//				log("No such thing!");
//			}
			// TODO: The code below can enter an endless loop when no available tile exists?
			newX = ((xPos + xMove >= 0) && (xPos + xMove <= X_TILES - 1)) ? xPos + xMove : -1;
			newY = ((yPos + yMove >= 0) && (yPos + yMove <= Y_TILES - 1)) ? yPos + yMove : -1;
			currNeighbor = ((newX < 0) || (newY < 0)) ? null : worldGrid[newX][newY];
			if (currNeighbor == null) {
				continue;
			} else if (currNeighbor.getType() != MOULD_TYPE) {
				return nodePool.getNode(currNeighbor);
			} else {
				currMould = (Mould) currNeighbor;
			}
			spreadTo(currNeighbor);
		} while (true);
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
//		if (Mould.maxTimesPast < mould.timesPast) {
//			Mould.maxTimesPast = mould.timesPast;
//			if (!mouldHeads.containsKey(mould)) {
//
//				headsToAdd.put(mould, null);
//			}
//		}
		mould.saturate();
		double opacity = ((Color) mould.getElementRepr().getFill()).getOpacity();
	}

	/**
	 * Eats given food found.
	 */
	public void eatFood(Food currFood) {
		Mould.setFoundFood(true);
		spreadToEmpty(currFood);
		currFood.desaturate();
		if (((Color) currFood.getElementRepr().getFill()).getOpacity() < DISAPPEAR_THRESH) {
			Mould newMould = new Mould(currFood._xPos, currFood._yPos);
			headsToAdd.put(newMould, null);
			replace(newMould);
			Mould.setFoundFood(false);
		}
	}

	public void spreadToEmpty(Element currNeighbor) {
		int xPos = currNeighbor.getXPos();
		int yPos = currNeighbor.getYPos();
		Mould toSpread = new Mould(xPos, yPos);
		place(toSpread);    // TODO: replace instead of place?
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
		for (int i = 0; i < NUM_OF_FOODS; i++) {
			int randX = rand.nextInt(X_TILES);
			int randY = rand.nextInt(Y_TILES);
			place(new Food(randX, randY));

//            addFood(randX, randY);
		}
	}

	private void replace(Element toPlace) {
		Element toRemove = worldGrid[toPlace._xPos][toPlace._yPos];
		worldPane.getChildren().remove(toRemove);
		place(toPlace);
	}

	private void place(Element toPlace) {
		worldGrid[toPlace._xPos][toPlace._yPos] = toPlace;
		worldPane.getChildren().add(toPlace.getElementRepr());
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
		place(head);
	}

// Helpers //

	public void restart() {
		timer.stop();
	}

	/**
	 * Goes over all moulds and normalizes them (energy-wise).
	 */
	private void reenergizeMoulds() {
		for (int x = 0; x < X_TILES; x++) {
			for (int y = 0; y < Y_TILES; y++) {
				Element currElem = worldGrid[x][y];
				if (currElem.getType() == MOULD_TYPE && !vains.contains(currElem)) {
					currElem.desaturate();
					// If current elements reaches a minimal threshold opacity, it should disappear.
					if (((Color) currElem.getElementRepr().getFill()).getOpacity() < DISAPPEAR_THRESH) {
						replace(new Empty(x, y));
					}
				}
			}
		}
	}
}
