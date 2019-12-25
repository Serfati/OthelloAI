public class AlphaBetaPruning implements ISolver {
    @Override
    public String getSolverName() {
        return "Alpha-Beta Pruning";
    }

    @Override
    public double solve(IBoard board) {
        Node root = new Node(board, Node.NodeType.MAX);
        return AlphaBetaPruningAlgorithm(root, -Double.MAX_VALUE, Double.MAX_VALUE);
    }


    private double AlphaBetaPruningAlgorithm(Node node, double p_alpha, double p_beta) {
        double nodeValue = node.getNodeType() == Node.NodeType.MAX ? -Double.MAX_VALUE : Double.MAX_VALUE;
        for(Node child : node.getNodeChildren())
            if (p_beta > p_alpha) {
                double childValue;
                if (!child.isTerminalNode())
                    childValue = p_beta > p_alpha ? AlphaBetaPruningAlgorithm(child, p_alpha, p_beta) : child.getScore();
                else {
                    childValue = child.getScore();
                    switch(node.getNodeType()) {
                        case MIN:
                            p_beta = childValue;
                            break;
                        case MAX:
                            p_alpha = childValue;
                            break;
                    }
                }
                switch(node.getNodeType()) {
                    case MIN:
                        nodeValue = Double.min(nodeValue, childValue);
                        if (p_beta > childValue)
                            p_beta = childValue;
                        if (p_beta > nodeValue)
                            p_beta = nodeValue;
                        break;
                    case MAX:
                        nodeValue = Double.max(nodeValue, childValue);
                        if (p_alpha < childValue)
                            p_alpha = childValue;
                        if (p_alpha < nodeValue)
                            p_alpha = nodeValue;
                        break;
                }
            }
        return nodeValue;
    }
}