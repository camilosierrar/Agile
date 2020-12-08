package tsp;

import config.Config;
import config.Variable;
import config.Config.Type_Request;
import dijkstra.Dijkstra;
import model.*;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.*;

public class RunTSP {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        loadData();
        computeDijkstra();
        long endTime = System.currentTimeMillis();
        System.out.println("Temps de calcul pour les dijkstra : " + (endTime-startTime) + " ms");
        //Initializes complete graph and launch TSP algo
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.shortestPaths);
        getSolution();
        //TEST ADD REQUEST
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26086127),
                Variable.cityPlan.getIntersectionById(26086128),
                20,
                40);
        addRequest(request);
        //END TEST
    }

    public static List<Segment> runTSP() {
        loadData();
        computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.shortestPaths);
        List<Segment> segmentsSolution = getSolution();
        return segmentsSolution;
    }

    public static void loadData(){
        Variable.cityPlan = XMLmap.readData("");
        Variable.tour = XMLrequest.readData("");
        for(Request request: Variable.tour.getRequests())
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        fillGraph();
    }

    public static void computeDijkstra(){
        //Obtains all points of interest + departure address
        Variable.pointsInterestId.add(Variable.tour.getAddressDeparture().getId());
        for(Request request: Variable.tour.getRequests()){
            Variable.pointsInterestId.add(request.getPickupAddress().getId());
            Variable.pointsInterestId.add(request.getDeliveryAddress().getId());
        }
        //Executes dijkstra
        for(Long pointInterestId: Variable.pointsInterestId){
            doDijkstra(pointInterestId);
        }
    }

    public static void doDijkstra(long pointInterestId){
        Dijkstra algoPointI = new Dijkstra();
        algoPointI = algoPointI.calculateShortestPathFromSource(algoPointI, pointInterestId);
        Set<Node> results = algoPointI.getPointsInterest();
        Variable.dijkstras.put(algoPointI.findNodeGraph(pointInterestId), algoPointI);
        Variable.shortestPaths.put(algoPointI.findNodeGraph(pointInterestId), results);
    }

    public static void addRequest(Request request){
        long pickupId = request.getPickupAddress().getId();
        long deliveryId = request.getDeliveryAddress().getId();
        //Update modifications to Variable
        Variable.findNodeInFirstGraph(pickupId).setTypeOfNode(Type_Request.PICK_UP);
        Variable.findNodeInFirstGraph(deliveryId).setTypeOfNode(Type_Request.DELIVERY);
        Variable.pickUpDeliveryCouplesId.put(pickupId, deliveryId);
        Variable.pointsInterestId.add(pickupId);
        Variable.pointsInterestId.add(deliveryId);
        //Updates old Dijkstras
        for(Map.Entry<Node, Dijkstra> entry: Variable.dijkstras.entrySet()){
            entry.getValue().addRequest(pickupId, deliveryId, entry.getKey().getId());
        }
        //Executes dijkstra for added request
        doDijkstra(pickupId);
        doDijkstra(deliveryId);
        //Update complete graph and launch TSP algo
        Node pickup = null;
        Node delivery = null;

        for(Map.Entry<Node,Set<Node>> entry: Variable.shortestPaths.entrySet()){
            if(entry.getKey().getId() == pickupId)
                pickup = entry.getKey();
            if(entry.getKey().getId() == deliveryId)
                delivery = entry.getKey();
        }
        List<Node> addedNodes = Arrays.asList(pickup,delivery);
        Variable.g.addRequest(addedNodes);
        Variable.g.prettyPrint();
        getSolution();
    }

    public static void removeRequest(Request request) {
        long pickupId = request.getPickupAddress().getId();
        long deliveryId = request.getDeliveryAddress().getId();

        if(Variable.pointsInterestId.contains(pickupId) && Variable.pointsInterestId.contains(deliveryId)) {
            //System.out.println("----------STARTING REMOVING NODES ----------");
            Node pickupNode = Variable.g.findNodeById(pickupId);
            Node deliveryNode = Variable.g.findNodeById(deliveryId);
    
            Variable.pickUpDeliveryCouplesId.remove(pickupId, deliveryId);
            Variable.pointsInterestId.remove(pickupId);
            Variable.pointsInterestId.remove(deliveryId);
            Variable.dijkstras.remove(pickupNode);
            Variable.dijkstras.remove(deliveryNode);
            Variable.shortestPaths.remove(pickupNode);
            Variable.shortestPaths.remove(deliveryNode);

            for (Map.Entry<Node, Dijkstra> entry : Variable.dijkstras.entrySet()) 
                entry.getValue().removeRequest(pickupId, deliveryId, entry.getKey().getId());
            
            /*System.out.println("Couple pickup/delivery : " +Variable.pickUpDeliveryCouplesId + "\n\n");
            System.out.println("Points d'intérêts : " +Variable.pointsInterestId + "\n\n");
            System.out.println("Dijkstras : " +Variable.dijkstras + "\n\n");
            System.out.println("+ Courts chemins : \n");
            for (Map.Entry<Node, Set<Node>> entry : Variable.shortestPaths.entrySet()) 
                System.out.println("--> " +entry.getKey() + "\n");*/
            
            List<Node> nodesToRemove = Arrays.asList(pickupNode, deliveryNode);
            Variable.g.removeRequest(nodesToRemove);
            getSolution();
        }
    }

    public static List<Segment> modifyOrderOfTour(LinkedList<Long> newPath) {
        double previousDistance = 0;
        Variable.shortestPath.clear();

        for (int i = 0; i < newPath.size(); i++) {
            Node currentSource = Variable.g.findNodeById(newPath.get(i));
            Dijkstra graph = Objects.requireNonNull(Variable.dijkstras.entrySet().stream()
                    .filter(elem -> elem.getKey().getId() == currentSource.getId()).findFirst().orElse(null)).getValue();
            Node destination = graph.findNodeInterest((newPath.get(i + 1)));
            Node sourceDijkstra = graph.findNodeInterest(currentSource.getId());
            LinkedList<Node> sp = graph.getShortestPath(sourceDijkstra, destination);
            for (Node node : sp) {
                Node temp = new Node(node.getId());
                temp.setTypeOfNode(node.getTypeOfNode());
                temp.setDistance(node.getDistance() + previousDistance);
                sp.set(sp.indexOf(node), temp);
            }
            Variable.shortestPath.addAll(sp);
            previousDistance = Variable.shortestPath.getLast().getDistance();
            if (i != newPath.size() - 1) {
                Variable.shortestPath.removeLast();
            }
        }

        List<Segment> solution = new LinkedList<>();
        List<Segment> segments = Variable.cityPlan.getSegments();
        Map<Long, Intersection> intersections = Variable.cityPlan.getIntersections();
        for (int i = 0; i < Variable.shortestPath.size(); ++i) {
            long indexStart = Variable.shortestPath.get(i).getId();
            long indexEnd;
            if (i != Variable.shortestPath.size() - 1) 
                indexEnd = Variable.shortestPath.get(i + 1).getId();
             else 
                indexEnd = Variable.shortestPath.get(0).getId();
            
            Intersection start = intersections.get(indexStart);
            Intersection end = intersections.get(indexEnd);
            for (Segment segment : segments) {
                if (segment.getOrigin().getId() == start.getId() && segment.getDestination().getId() == end.getId()) {
                    solution.add(segment);
                    break;
                }
            }
        }

        return solution;
    }

    public static List<Segment> getSolution() {
        
        TSP tsp = new TSPEnhanced();
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(Config.TIME_LIMIT, Variable.g);
        System.out.print("Solution found in " + (System.currentTimeMillis() - startTime)+"ms. \n");
        
        Variable.shortestPath.clear();
        //Stores index and id given by tsp (solution for optimal tour)
        List<Integer> indexSolution = new LinkedList<>();
        //Store the id of the Nodes solution
        List<Long> idSolution = new LinkedList<>();
        //Each shortest path start from 0, hence we must add last distance value of previous index shortestPath
        double previousDistance = 0;

        for (int i = 0; i < Variable.pointsInterestId.size(); i++) {
            int indexTsp = tsp.getSolution(i);
            long idIndexTSP = Variable.g.findIdNodeByIndex(indexTsp);
            //adds solution
            indexSolution.add(indexTsp);
            idSolution.add(idIndexTSP);
            Node currentSource = Variable.g.findNodeById(idIndexTSP);
            //Finds corresponding dijkstra graph
            Dijkstra graph = Objects.requireNonNull(Variable.dijkstras.entrySet().stream()
                    .filter(elem -> elem.getKey().getId() == currentSource.getId()).findFirst().orElse(null)).getValue();
            Node destination;
            //Destination is following index in tsp solution or departure address if we're on last index of tsp solution
            if (i == (Variable.pointsInterestId.size() - 1))
                destination = graph.findNodeInterest(Variable.g.findIdNodeByIndex(tsp.getSolution(0)));
            else
                destination = graph.findNodeInterest(Variable.g.findIdNodeByIndex(tsp.getSolution(i + 1)));
            Node sourceDijkstra = graph.findNodeInterest(currentSource.getId());

            LinkedList<Node> sp = graph.getShortestPath(sourceDijkstra, destination);
            for (Node node : sp) {
                Node temp = new Node(node.getId());
                temp.setTypeOfNode(node.getTypeOfNode());
                temp.setDistance(node.getDistance() + previousDistance);
                sp.set(sp.indexOf(node), temp);
            }
            Variable.shortestPath.addAll(sp);
            previousDistance = Variable.shortestPath.getLast().getDistance();
            if (i != Variable.pointsInterestId.size() - 1) {
                Variable.shortestPath.removeLast();
            }
        }

        List<Segment> solution = new LinkedList<>();
        List<Segment> segments = Variable.cityPlan.getSegments();
        Map<Long, Intersection> intersections = Variable.cityPlan.getIntersections();
        for (int i = 0; i < Variable.shortestPath.size(); ++i) {
            long indexStart = Variable.shortestPath.get(i).getId();
            long indexEnd;
            if (i != Variable.shortestPath.size() - 1) {
                indexEnd = Variable.shortestPath.get(i + 1).getId();
            } else {
                indexEnd = Variable.shortestPath.get(0).getId();
            }
            Intersection start = intersections.get(indexStart);
            Intersection end = intersections.get(indexEnd);
            for (Segment segment : segments) {
                if (segment.getOrigin().getId() == start.getId() && segment.getDestination().getId() == end.getId()) {
                    solution.add(segment);
                    break;
                }
            }
        }
        printGraphInformation(Variable.shortestPath,indexSolution, idSolution);
        return solution;
    }

    /**
     * From a Plan and a Tour object, retrieves and stores all informations as nodes
     * Fills graphPlan, pickup/delivery couples and points of interest
     */
    private static void fillGraph(){
        //Stores all intersections in graphPlan
        for(Map.Entry<Long,Intersection> entry: Variable.cityPlan.getIntersections().entrySet()){
            Intersection intersection = entry.getValue();
            Node originNode = new Node(intersection.getId());
            Variable.graph.add(originNode);
        }

        //Set adjacent Nodes for each segments
        for(Map.Entry<Long,Intersection> entry: Variable.cityPlan.getIntersections().entrySet()){
            Intersection intersection = entry.getValue();
            Node originNode = Variable.findNodeInFirstGraph(intersection.getId());
            //Iterates over all segment from Plan and adds adjacent nodes
            for(Segment segment: Variable.cityPlan.getSegments()){
                Intersection origin = segment.getOrigin();
                if(originNode.getId() == origin.getId()){
                    Intersection dest = segment.getDestination();
                    Node destination = Variable.findNodeInFirstGraph(dest.getId());
                    originNode.addDestination(destination, segment.getLength());
                }
            }
        }
        //Stores all points of interest and pickup/delivery couple
        //Stores departure address
        Node addressDeparture = Variable.findNodeInFirstGraph(Variable.tour.getAddressDeparture().getId());
        addressDeparture.setTypeOfNode(Config.Type_Request.DEPARTURE_ADDRESS);
        Variable.pointsInterestId.add(addressDeparture.getId());
        //Stores pickup and delivery addresses
        for(Map.Entry<Long,Long> entry: Variable.pickUpDeliveryCouplesId.entrySet()){
            Node pickupAddress = Variable.findNodeInFirstGraph(entry.getKey());
            Node deliveryAddress = Variable.findNodeInFirstGraph(entry.getValue());
            pickupAddress.setTypeOfNode(Config.Type_Request.PICK_UP);
            deliveryAddress.setTypeOfNode(Config.Type_Request.DELIVERY);
            Variable.pointsInterestId.add(pickupAddress.getId());
            Variable.pointsInterestId.add(deliveryAddress.getId());
        }
    }

    public static void printGraphInformation(LinkedList<Node> solutionNodes, List<Integer> indexSolution, List<Long> idSolution) {
        System.out.println("SOLUTION");
        for (int i = 0; i < indexSolution.size(); ++i) {
            //To Print the couple
            /*final int index = i;
            Node cur = solutionNodes.stream().filter(node -> node.getId() == idSolution.get(index)).findFirst().orElse(null);
            Map.Entry<Node,Node> pairOfCur = Dijkstra.getPickUpDeliveryCouples().entrySet().stream()
                            .filter(elem -> elem.getKey().getId() == cur.getId() || elem.getValue().getId() == cur.getId()).findFirst().orElse(null);
                            //.map(elem -> {if(elem.getKey().getId() == cur.getId()) return elem.getValue(); else return elem.getKey();}).findFirst().orElse(null);
            Node nodePair = null;
            if(pairOfCur != null) {
                if(pairOfCur.getKey().getId() == cur.getId())
                    nodePair = pairOfCur.getValue();
                else if(pairOfCur.getValue().getId() == cur.getId())
                    nodePair = pairOfCur.getKey();
            }*/
            System.out.println("Index : " + indexSolution.get(i) + "\t\tID : " + idSolution.get(i) );
                            /*+ "\t\t Type : " + cur.getTypeOfNode() + "\t\t Couple : " + ((nodePair!=null)?nodePair.getId():"null")
                            + " : "  + ((nodePair!=null)?nodePair.getTypeOfNode():""));*/
        }
        //index solution doesn't contain departure address twice
        System.out.println("Index : " + indexSolution.get(0) + "\t\tID : " + idSolution.get(0));
        System.out.println("Total distance : " + solutionNodes.getLast().getDistance() + " meters");
        /*System.out.println("\n\n SOLUTION IN DETAILS");
        for (Node node : solutionNodes) {
            System.out.println("ID : " + node.getId() + "\t\tDISTANCE : " + node.getDistance());
        }*/
        System.out.println("----------END----------");
        System.out.println();
        System.out.println();
    }
}
