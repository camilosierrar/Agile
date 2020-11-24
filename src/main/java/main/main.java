package main;

import java.util.Scanner;

import model.Plan;
import model.Tour;
import tsp.Dijkstra;
import tsp.Node;
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
        Node source = algo.findNode(342873658);
        algo = algo.calculateShortestPathFromSource(algo, source);
        //Gui gui = new Gui();
    }
}

