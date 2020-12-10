package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import config.Config.Type_Request;

/**
 * Encapsulates model information (intersections, segments,...) in a data structure adapted to graphs (complete graph, Dijkstra)
 */
public class Node implements Serializable {
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

    public String printAdjacentNodes() {
        String msg ="{";
        for (Map.Entry<Node, Double> node : this.getAdjacentNodes().entrySet()) {
            msg += "id =" + node.getKey().id + " edge weight =" + node.getValue() +", ";
        }
        msg +="}";
        return msg;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", distance='" + getDistance() + "'" +
            //", adjacentNodes='" + printAdjacentNodes()  + "'" +
            //", typeOfNode='" + getTypeOfNode() + "'" +
            "}";
    }

}
