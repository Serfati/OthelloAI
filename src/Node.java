import java.util.ArrayList;
import java.util.List;

public class Node {
    private IBoard board;

    ;
    private NodeType nodeType;

    public Node(IBoard board, NodeType nodeType) {
        this.nodeType = nodeType;
        this.board = board;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public NodeType getNodeOtherType() {
        if (nodeType == NodeType.MAX)
            return NodeType.MIN;
        else
            return NodeType.MAX;
    }

    public List<Node> getNodeChildren() {
        List<Node> children = new ArrayList<Node>();
        List<IMove> legalMoves = board.getLegalMoves();
        for(IMove move : legalMoves) {
            IBoard newBoard = board.getNewChildBoard(move);
            NodeType newNodeType;
            if (board.getCurrentPlayer() == newBoard.getCurrentPlayer())
                newNodeType = nodeType;
            else
                newNodeType = getNodeOtherType();

            Node newNode = new Node(newBoard, newNodeType);
            children.add(newNode);
        }
        return children;
    }

    public IBoard getBoard() {
        return board;
    }

    public boolean isTerminalNode() {
        if (board.isTheGameOver())
            return true;
        else
            return false;
    }

    public double getScore() {
        return board.getScore();
    }

    public enum NodeType {MAX, MIN}
}
