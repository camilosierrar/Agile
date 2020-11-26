package main;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

import model.Plan;
import model.Tour;
import dijkstra.Dijkstra;
import dijkstra.Node;
import xml.XMLmap;
import xml.XMLrequest;

public class main {
    public static void main (String[] args){

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the Plan");
        String fileNamePlan = scanner.next();
        Plan.plan = XMLmap.readData(fileNamePlan);
        System.out.println("Choose the file to load the requests");
        String fileNameRequests = scanner.next();
        Tour tour = XMLrequest.readData(fileNameRequests);
        scanner.close();*/
        Plan.plan = XMLmap.readData("");
        Tour tour = XMLrequest.readData("");

        Dijkstra algo = new Dijkstra(Plan.plan, tour);
        System.out.println("Points of interest : " + algo.getPointsInterest());
        Set<Node> pointsOfInterest = algo.getPointsInterest();
        for(Node pointOfInterest: pointsOfInterest){
            System.out.println("Dijkstra for id " + pointOfInterest.getId());
            algo = algo.calculateShortestPathFromSource(algo, pointOfInterest);
            Set<Node> results = algo.getPointsOfInterestDistanceFromGraph(algo);
            for(Node result: results){
                System.out.println("id : " + result.getId() +", distance : " + result.getDistance());
            }
        }
    }
}

