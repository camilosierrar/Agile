package main;

import model.Plan;
import model.Tour;
import tsp.Dijkstra;
import tsp.Node;
import xml.XMLmap;
import xml.XMLrequest;

public class main {
    public static void main (String[] args){
        System.out.println("Bye world");
        Plan.plan = XMLmap.readData();
        Tour tour = XMLrequest.readData();
        Dijkstra algo = new Dijkstra(Plan.plan, tour);
        Node source = new Node(tour.getAddressDeparture().getId());
        algo.calculateShortestPathFromSource(algo, source);
        //Gui gui = new Gui();
    }
}

