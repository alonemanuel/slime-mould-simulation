package Logic;

import java.util.*;

public class BFS {
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

	private LinkedList<Node> S;
	private HashSet<Node> discovered;
	private static int frame;

	public BFS(Element[][] worldGrid, NodeMap nodePool, Node start, Node goal,
			   boolean isExpanding) {

		S = new LinkedList<>();
		discovered = new HashSet<>();

		// Avoid time consuming creations of node pools.
		if (BFS.nodePool == null) {
			BFS.nodePool = nodePool;
		}
		if (BFS.worldGrid == null) {
			BFS.worldGrid = worldGrid;
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
		frame++;
	}

	public LinkedList<Node> search() {
		LinkedList<Node> alternative = new LinkedList<>();
		alternative.add(start);
		S.addFirst(start);
		discovered.add(start);
		Random rand = new Random();
		while (!S.isEmpty()) {
			Node currNode = S.poll();
			if (currNode.getManhattanTo(start) > frame) {
				goal =currNode;
//				return reconstructPath();
				return alternative;
			}
			for (Node neighbor : currNode.neighbors) {
				if (!discovered.contains(neighbor)) {
					discovered.add(neighbor);
					cameFrom.put(neighbor, currNode);
					S.addFirst(neighbor);
//					if (rand.nextBoolean()) {
//					}
				}
				alternative.add(neighbor);
			}
		}
		return null;
	}

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
