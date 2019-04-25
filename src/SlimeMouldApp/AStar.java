package SlimeMouldApp;

import java.util.*;


public class AStar {

    private static NodeMap nodePool;

    private Node start;
    private Node goal;
    private HashSet<Node> closedSet;
    private PriorityQueue<Node> openSet;
    private HashMap<Node, Node> cameFrom;
    private HashMap<Node, Double> gScore;
    private HashMap<Node, Double> fScore;
    private Node currNode;
    private HashSet<Node> currNeighbors;


    public AStar(NodeMap nodePool, Node start, Node goal) {
        if (AStar.nodePool == null) {
            AStar.nodePool = nodePool;
        }
        this.start = start;
        this.goal = goal;

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

        // Initially, only the start node is know.
        openSet.add(start);

        // The cost of going from start to start is zero.
        gScore.put(start, 0.0);


        // For the first node, that value is completely heuristic.
        fScore.put(start, h(start));

        // Init currNeighbors
        currNeighbors = new HashSet<>();
    }

    private double h(Node node) {
        return Math.abs((node.xPos - goal.xPos + Math.abs((node.yPos - goal.yPos))));
    }

    public Node search() {

        if (openSet.size() == 0) {
            // Do something
        }


        // The node in openSet having the lowest f()
        currNode = openSet.poll();
        closedSet.add(currNode);

        // If we ran through all neighbors
        if (currNeighbors.size() == 0) {
            currNeighbors = new HashSet<>(currNode.neighbors);
        }

//        closedSet.add(currNode);  // Why is this here?
        if (currNode == goal) {
            return goal;
        }

        Iterator<Node> neighbors = currNeighbors.iterator();
        Node neighbor;
        do {
            neighbor = neighbors.next();
            // What if neighbor becomes null? Can this even happen?
        } while (closedSet.contains(neighbor));


        // The distance from start to a neighbor

        double tentGScore = gScore.get(currNode) + 1;
        while (openSet.contains(neighbor) && tentGScore >= gScore.get(neighbor) {
            openSet.remove(neighbor);
            openSet.add(neighbor);
            neighbor = neighbors.next();
        }

        if (!openSet.contains(neighbor)) {
            openSet.add(neighbor);
        }

        // This path is the best up to now. Record it!
        cameFrom.put(neighbor, currNode);
        gScore.put(neighbor, tentGScore);
        fScore.put(neighbor, tentGScore + h(neighbor));
        return neighbor;

    }

//
//    public LinkedList<Node> search() {
//
//        while (openSet.size() > 0) {
//            // The node in openSet having the lowest f()
//            currNode = openSet.poll();
//            closedSet.add(currNode);
//            if (currNode == goal) {
//                return reconstructPath();
//            }
//
//            for (Node neighbor : currNode.neighbors) {
//                // Ignore neighbors which were already evaluated.
//                if (closedSet.contains(neighbor)) {
//                    continue;
//                }
//
//                // The distance from start to a neighbor
//                double tentGScore = gScore.get(currNode) + 1;
//
//                // Discover a new node
//                if (!openSet.contains(neighbor)) {
//                    openSet.add(neighbor);  // TODO: cut contains() time with visited field of node?
//                } else if (tentGScore >= gScore.get(neighbor)) {
//                    openSet.remove(neighbor);
//                    openSet.add(neighbor);
//                    continue;
//                }
//
//                // This path is the best up to now. Record it!
//                cameFrom.put(neighbor, currNode);
//                gScore.put(neighbor, tentGScore);
//                fScore.put(neighbor, tentGScore + h(neighbor));
//
//            }
//
//        }
//
//        return null; // Never executes
//    }

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