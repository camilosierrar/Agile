package tsp;

import dijkstra.Dijkstra;
import dijkstra.Node;
import model.Plan;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RunTSP {
    public static void main(String[] args) {
        TSP tsp = new TSP1();
        
        Plan.plan = XMLmap.readData("");
        Tour tour = XMLrequest.readData("");

        Dijkstra init_points = new Dijkstra(Plan.plan, tour);

        Map<Node, Set<Node>> shortestPaths = new HashMap<>();
        Map<Node,Dijkstra> dijkstras = new HashMap<>();

        //For each point of interes, it executes Dijkstra and store result in data structure
        for (Node pointOfInterest : init_points.getPointsInterest()) {
            Dijkstra algoPoint_i = new Dijkstra(Plan.plan, tour);
            algoPoint_i = algoPoint_i.calculateShortestPathFromSource(algoPoint_i, pointOfInterest);
            Set<Node> results = algoPoint_i.getPointsOfInterestDistanceFromGraph(algoPoint_i);

            dijkstras.put(pointOfInterest, algoPoint_i);
            shortestPaths.put(pointOfInterest, results);
        }

        int nbVertices = init_points.getPointsInterest().size();
        System.out.println("Graphs with " + nbVertices + " vertices:");
        
        Graph g = new CompleteGraph(nbVertices, shortestPaths);
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, g);
        System.out.print("Solution of cost " + tsp.getSolutionCost() + " found in "
                + (System.currentTimeMillis() - startTime) + "ms : ");
        
        LinkedList<Node> shortestPath = new LinkedList<>();


        for (int i = 0; i < nbVertices; i++) {
            //Current Node is the node i of the shortest path in tsp
            Node currentNode = g.findNodeById(g.findIdNodeByIndex(tsp.getSolution(i)));
            Dijkstra graph = dijkstras.entrySet().stream().filter(elem -> elem.getKey().getId() == currentNode.getId()).findFirst().orElse(null).getValue();
            Node source = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(i)));
            Node destination = null;
            if(i == nbVertices-1)
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(0)));
            else
                destination = graph.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(i+1)));
            shortestPath.addAll(graph.getShortestPath(source ,destination));
            if(i!=nbVertices-1)
                shortestPath.removeLast();
        }
        System.out.println("\nChemin le plus court : " + shortestPath);

        System.out.println("0");
    }

}
