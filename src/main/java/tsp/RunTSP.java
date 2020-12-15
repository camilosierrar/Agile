package tsp;

import config.Config;
import config.Variable;
import controller.Controller;
import dijkstra.Dijkstra;
import model.*;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.*;

public class RunTSP {

    //TO USE MAIN, UNCOMMENT LINES 134 & 135
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        loadData();
        computeDijkstra();
        long endTime = System.currentTimeMillis();
        System.out.println("Temps de calcul pour les dijkstra : " + (endTime-startTime) + " ms");

        //Initializes complete graph and launch TSP algo
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        Scanner scan = new Scanner(System.in);
        getSolution();
        printSolutionInformations();
        System.out.println();
        //TEST REQUEST
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26086127),
                Variable.cityPlan.getIntersectionById(26086128),
                20,
                40);
        
        Controller c = new Controller();
        Boolean loop = true;

        while(loop) {
            System.out.println("Que faire ?");
            System.out.println("1 - Add Request");
            System.out.println("2 - Remove Request");
            System.out.println("3 - Modify Order (after adding new request");
            System.out.println("4 - Undo");
            System.out.println("5 - Redo");
            System.out.println("q - quit");
            System.out.println();
            String choice = scan.nextLine();
            switch(choice) {
                case "1": {
                    System.out.println("Which request");
                    System.out.println("1 - New Request");
                    System.out.println("2 - First of tour");
                    String choice2 = scan.nextLine();
                    switch(choice2) {
                        case "1": {
                            c.addRequest(request, true);
                            break;
                        }
                        case "2": {
                            c.addRequest(Variable.tour.getRequests().get(0), true);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case "2": {
                    System.out.println("Which request");
                    System.out.println("1 - New Request");
                    System.out.println("2 - First of tour");
                    String choice2 = scan.nextLine();
                    switch(choice2) {
                        case "1": {
                            c.removeRequest(request, true);
                            break;
                        }
                        case "2": {
                            c.removeRequest(Variable.tour.getRequests().get(0), true);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case "3": {
                    LinkedList<Long> testList = new LinkedList<>();
                    testList.add((long) 342873658);
                    testList.add((long) 26086127);
                    testList.add((long) 208769039);
                    testList.add((long) 25173820);
                    testList.add((long) 26086128);
                    testList.add((long) 342873658);
                    c.modifyOrder(testList);
                    break;
                }
                case "4": {
                    c.undo();
                    break;
                }
                case "5": {
                    c.redo();
                    break;
                }
                case "q":
                    loop = false;
                    break;
            }
            printSolutionInformations();
            System.out.println();
        }
        scan.close();
        //END TEST
    }

    /**
     * 
     * @return List of segment ordered in best tour computed
     */
    public static List<Segment> runTSP() {
        loadData();
        computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        List<Segment> segmentsSolution = getSolution();
        return segmentsSolution;
    }

    /**
     * Loads the map and requests
     */
    public static void loadData(){
        //XMLmap.readData("smallMap.xml");
        //XMLrequest.readData("requestsSmall1.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for(Request request: Variable.tour.getRequests())
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        fillGraph();
    }

    /**
     * Computes Dijkstra for each point of interest and store results in Variable.dijkstras
     */
    public static void computeDijkstra(){
        //Executes dijkstra
        Variable.dijkstras.clear();
        for(Long pointInterestId: Variable.pointsInterestId){
            doDijkstra(pointInterestId);
        }
    }

    /**
     * Executes Dijkstra algorithm from source
     * @param pointInterestId source id
     */
    public static void doDijkstra(long pointInterestId){
        Dijkstra algoPointI = new Dijkstra();
        algoPointI = algoPointI.calculateShortestPathFromSource(algoPointI, pointInterestId);
        Variable.dijkstras.put(algoPointI.findNodeGraph(pointInterestId), algoPointI);
    }

    /**
     * Run TSP algorithm and get best solution
     * @return
     */
    public static List<Segment> getSolution() {
        
        Variable.tsp = new TSPEnhanced();
        long startTime = System.currentTimeMillis();
        Variable.tsp.searchSolution(Config.TIME_LIMIT, Variable.g);
        System.out.print("Solution found in " + (System.currentTimeMillis() - startTime)+"ms. \n");
        Variable.cutAlgo = false;
        
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

        //printGraphInformation(Variable.shortestPath,indexSolution);
        return getSegmentsSolution();
    }

    /**
     * Uses dijkstra parentNode map to get the shortest path between two point
     * of interest and stores full path of id ordered in Variable.shortestPath
     */
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
     * Translates id of intersections to segments from the shortest path computed
     * @return list of segments solutions ordered
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

    /**
     * Computes the distance of the shortest path
     * @return distance
     */
    private static double getDistanceOfShortestPath() {
        double fullDistance = 0.0;
        List<Segment> segmentsSolution = getSegmentsSolution();
        System.out.println("NB Segments : " + segmentsSolution.size());
        for (Segment segment : segmentsSolution) {
            fullDistance += segment.getLength();
        }
        return fullDistance;
    }

    /**
     * From a Plan and a Tour object, it stores all informations as nodes
     * Fills graphPlan, pickup/delivery couples and points of interest (static Variables)
     */
    public static void fillGraph(){
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

    /**
     * Prints informations on the solution
     */
    public static void printSolutionInformations() {
        System.out.println();
        System.out.println("---------SOLUTION---------");
        for (Long id : Variable.sPathOfPointsInterests)
            System.out.println("\tID : " +  id );
        System.out.println("Total distance : " + getDistanceOfShortestPath() + " meters");
        System.out.println("---------END----------");
    }

    /**
     * Prints information on the graph and best path found
     * @param solutionNodes
     * @param indexSolution
     */
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
