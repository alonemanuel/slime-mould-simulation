package Logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class NodeMap {

    private int X, Y;
    private HashMap<Integer, Map<Integer, Node>> _nodeMap;
    private HashSet<Node> _nodeSet;

    public NodeMap(int _X, int _Y) {
        X = _X;
        Y = _Y;
        _nodeMap = new HashMap<>();
        _nodeSet = new HashSet<>();

        Map<Integer, Node> _yMap;
        Node currNode;
        for (int x = 0; x < _X; x++) {
            _yMap = new HashMap<>();
            _nodeMap.put(x, _yMap);
            for (int y = 0; y < Y; y++) {
                currNode = new Node(x, y);
                _yMap.put(y, currNode);
                _nodeSet.add(currNode);
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

    public Node getNode(Element element) {
        return getNode(element._xPos, element._yPos);
    }

    public Node getNode(int x, int y) {
        Map<Integer, Node> xNode = _nodeMap.get(x);
        if (xNode != null) {
            return xNode.get(y);
        } else {
            return null;
        }
    }


    public <E> HashMap<Node, E> getNodeMap(E defaultVal) {
        HashMap<Node, E> asMap = new HashMap<>();
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                asMap.put(_nodeMap.get(x).get(y), defaultVal);
            }
        }
        return asMap;
    }

    public HashSet<Node> getNodeSet() {
        return _nodeSet;
    }


}

