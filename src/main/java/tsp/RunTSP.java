package tsp;

import dijkstra.Dijkstra;
import dijkstra.Node;
import model.Plan;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RunTSP {
    public static void main(String[] args) {
        TSP tsp = new TSP1();
        Plan.plan = XMLmap.readData("");
        Tour tour = XMLrequest.readData("");
        Dijkstra algo = new Dijkstra(Plan.plan, tour);
        System.out.println("Points of interest : " + algo.getPointsInterest());
        Set<Node> pointsOfInterest = algo.getPointsInterest();
        Map<Node, Set<Node>> shortestPaths = new HashMap<>();
        for (Node pointOfInterest : pointsOfInterest) {
            System.out.println("Dijkstra for id " + pointOfInterest.getId());
            Dijkstra algoTest = new Dijkstra(Plan.plan, tour);
            algoTest = algoTest.calculateShortestPathFromSource(algoTest, pointOfInterest);
            Set<Node> results = algoTest.getPointsOfInterestDistanceFromGraph(algoTest);
            for (Node result : results) {
                System.out.println("id : " + result.getId() + ", distance : " + result.getDistance());
            }
            shortestPaths.put(pointOfInterest, results);
        }

        //
        int nbVertices = pointsOfInterest.size();
        System.out.println("Graphs with " + nbVertices + " vertices:");
        Graph g = new CompleteGraph(nbVertices, shortestPaths, pointsOfInterest);
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, g);
        System.out.print("Solution of cost " + tsp.getSolutionCost() + " found in "
                + (System.currentTimeMillis() - startTime) + "ms : ");
        for (int i = 0; i < nbVertices; i++)
            System.out.print(tsp.getSolution(i) + " ");
        System.out.println("0");
    }

}
