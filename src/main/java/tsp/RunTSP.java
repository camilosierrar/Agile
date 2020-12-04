package tsp;

import dijkstra.Dijkstra;
import dijkstra.Node;
import model.Intersection;
import model.Plan;
import model.Segment;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.*;

public class RunTSP {
    public static void main(String[] args) {

        //Load data
        Plan.plan = XMLmap.readData("fileNamePlan");
        Tour tour = XMLrequest.readData("fileNameRequests");
        List<Segment> segmentsSolution = getSolution(tour);
        /*for(Segment segment: segmentsSolution){
            System.out.println(segment.getOrigin().getId() + "\t" + segment.getDestination().getId() + "\t" + segment.getName());
        }*/
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

    public static List<Segment> getSolution(Tour tour) {
        long startTimeDijkstra = System.currentTimeMillis();
        //Initializes dijkstra
        Dijkstra initPoints = new Dijkstra(Plan.plan, tour);

        //Data structure containing the node source in key and in value the set of Node of interest
        //and their distance in shortest path to the source
        Map<Node, Set<Node>> shortestPaths = new HashMap<>();

        //Data structure containing the graph of shortest path from source Node (in key)
        Map<Node, Dijkstra> dijkstras = new HashMap<>();

        //For each point of interest, it executes Dijkstra and store result in data structure
        for (Node pointOfInterest : initPoints.getPointsInterest()) {
            Dijkstra algoPointI = new Dijkstra(Plan.plan, tour);
            algoPointI.calculateShortestPathFromSource(algoPointI, pointOfInterest.getId());
            Set<Node> results = algoPointI.getPointsInterest();
            dijkstras.put(algoPointI.findNodeGraph(pointOfInterest.getId()), algoPointI);
            shortestPaths.put(algoPointI.findNodeGraph(pointOfInterest.getId()), results);
        }
        System.out.print("Dijkstra found in "
					+(System.currentTimeMillis() - startTimeDijkstra)+"ms \n \n");

        long startTimeCompleteGraph = System.currentTimeMillis();
        //Initializes complete graph and launch TSP algo
        int nbVertices = initPoints.getPointsInterest().size();
        Graph g = new CompleteGraph(nbVertices, shortestPaths);
        System.out.print("Complete graph instanciated in "
                    +(System.currentTimeMillis() - startTimeCompleteGraph)+"ms \n \n");
                    
        TSP tsp = new TSPEnhanced();
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(120000, g);
        System.out.print("Solution found in "
					+(System.currentTimeMillis() - startTime)+"ms \n ");

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
            Dijkstra graph = dijkstras.entrySet().stream().filter(elem -> elem.getKey().getId() == source.getId()).findFirst().orElse(null).getValue();
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
        List<Segment> segments = Plan.plan.getSegments();
        Map<Long, Intersection> intersections = Plan.plan.getIntersections();
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
