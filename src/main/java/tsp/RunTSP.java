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

        System.out.println("Points of interest : " + Dijkstra.getPointsInterest());
        Set<Node> pointsOfInterest = Dijkstra.getPointsInterest();

        Map<Node, Set<Node>> shortestPaths = new HashMap<>();
        Map<Node,Node> pickupDeliveryCouples = Dijkstra.getPickUpDeliveryCouples();
        //System.out.println("Couples " + pickupDeliveryCouples);
        for (Node pointOfInterest : pointsOfInterest) {
            //System.out.println("Dijkstra for id " + pointOfInterest.getId());
            Dijkstra algoPoint_i = new Dijkstra(Plan.plan, tour);
            algoPoint_i = algoPoint_i.calculateShortestPathFromSource(algoPoint_i, pointOfInterest);
            Set<Node> results = algoPoint_i.getPointsOfInterestDistanceFromGraph(algoPoint_i);
            /*for (Node result : results) {
                System.out.println("id : " + result.getId() + ", distance : " + result.getDistance());
            }*/
            shortestPaths.put(pointOfInterest, results);
        }
        System.out.println(shortestPaths);


        int nbVertices = pointsOfInterest.size();
        System.out.println("Graphs with " + nbVertices + " vertices:");
        
        Graph g = new CompleteGraph(nbVertices, shortestPaths);
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, g);
        System.out.print("Solution of cost " + tsp.getSolutionCost() + " found in "
                + (System.currentTimeMillis() - startTime) + "ms : ");
        
        LinkedList<Node> shortestPath = new LinkedList<>();
        shortestPath.add(Dijkstra.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(0))));
        System.out.print("0 ");
        for (int i = 1; i < nbVertices; i++) {
            System.out.print(tsp.getSolution(i) + " ");
            Node currentNode = Dijkstra.findNodeInterest(g.findIdNodeByIndex(tsp.getSolution(i)));
            Set<Node> shortestPathOfNode = shortestPaths.get(Dijkstra.findNodeInterest(currentNode.getId()));
            System.out.println(currentNode.getId());
            for(Node node: shortestPathOfNode)
                shortestPath.add(node);
        }
        System.out.println("chemin le plus court : " + shortestPath);

        System.out.println("0");
    }

}
