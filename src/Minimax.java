public class Minimax implements ISolver {
    @Override
    public String getSolverName() {
        return "Minimax";
    }

    @Override
    public double solve(IBoard board) {
        Node root = new Node(board, Node.NodeType.MAX);
        return MinimaxAlgorithm(root);
    }

    private double MinimaxAlgorithm(Node node) {
        double nodeValue = node.getNodeType() == Node.NodeType.MAX ? -Integer.MAX_VALUE : Integer.MAX_VALUE;
        for(Node child : node.getNodeChildren()) {
            double childValue = child.isTerminalNode() ? child.getScore() : MinimaxAlgorithm(child);
            nodeValue = node.getNodeType() == Node.NodeType.MAX ? Double.max(nodeValue, childValue) : Double.min(nodeValue, childValue);
        }
        return nodeValue;
    }
}
