package dijkstra;

import model.*;
import tsp.Graph;

import java.util.*;

/**
 * Implements Dijkstra's algorithm and compute for every point of interest
 * the shortest path
 */
public class Dijkstra implements Graph {
    private Map<Node,Node> parentNode;
    private Plan cityPlan;
    private Tour tour;
    private Set<Node> nodes;
    private Set<Node> pointsInterest;

    public Dijkstra(Plan cityPlan, Tour tour) {
        this.cityPlan = cityPlan;
        this.tour = tour;
        this.nodes = new HashSet<>();
        this.pointsInterest = new HashSet<>();
        this.parentNode = new HashMap<>();
        fillDijkstra();
    }

    private void fillDijkstra(){
        //Fetches all intersections
        HashMap<Long,Intersection> intersections = this.cityPlan.getIntersections();
        for(Map.Entry<Long,Intersection> entry: intersections.entrySet()){
            Intersection intersection = entry.getValue();
            Node originNode = new Node(intersection.getId());
            for(Segment segment: this.cityPlan.getSegments()){
                Intersection origin = segment.getOrigin();
                if(originNode.getId() == origin.getId()){
                    Intersection dest = segment.getDestination();
                    Node destination = new Node(dest.getId());
                    originNode.addDestination(destination, segment.getLength());
                }
            }
            this.nodes.add(originNode);
        }

        //Fetches all points of interest
        Node addressDeparture = findNode(this.tour.getAddressDeparture().getId());
        this.pointsInterest.add(addressDeparture);
        List<Request> requests = this.tour.getRequests();
        for(Request request: requests){
            Node pickupAddress = findNode(request.getPickupAddress().getId());
            Node deliveryAddress = findNode(request.getDeliveryAddress().getId());
            this.pointsInterest.add(pickupAddress);
            this.pointsInterest.add(deliveryAddress);
        }
    }

    public Dijkstra calculateShortestPathFromSource(Dijkstra graph, Node source) {
        source.setDistance(0);
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        unvisitedNodes.add(source);
        while (!isConditionFulfilled(unvisitedNodes, visitedNodes)) {
            //Gets the node with minimal distance on the graph
            Node currentClosestNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentClosestNode);
            //For each of its successors : 
            for (Map.Entry< Node, Double> adjacencyNode : currentClosestNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = nodes.stream().filter(node -> node.getId() == adjacencyNode.getKey().getId()).findFirst().orElse(null) ;
                if(adjacencyNode != null) {
                    Double edgeWeight = adjacencyNode.getValue();
                    if (!visitedNodes.contains(adjacentNode)) {
                        //Computes distance of adjacentNode assuming last node is its parent
                        //If distance computed is less than the one adjacentNode had, its parentNode and distance are updated
                        calculateMinimumDistance(adjacentNode, edgeWeight, currentClosestNode);
                        //Adds adjacentNodes to unvisitedNode step by step in order to avoid overloading memory, and for better performance
                        unvisitedNodes.add(adjacentNode);
                    }
                }
            }
            visitedNodes.add(currentClosestNode);
        }
        return graph;
    }

    private boolean isConditionFulfilled(Set<Node> unvisitedNodes, Set<Node> visitedNodes) {
        boolean isFulfilled = false;
        if(unvisitedNodes.size()==0)
            isFulfilled=  true;
        if(areAllPointsOfInterestVisited(visitedNodes))
            isFulfilled=  true;
        return isFulfilled;
    }

    private boolean areAllPointsOfInterestVisited(Set<Node> nodesVisited) {
        Set<Long> nodesVisitedId = new HashSet<>();
        Set<Long> nodesInterestingId = new HashSet<>();
        for (Node node : nodesVisited) {
            nodesVisitedId.add(node.getId());
        }
        for (Node node : this.pointsInterest) {
            nodesInterestingId.add(node.getId());
        }
        return nodesVisitedId.containsAll(nodesInterestingId);
    }

    /**
     *  Get the node within unvisitedNodes with minimal distance 
     * @param unvisitedNodes
     * @return
     */
    private Node getLowestDistanceNode(Set < Node > unvisitedNodes) {
        Node lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Node node: unvisitedNodes) {
            double nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    /**
     * Compare the current value of distance of evaluationNode with a new one computed
     * assuming sourceNode is its parent
     * @param evaluationNode
     * @param edgeWeigh
     * @param sourceNode
     */
    private void calculateMinimumDistance(Node evaluationNode, Double edgeWeigh, Node sourceNode) {
        Double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            parentNode.put(evaluationNode, sourceNode);
        }
    }

    public Node findNode(long id){
        for(Node node: this.nodes){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public LinkedList<Node> getShortestPath(Node destination, Node source) {
        LinkedList<Node> shortestPath = new LinkedList<>();
        shortestPath.add(destination);
        Node currentNode = destination;
        Node parent = null;
        while(currentNode != source) {
            parent = parentNode.get(currentNode);
            shortestPath.add(parent);
            currentNode = parent;
        }
        return shortestPath;
    }

    public double getCostOfShortestPath(LinkedList<Node> shortestPath) {
        double cost = 0;
        for(Node node : shortestPath) {
            cost += node.getDistance();
        }
        return cost;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Node> getPointsInterest() {
        return pointsInterest;
    }

    public Map<Node,Node> getParentNodes() {
        return parentNode;
    }

    //TODO
    @Override
    public int getNbVertices() {
        return nodes.size();
    }

    @Override
    public int getCost(int i, int j) {
        return 0;
    }

    @Override
    public boolean isArc(int i, int j) {
        return false;
    }



}
