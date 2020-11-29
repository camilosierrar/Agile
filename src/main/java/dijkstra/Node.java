package dijkstra;

import java.util.HashMap;
import java.util.Map;

import config.Config.Type_Request;

public class Node {
    private long id;
    private Double distance = Double.MAX_VALUE;
    private Map<Node, Double> adjacentNodes = new HashMap<>();
    private Type_Request typeOfNode;

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

    public Type_Request getTypeOfNode() {
        return typeOfNode;
    }

    public void setTypeOfNode(Type_Request pTypeOfNode) {
        this.typeOfNode = pTypeOfNode;
    }

    public Map<Node, Double> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Node)) return false;
        Node n = (Node) obj;
        return n.id == this.id;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", distance='" + getDistance() + "'" +
            //", adjacentNodes='" + getAdjacentNodes() + "'" +
            ", typeOfNode='" + getTypeOfNode() + "'" +
            "}";
    }

}
