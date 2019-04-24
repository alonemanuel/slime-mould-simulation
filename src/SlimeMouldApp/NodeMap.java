package SlimeMouldApp;

import java.util.HashMap;
import java.util.Map;


public class NodeMap {

    private int X, Y;
    private Map<Integer, Map<Integer, Node>> _nodeMap;

    public NodeMap(int _X, int _Y) {
        X = _X;
        Y = _Y;
        _nodeMap = new HashMap<>();
        Map<Integer, Node> _yMap;
        for (int x = 0; x < _X; x++) {
            _yMap = new HashMap<>();
            _nodeMap.put(x, _yMap);
            for (int y = 0; y < Y; y++) {
                _yMap.put(y, new Node(x, y));
            }
        }
        createEdges();
    }

    private void createEdges() {
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                Node currNode = getNode(x, y);
                currNode.addNeighbor(getNode(x, y - 1));
                currNode.addNeighbor(getNode(x, y + 1));
                currNode.addNeighbor(getNode(x - 1, y));
                currNode.addNeighbor(getNode(x + 1, y));
            }
        }
    }

    public Node getNode(int x, int y) {
        return _nodeMap.get(x).get(y);
    }


}

