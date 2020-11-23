package tsp;

import java.util.*;

public class Dijkstra {
    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public static Dijkstra calculateShortestPathFromSource(Dijkstra graph, Node source) {
        source.setDistance(0);

        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();

        unvisitedNodes.add(source);

        while (unvisitedNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);
            for (Map.Entry< Node, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!visitedNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unvisitedNodes.add(adjacentNode);
                }
            }
            visitedNodes.add(currentNode);
        }
        return graph;
    }

    /**
     *  renvoie le nœud dont la distance est la plus basse du jeu de nœuds non définis
     * @param unvisitedNodes
     * @return
     */
    private static Node getLowestDistanceNode(Set < Node > unvisitedNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unvisitedNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    /**
     * compare la distance réelle à celle nouvellement calculée tout en suivant le chemin récemment exploré
     * @param evaluationNode
     * @param edgeWeigh
     * @param sourceNode
     */
    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
