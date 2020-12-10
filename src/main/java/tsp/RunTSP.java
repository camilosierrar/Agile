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
        addRequest(request, false);
        printSolutionInformations();

        LinkedList<Long> testList = new LinkedList<>();
        testList.add((long) 342873658);
        testList.add((long) 26086127);
        testList.add((long) 208769039);
        testList.add((long) 25173820);
        testList.add((long) 26086128);
        testList.add((long) 342873658);

        modifyOrderOfTour(testList);
        printSolutionInformations();

        removeRequest(request, false);
        printSolutionInformations();

        //END TEST
    }

    /**
     * 
     * @return
     */
    public static List<Segment> runTSP() {
        loadData();
        computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.shortestPaths);
        List<Segment> segmentsSolution = getSolution();
        return segmentsSolution;
    }

    /**
     * 
     */
    public static void loadData(){
        //Variable.cityPlan = XMLmap.readData("");
        //Variable.tour = XMLrequest.readData("");
        Variable.pickUpDeliveryCouplesId.clear();
        for(Request request: Variable.tour.getRequests())
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        fillGraph();
    }

    /**
     * 
     */
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

    /**
     * 
     * @param pointInterestId
     */
    public static void doDijkstra(long pointInterestId){
        Dijkstra algoPointI = new Dijkstra();
        algoPointI = algoPointI.calculateShortestPathFromSource(algoPointI, pointInterestId);
        Set<Node> results = algoPointI.getPointsInterest();
        Variable.dijkstras.put(algoPointI.findNodeGraph(pointInterestId), algoPointI);
        Variable.shortestPaths.put(algoPointI.findNodeGraph(pointInterestId), results);
    }

    /**
     * 
     */
    public static void addRequest(Request request, Boolean recalculatePath){
        long pickupId = request.getPickupAddress().getId();
        long deliveryId = request.getDeliveryAddress().getId();
        //Update modifications to Variable
        Variable.findNodeInFirstGraph(pickupId).setTypeOfNode(Type_Request.PICK_UP);
        Variable.findNodeInFirstGraph(deliveryId).setTypeOfNode(Type_Request.DELIVERY);
        Variable.pickUpDeliveryCouplesId.put(pickupId, deliveryId);
        Variable.pointsInterestId.add(pickupId);
        Variable.pointsInterestId.add(deliveryId);
        //Updates old Dijkstras
        for(Map.Entry<Node, Dijkstra> entry: Variable.dijkstras.entrySet())
            entry.getValue().addRequest(pickupId, deliveryId, entry.getKey().getId());
        
        //Executes dijkstra for added request
        doDijkstra(pickupId);
        doDijkstra(deliveryId);

        //Update complete graph
        Node pickup = null;
        Node delivery = null;
        for(Map.Entry<Node, Dijkstra> entry : Variable.dijkstras.entrySet())
            if(entry.getKey().getId() == pickupId)
                pickup = entry.getKey();
            else if(entry.getKey().getId() == deliveryId)
                delivery = entry.getKey();
            
        
        List<Node> addedNodes = Arrays.asList(pickup,delivery);
        Variable.g.addRequest(addedNodes);

        if(recalculatePath)
            getSolution();
        else {
            Long idLast = Variable.sPathOfPointsInterests.pollLast();
            Variable.sPathOfPointsInterests.add(pickupId);
            Variable.sPathOfPointsInterests.add(deliveryId);
            Variable.sPathOfPointsInterests.add(idLast);
            computeFullShortestPath();
        }
    }

    /**
     * 
     * @param request
     * @param recalculatePath
     */
    public static void removeRequest(Request request, Boolean recalculatePath) {
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
            Variable.sPathOfPointsInterests.remove(pickupId);
            Variable.sPathOfPointsInterests.remove(deliveryId);
            for (Map.Entry<Node, Dijkstra> entry : Variable.dijkstras.entrySet()) 
                entry.getValue().removeRequest(pickupId, deliveryId, entry.getKey().getId());
            
            List<Node> nodesToRemove = Arrays.asList(pickupNode, deliveryNode);
            Variable.g.removeRequest(nodesToRemove);

            Variable.shortestPath.clear();
            if(recalculatePath)
                getSolution();
            else 
                computeFullShortestPath();
        }
    }

    /**
     * 
     * @param newPath
     * @return
     */
    public static List<Segment> modifyOrderOfTour(LinkedList<Long> newPath) {
        Variable.sPathOfPointsInterests = newPath;
        computeFullShortestPath();
        return getSegmentsSolution();
    }

    /**
     * 
     * @return
     */
    public static List<Segment> getSolution() {
        
        Variable.tsp = new TSPEnhanced();
        long startTime = System.currentTimeMillis();
        Variable.tsp.searchSolution(Config.TIME_LIMIT, Variable.g);
        System.out.print("Solution found in " + (System.currentTimeMillis() - startTime)+"ms. \n");
        
        //Stores index and id given by tsp (solution for optimal tour)
        List<Integer> indexSolution = new LinkedList<>();
        //Store the id of the Nodes solution
        Variable.sPathOfPointsInterests.clear();

        for (int i = 0; i < Variable.pointsInterestId.size(); i++) {
            int indexTsp = Variable.tsp.getSolution(i);
            long idIndexTSP = Variable.g.findIdNodeByIndex(indexTsp);
            //adds solution
            indexSolution.add(indexTsp);
            Variable.sPathOfPointsInterests.add(idIndexTSP);            
        }
        Variable.sPathOfPointsInterests.add(Variable.g.findIdNodeByIndex(Variable.tsp.getSolution(0)));
        computeFullShortestPath();

        printGraphInformation(Variable.shortestPath,indexSolution);
        return getSegmentsSolution();
    }

    public static void computeFullShortestPath() {
        Variable.shortestPath.clear();
        for(int i = 0; i<Variable.sPathOfPointsInterests.size() -1 ; i++) {
            Long curId = Variable.sPathOfPointsInterests.get(i);
            Long nextId = Variable.sPathOfPointsInterests.get(i+1);
            //Finds corresponding dijkstra graph
            Dijkstra graph = Objects.requireNonNull(Variable.dijkstras.entrySet().stream()
                    .filter(elem -> elem.getKey().getId() == curId).findFirst().orElse(null)).getValue();
            Node source = graph.findNodeInterest(curId);
            Node destination = graph.findNodeInterest(nextId);
            LinkedList<Node> sp = graph.getShortestPath(source, destination);
            List<Long> spId = new LinkedList<>();
            for (Node node : sp) 
                spId.add(node.getId());
            
            Variable.shortestPath.addAll(spId);
            if (i != Variable.pointsInterestId.size() - 1) 
                Variable.shortestPath.removeLast();
        }
    }

    /**
     * 
     * @return
     */
    public static List<Segment> getSegmentsSolution() {
        List<Segment> solution = new LinkedList<>();
        List<Segment> segments = Variable.cityPlan.getSegments();
        Map<Long, Intersection> intersections = Variable.cityPlan.getIntersections();
        for (int i = 0; i < Variable.shortestPath.size(); ++i) {
            long indexStart = Variable.shortestPath.get(i);
            long indexEnd;
            if (i != Variable.shortestPath.size() - 1) {
                indexEnd = Variable.shortestPath.get(i + 1);
            } else {
                indexEnd = Variable.shortestPath.get(0);
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
        return solution;
    }

    private static double getDistanceOfShortestPath() {
        double fullDistance = 0.0;
        List<Segment> segmentsSolution = getSegmentsSolution();
        for (Segment segment : segmentsSolution) {
            fullDistance += segment.getLength();
        }
        return fullDistance;
    }

    /**
     * From a Plan and a Tour object, retrieves and stores all informations as nodes
     * Fills graphPlan, pickup/delivery couples and points of interest
     */
    private static void fillGraph(){
        Variable.graph.clear();
        Variable.pointsInterestId.clear();
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

    public static void printSolutionInformations() {
        System.out.println();
        System.out.println("---------SOLUTION---------");
        for (Long id : Variable.sPathOfPointsInterests)
            System.out.println("\tID : " +  id );
        System.out.println("Total distance : " + getDistanceOfShortestPath() + " meters");
        System.out.println("---------END----------");
    }

    public static void printGraphInformation(LinkedList<Long> solutionNodes, List<Integer> indexSolution) {
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
            System.out.println("Index : " + indexSolution.get(i) + "\t\tID : " +  Variable.sPathOfPointsInterests.get(i) );
                            /*+ "\t\t Type : " + cur.getTypeOfNode() + "\t\t Couple : " + ((nodePair!=null)?nodePair.getId():"null")
                            + " : "  + ((nodePair!=null)?nodePair.getTypeOfNode():""));*/
        }
        //index solution doesn't contain departure address twice
        System.out.println("Index : " + indexSolution.get(0) + "\t\tID : " +  Variable.sPathOfPointsInterests.get(0));
        System.out.println("Total distance : " + getDistanceOfShortestPath() + " meters");
        /*System.out.println("\n\n SOLUTION IN DETAILS");
        for (Node node : solutionNodes) {
            System.out.println("ID : " + node.getId() + "\t\tDISTANCE : " + node.getDistance());
        }*/
        System.out.println("----------END----------");
        System.out.println();
        System.out.println();
    }
}
