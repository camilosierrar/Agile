package dijkstra;

import config.Variable;
import model.*;
import java.util.*;
import config.Config.Type_Request;

/**
 * Implements Dijkstra's algorithm and computes for every point of interest shortest path to other points of interest
 */
public class Dijkstra{
    /**
     * Value node is key Node's parent with the shortest path
     */
    private Map<Node,Node> parentNode;


    /**
     * All distinct intersections of Plan object
     */
    private Set<Node> graphPlan;

    /**
     * Every pickup, delivery and departure addresses
     */
    private Set<Node> pointsInterest;

    private static Map<Node,Node> pickUpDeliveryCouples;


    /**
     * Instantiates Dijkstra and fill its variables given Tour and Plan object
     */
    public Dijkstra() {
        this.graphPlan = new HashSet<>();
        this.parentNode = new HashMap<>();
        this.pointsInterest = new HashSet<>();
        Dijkstra.pickUpDeliveryCouples = new HashMap<>();
        fillDijkstra();
    }

    /**
     * From a Plan and a Tour object, retrieves and stores all informations as nodes
     * Fills graphPlan, pickup/delivery couples and points of interest
     */
    private void fillDijkstra(){
        //Stores all intersections in graphPlan
        HashMap<Long,Intersection> intersections = Variable.cityPlan.getIntersections();
        for(Map.Entry<Long,Intersection> entry: intersections.entrySet()){
            Intersection intersection = entry.getValue();
            Node originNode = new Node(intersection.getId());
            this.graphPlan.add(originNode);
        }

        //Set adjacent Nodes for each segments
        for(Map.Entry<Long,Intersection> entry: intersections.entrySet()){
            Intersection intersection = entry.getValue();
            Node originNode = findNodeGraph(intersection.getId());
            //Iterates over all segment from Plan and adds adjacent nodes
            for(Segment segment: Variable.cityPlan.getSegments()){
                Intersection origin = segment.getOrigin();
                if(originNode.getId() == origin.getId()){
                    Intersection dest = segment.getDestination();
                    Node destination = findNodeGraph(dest.getId());
                    originNode.addDestination(destination, segment.getLength());
                }
            }
        }

        //Stores all points of interest and pickup/delivery couple
        //Stores departure address
        Node addressDeparture = findNodeGraph(Variable.tour.getAddressDeparture().getId());
        addressDeparture.setTypeOfNode(Type_Request.DEPARTURE_ADDRESS);
        this.pointsInterest.add(addressDeparture);
        List<Request> requests = Variable.tour.getRequests();
        //Stores pickup and delivery addresses
        for(Request request: requests){
            Node pickupAddress = findNodeGraph(request.getPickupAddress().getId());
            Node deliveryAddress = findNodeGraph(request.getDeliveryAddress().getId());
            pickupAddress.setTypeOfNode(Type_Request.PICK_UP);
            deliveryAddress.setTypeOfNode(Type_Request.DELIVERY);
            this.pointsInterest.add(pickupAddress);
            this.pointsInterest.add(deliveryAddress);
            //Adds couple to structure
            Dijkstra.pickUpDeliveryCouples.put(pickupAddress, deliveryAddress);
        }
    }

    /**
     * Dijkstra algorithm.
     * Computes shortest path to every point of interest
     * points of interests distance are set to minimum distance to source
     * @param graph Dijkstra with data loaded and initialized (nodes with distance set to infinity)
     * @param source_Id node from which we want to calculate shortest path
     * @return Dijkstra instance with variables containing proper data (i.e, distances) starting from source node
     */
    public Dijkstra calculateShortestPathFromSource(Dijkstra graph, long source_Id) {
        Node source = findNodeGraph(source_Id);
        source.setDistance(0);
        parentNode.put(source,source);
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        unvisitedNodes.add(source);
        //Continues until all nodes are or all points of interest are visited
        while (!isConditionFulfilled(unvisitedNodes, visitedNodes)) {
            //Gets the node with minimal distance on the graph
            Node currentClosestNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentClosestNode);
            //For each of its successors : 
            for (Map.Entry< Node, Double> adjacencyNode : currentClosestNode.getAdjacentNodes().entrySet()) {
                if(adjacencyNode != null) {
                    Double edgeWeight = adjacencyNode.getValue();
                    if (!visitedNodes.contains(adjacencyNode.getKey())) {
                        //Computes distance of adjacentNode assuming currentClosestNode is its parent
                        //If distance computed is less than the one adjacentNode had, its parentNode and distance are updated
                        calculateMinimumDistance(adjacencyNode.getKey(), edgeWeight, currentClosestNode);
                        //Adds adjacencyNodes to unvisitedNode step by step in order to avoid overloading memory, and for better performance
                        unvisitedNodes.add(adjacencyNode.getKey());
                    }
                }
            }
            visitedNodes.add(currentClosestNode);
        }
        return graph;
    }

    /**
     * Checks conditions to stop dijkstra algorithm.
     * Checks if all nodes are visited (black) or if all points of interest are visited (black)
     * @param unvisitedNodes set of unvisited nodes
     * @param visitedNodes set of visited nodes
     * @return true if at least one of the above conditions is true
     */
    private boolean isConditionFulfilled(Set<Node> unvisitedNodes, Set<Node> visitedNodes) {
        boolean isFulfilled = false;
        if(unvisitedNodes.size()==0 || areAllPointsOfInterestVisited(visitedNodes))
            isFulfilled=  true;
        return isFulfilled;
    }

    /**
     * Checks if the set of visited nodes contains all points of interest
     * @param nodesVisited set of visited nodes
     * @return true if nodesVisited contains all points of interest
     */
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
     * Fetches node in unvisitedNodes set having lowest distance
     * @param unvisitedNodes set of unvisited nodes
     * @return node with the lowest distance among unvisited nodes
     */
    private Node getLowestDistanceNode(Set <Node> unvisitedNodes) {
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
     * Compares the distance of evaluationNode with a new one computed assuming sourceNode is its parent
     * @param evaluationNode current node in dijkstra algorithm step
     * @param edgeWeigh distance of the edge
     * @param sourceNode source node
     */
    private void calculateMinimumDistance(Node evaluationNode, Double edgeWeigh, Node sourceNode) {
        Double sourceDistance = sourceNode.getDistance();
        
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            parentNode.put(evaluationNode, sourceNode);
        }
    }

    /**
     * Finds shortest path between two nodes
     * @param source source node
     * @param destination destination node
     * @return list of nodes constituting the shortest path between source and destination
     */
    public LinkedList<Node> getShortestPath(Node source, Node destination) {
        LinkedList<Node> shortestPath = new LinkedList<>();
        shortestPath.add(destination);
        Node currentNode = destination;
        Node parent;
        while(!currentNode.equals(source)) {
            parent = parentNode.get(currentNode);
            shortestPath.add(parent);
            currentNode = parent;
        }
        Collections.reverse(shortestPath);
        
        return shortestPath;
    }

    /**
     * Finds points of interest given an id
     * @param id node's id
     * @return point of interest as a Node or null if it doesn't exist
     */
    public Node findNodeInterest(long id){
        for(Node node: this.pointsInterest){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }

    /**
     * Finds node in graph given an id
     * @param id node's id
     * @return node or null if it is not in the graph
     */
    public Node findNodeGraph(long id){
        for(Node node: this.graphPlan){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }

    public void addRequest(List<Node> addedNodes){
        this.pointsInterest.addAll(addedNodes);
        for(Node node: this.pointsInterest){
            this.calculateShortestPathFromSource(this, node.getId());
        }
    }

    public Set<Node> getGraphPlan() {
        return graphPlan;
    }

    public Map<Node,Node> getParentNodes() {
        return parentNode;
    }

    public Set<Node> getPointsInterest() {
        return pointsInterest;
    }

    public static Map<Node, Node> getPickUpDeliveryCouples() {
        return pickUpDeliveryCouples;
    }
}
