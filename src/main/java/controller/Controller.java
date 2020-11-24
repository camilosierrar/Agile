package controller;

import model.Plan;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

public class Controller {

    public Controller() {
        // TODO constructor
    }

    public Plan loadMap(String file) {
        return XMLmap.readData(file);
    }

    public Tour loadRequests(String file) {
        return XMLrequest.readData(file);
    }

    public void findBestTour() {
        // TODO
    }
}
