package tsp;

import config.Config;
import config.Variable;
import dijkstra.Dijkstra;
import dijkstra.Node;
import model.*;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.*;

public class RunTSP {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        loadData();
        long endTime = System.currentTimeMillis();
        computeDijkstra();
        System.out.println("ça a mis : " + (endTime-startTime) + " ms");
        List<Segment> segmentsSolution = getSolution();
        for(Segment segment: segmentsSolution) {
            System.out.println(segment.getOrigin().getId() + "\t" + segment.getDestination().getId() + "\t" + segment.getName());
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
    }


    public static void loadData(){
        Variable.cityPlan = XMLmap.readData("");
        Variable.tour = XMLrequest.readData("");
        for(Request request: Variable.tour.getRequests())
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
    }

    public static void computeDijkstra(){
        //Obtains all points of interest + departure address
        Variable.pointsInterestId.add(Variable.tour.getAddressDeparture().getId());
        for(Request request: Variable.tour.getRequests()){
            Variable.pointsInterestId.add(request.getPickupAddress().getId());
            Variable.pointsInterestId.add(request.getDeliveryAddress().getId());
        }
        //Executes dijkstra
        for(long pointInterestId: Variable.pointsInterestId){
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
        Variable.pickUpDeliveryCouplesId.put(pickupId, deliveryId);
        Variable.pointsInterestId.add(pickupId);
        Variable.pointsInterestId.add(deliveryId);
        //Updates old Dijkstras
        for(Map.Entry<Node, Dijkstra> entry: Variable.dijkstras.entrySet()){
            entry.getValue().addRequest(pickupId, deliveryId,entry.getKey().getId());
        }
        //Executes dijkstra for added request
        doDijkstra(pickupId);
        doDijkstra(deliveryId);
    }


    public static List<Segment> getSolution() {
        //Initializes complete graph and launch TSP algo
        int nbVertices = Variable.pointsInterestId.size();
        Graph g = new CompleteGraph(nbVertices, Variable.shortestPaths);
        //TEST ADD REQUEST
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26079654),
                Variable.cityPlan.getIntersectionById(33066313),
                20,
                40);
        addRequest(request);
        long pickupId = request.getPickupAddress().getId();
        long deliveryId = request.getDeliveryAddress().getId();
        Node pickup = null;
        Node delivery = null;
        Map<Node,Set<Node>> addedShortestPath = new HashMap<>();
        for(Map.Entry<Node,Set<Node>> entry: Variable.shortestPaths.entrySet()){
            if(entry.getKey().getId() == pickupId){
                addedShortestPath.put(entry.getKey(), entry.getValue());
                pickup = entry.getKey();
            }if(entry.getKey().getId() == deliveryId){
                addedShortestPath.put(entry.getKey(), entry.getValue());
                delivery = entry.getKey();
            }
        }
        List<Node> addedNodes = Arrays.asList(pickup,delivery);
        g.addRequest(Variable.shortestPaths, addedNodes);
        g.prettyPrint();
        //END TEST
        TSP tsp = new TSPEnhanced();
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(Config.TIME_LIMIT, g);
        System.out.print("Solution found in " + (System.currentTimeMillis() - startTime)+"ms : ");
        //Stores all nodes to traverse (from departure to departure) to obtain optimal tour (minimum distance)
        LinkedList<Node> shortestPath = new LinkedList<>();
        //Stores index and id given by tsp (solution for optimal tour)
        List<Integer> indexSolution = new LinkedList<>();
        //Store the id of the Nodes solution
        List<Long> idSolution = new LinkedList<>();
        //Each shortest path start from 0, hence we must add last distance value of previous index shortestPath
        double previousDistance = 0;
        for (int i = 0; i < nbVertices; i++) {
            int indexTsp = tsp.getSolution(i);
            long idIndexTSP = g.findIdNodeByIndex(indexTsp);
            //adds solution
            indexSolution.add(indexTsp);
            idSolution.add(idIndexTSP);
            Node source = g.findNodeById(idIndexTSP);
            //Finds corresponding dijkstra graph
            Dijkstra graph = Objects.requireNonNull(Variable.dijkstras.entrySet().stream()
                    .filter(elem -> elem.getKey().getId() == source.getId()).findFirst().orElse(null)).getValue();
            Node destination;
            //Destination is following index in tsp solution or departure address if we're on last index of tsp solution
            if (i == (nbVertices - 1))
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(0)));
            else
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(i + 1)));
            Node sourceDijkstra = graph.findNodeInterest(source.getId());

            LinkedList<Node> sp = graph.getShortestPath(sourceDijkstra, destination);
            for (Node node : sp) {
                Node temp = node;
                temp.setDistance(node.getDistance() + previousDistance);
                sp.set(sp.indexOf(node), temp);
            }
            shortestPath.addAll(sp);
            previousDistance = shortestPath.getLast().getDistance();
            if (i != nbVertices - 1) {
                shortestPath.removeLast();
            }
        }

        List<Segment> solution = new LinkedList<>();
        List<Segment> segments = Variable.cityPlan.getSegments();
        Map<Long, Intersection> intersections = Variable.cityPlan.getIntersections();
        for (int i = 0; i < shortestPath.size(); ++i) {
            long indexStart;
            long indexEnd;
            indexStart = shortestPath.get(i).getId();
            if (i != shortestPath.size() - 1) {
                indexEnd = shortestPath.get(i + 1).getId();
            } else {
                indexEnd = shortestPath.get(0).getId();
            }
            Intersection start = intersections.get(indexStart);
            Intersection end = intersections.get(indexEnd);
            for (Segment segment : segments) {
                if (segment.getOrigin().getId() == start.getId() && segment.getDestination().getId() == end.getId()) {
                    solution.add(segment);
                }
            }
        }
        printGraphInformation(shortestPath,indexSolution, idSolution);
        return solution;
    }
}
