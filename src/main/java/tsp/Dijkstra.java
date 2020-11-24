package tsp;

import model.*;

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

    public static Dijkstra calculateShortestPathFromSource(Dijkstra graph, Node source) {
        source.setDistance(0);
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        unvisitedNodes.add(source);
        while (unvisitedNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);
            for (Map.Entry< Node, Double> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();
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
     * compare la distance réelle à celle nouvellement calculée tout en suivant le chemin récemment exploré
     * @param evaluationNode
     * @param edgeWeigh
     * @param sourceNode
     */
    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Double edgeWeigh, Node sourceNode) {
        Double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
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

    public Set<Node> getNodes() {
        return nodes;
    }

    //TODO
    @Override
    public int getNbVertices() {
        return 0;
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
