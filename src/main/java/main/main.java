package main;

<<<<<<< HEAD
import controller.Controller;

public class main {
    public static void main (String[] args){
        System.out.println("Bye world");
        Controller controller = new Controller();
        Gui gui = new Gui(controller);
=======
import java.util.*;

import model.Plan;
import model.Tour;
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
>>>>>>> dijkstra
    }
}

