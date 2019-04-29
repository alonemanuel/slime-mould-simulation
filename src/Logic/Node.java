package Logic;

import java.util.HashSet;
import java.util.Set;

/**
 * A class representing a node.
 */
public class Node {
	public int xPos;
	public int yPos;
	public Set<Node> neighbors;

	/**
	 * Constructor for a node.
	 *
	 * @param x
	 * @param y
	 */
	public Node(int x, int y) {
		xPos = x;
		yPos = y;
		neighbors = new HashSet<>();
	}

	/**
	 * Adds neighbor to this node if neighbor is not null.
	 *
	 * @param neighbor
	 */
	public void addNeighbor(Node neighbor) {
		if (neighbor != null) {
			neighbors.add(neighbor);
		}
	}

	/**
	 * @return manhattan distance to other node.
	 */
	public double getManhattanTo(Node other) {
		return Math.abs(xPos - other.xPos) + Math.abs(yPos - other.yPos);
	}
}

