package Logic;

import java.util.*;

/**
 * Class representing an A* algorithm.
 */
public class AStar {

	/**
	 * Node pool to work on.
	 */
	private static NodeMap nodePool;
	private static Element[][] worldGrid;
	/**
	 * Start node.
	 */
	private Node start;
	/**
	 * Goal node.
	 */
	private Node goal;
	/**
	 * Closed set.
	 */
	private HashSet<Node> closedSet;
	/**
	 * Open set.
	 */
	private PriorityQueue<Node> openSet;
	/**
	 * Map of 'came from' nodes.
	 */
	private HashMap<Node, Node> cameFrom;
	/**
	 * Map of g scores.
	 */
	private HashMap<Node, Double> gScore;
	/**
	 * Map of f scored.
	 */
	private HashMap<Node, Double> fScore;
	private boolean isExpanding;

	/**
	 * Constructor.
	 */
	public AStar(Element[][] worldGrid, NodeMap nodePool, Node start, Node goal, boolean isExpanding) {
		// Avoid time consuming creations of node pools.
		if (AStar.nodePool == null) {
			AStar.nodePool = nodePool;
		}
		if (AStar.worldGrid == null) {
			AStar.worldGrid = worldGrid;
		}
		this.start = start;
		this.goal = goal;
		this.isExpanding = isExpanding;

		// The set of nodes already evaluated
		closedSet = new HashSet<>();
		// The set of currently discovered nodes that are not evaluated yet.
		openSet = new PriorityQueue<>(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return Double.compare(fScore.get(o1), fScore.get(o2));
			}
		});

		// For each node, which node it can most efficiently be reached from.
		// If a node can be reached from many nodes, cameFrom will eventually contain the most
		// efficient previous step
		cameFrom = nodePool.getNodeMap(null);

		// For each node, the cost of getting from the start node to that node.
		gScore = nodePool.getNodeMap(Double.POSITIVE_INFINITY);

		// For each node, the total cost of getting from the start node to the goal by passing by
		// that node. That value is partly known, partly heuristic
		fScore = nodePool.getNodeMap(Double.POSITIVE_INFINITY);
	}

	/**
	 * Heuristic function.
	 *
	 * @return manhattan distance to goal node.
	 */
	private double h(Node node) {
		// TODO: Add infinity to nodes outside of moulds.
		double manhattan = node.getManhattanTo(goal);
		boolean isMould = worldGrid[node.xPos][node.yPos].getType() == Element.MOULD_TYPE;
		if (isMould && isExpanding) {
			return node.getManhattanTo(goal) + 3;
		}
		if ((!isMould) && (!isExpanding)) {
			return node.getManhattanTo(goal) + 3;
		}
		return node.getManhattanTo(goal);
	}

	/**
	 * Searches for a path from start to goal.
	 *
	 * @return best path as a linked list.
	 */
	public LinkedList<Node> search() {
		// Initially, only the start node is know.
		openSet.add(start);

		// The cost of going from start to start is zero.
		gScore.put(start, 0.0);

		// For the first node, that value is completely heuristic.
		fScore.put(start, h(start));

		Node currNode;
		while (openSet.size() > 0) {
			// The node in openSet having the lowest f()
			currNode = openSet.poll();
			closedSet.add(currNode);
			if (currNode == goal) {
				return reconstructPath();
			}

			for (Node neighbor : currNode.neighbors) {
				// Ignore neighbors which were already evaluated.
				if (closedSet.contains(neighbor)) {
					continue;
				}

				// The distance from start to a neighbor
				double tentGScore = gScore.get(currNode) + 1;

				// Discover a new node
				if (!openSet.contains(neighbor)) {
					openSet.add(neighbor);  // TODO: cut contains() time with visited field of node?
				} else if (tentGScore >= gScore.get(neighbor)) {
					openSet.remove(neighbor);
					openSet.add(neighbor);
					continue;
				}

				// This path is the best up to now. Record it!
				cameFrom.put(neighbor, currNode);
				gScore.put(neighbor, tentGScore);
				fScore.put(neighbor, tentGScore + h(neighbor));
			}
		}

		return null; // Never executes
	}

	/**
	 * Reconstructs path from cameFrom map.
	 *
	 * @return linked list which is the path.
	 */
	private LinkedList<Node> reconstructPath() {
		LinkedList<Node> path = new LinkedList<>();
		Node curr = goal;
		path.addFirst(goal);
		while (cameFrom.get(curr) != null) {
			curr = cameFrom.get(curr);
			path.addFirst(curr);
		}
		return path;
	}
}
