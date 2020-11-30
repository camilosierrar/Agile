package controller;

import model.Plan;
import model.Segment;
import model.Tour;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.LinkedList;

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

    public LinkedList<Segment> findBestTour(Tour tour) {
        return RunTSP.getSolution(tour);
    }
}
