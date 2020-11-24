package controller;

import model.Plan;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

public class Controller {

    public Controller() {
        // TODO constructor
    }

    public Plan loadMap() {
        return XMLmap.readData();
    }

    public Tour loadRequests() {
        return XMLrequest.readData();
    }

    //public Intersection getIntersection()
}
