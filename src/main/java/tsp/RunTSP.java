package tsp;

import dijkstra.Dijkstra;
import dijkstra.Node;
import model.Plan;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.*;

public class RunTSP {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the Plan");
        String fileNamePlan = scanner.next();
        System.out.println("Choose the file to load the requests");
        String fileNameRequests = scanner.next();
        scanner.close();

        //Load data
        Plan.plan = XMLmap.readData(fileNamePlan);
        Tour tour = XMLrequest.readData(fileNameRequests);

        //Initializes dijkstra
        Dijkstra initPoints = new Dijkstra(Plan.plan, tour);
        
        //Data structure containing the node source in key and in value the set of Node of interest
        //and their distance in shortest path to the source
        Map<Node, Set<Node>> shortestPaths = new HashMap<>();

        //Data structure containing the graph of shortest path from source Node (in key)
        Map<Node,Dijkstra> dijkstras = new HashMap<>();

        //For each point of interest, it executes Dijkstra and store result in data structure
        for (Node pointOfInterest : initPoints.getPointsInterest()) {
            Dijkstra algoPointI = new Dijkstra(Plan.plan, tour);
            algoPointI = algoPointI.calculateShortestPathFromSource(algoPointI, pointOfInterest);
            Set<Node> results = algoPointI.getPointsInterest();
            dijkstras.put(pointOfInterest, algoPointI);
            shortestPaths.put(pointOfInterest, results);
        }

        //Initializes complete graph and launch TSP algo
        int nbVertices = initPoints.getPointsInterest().size();
        Graph g = new CompleteGraph(nbVertices, shortestPaths);
        TSP tsp = new TSP1();
        tsp.searchSolution(20000, g);

        //Stores all nodes to traverse (from departure to departure) to obtain optimal tour (minimum distance)
        LinkedList<Node> shortestPath = new LinkedList<>();

        //Stores index and id given by tsp (solution for optimal tour)
        List<Integer> indexSolution = new LinkedList<>();


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
            //Finds corresponding dijkstra
            Dijkstra graph = dijkstras.entrySet().stream().filter(elem -> elem.getKey().getId() == source.getId()).findFirst().orElse(null).getValue();
            Node destination;
            //Destination is following index in tsp solution or departure address if we're on last index of tsp solution
            if(i == (nbVertices-1))
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(0)));
            else
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(i+1)));
            LinkedList<Node> sp = graph.getShortestPath(source, destination);
            for(Node node: sp){
                Node temp = node;
                temp.setDistance(node.getDistance() + previousDistance);
                sp.set(sp.indexOf(node), temp);
            }
            shortestPath.addAll(sp);
            previousDistance = shortestPath.getLast().getDistance();
            if(i!=nbVertices-1)
                shortestPath.removeLast();
        }
        printGraphInformation(shortestPath,indexSolution,idSolution);
    }

    public static void printGraphInformation(LinkedList<Node> solutionNodes, List<Integer> indexSolution, List<Long> idSolution){
        System.out.println("SOLUTION");
        for(int i = 0; i < indexSolution.size(); ++i){
            System.out.println("Index : " + indexSolution.get(i) + "\t\tID : " + idSolution.get(i));
        }
        //index solution doesn't contain departure address twice
        System.out.println("Index : " + indexSolution.get(0) + "\t\tID : " + idSolution.get(0));
        System.out.println("Total distance : " + solutionNodes.getLast().getDistance() + " meters");
        System.out.println("\n\n SOLUTION IN DETAILS");
        for(Node node: solutionNodes){
            System.out.println("ID : " + node.getId() + "\t\tDISTANCE : " + node.getDistance());
        }
    }
}
