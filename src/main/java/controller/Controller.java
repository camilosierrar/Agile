package controller;

import model.Plan;
import model.Segment;
import model.Tour;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.LinkedList;
import java.util.List;

public class Controller {

    public Controller() {
        // TODO constructor
    }

    public void loadMap(String file) {
        XMLmap.readData(file);
    }

    public void loadRequests(String file) {
        XMLrequest.readData(file);
    }

    public List<Segment> findBestTour() {
        return RunTSP.runTSP();
    }


}
