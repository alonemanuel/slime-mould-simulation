package SlimeMouldApp;

import java.util.HashSet;
import java.util.Set;

public class Node {
    public int xPos;
    public int yPos;
    public Set<Node> neighbors;

    public Node(int x, int y) {
        xPos = x;
        yPos = y;
        neighbors = new HashSet<>();
    }

    public void addNeighbor(Node neighbor) {
        if (neighbor != null) {
            neighbors.add(neighbor);
        }
    }
}

