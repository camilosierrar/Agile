package tsp;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private long id;
    private Double distance = Double.MAX_VALUE;
    Map<Node, Double> adjacentNodes = new HashMap<>();

    public Node(long id){
        this.id = id;
    }

    public void addDestination(Node destination, double distance){
        adjacentNodes.put(destination, distance);
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Map<Node, Double> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }
}
