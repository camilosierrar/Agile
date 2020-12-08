package controller;

import model.Plan;
import model.Segment;
import model.Tour;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.LinkedList;
import java.util.List;

import command.ListOfCommands;

public class Controller {

    private ListOfCommands l;

    public void undo() {

    }

    public void redo() {

    }

    public Controller() {
        // TODO constructor
    }

    public Plan loadMap(String file) {
        return XMLmap.readData(file);
    }

    public Tour loadRequests(String file) {
        return XMLrequest.readData(file);
    }

    public List<Segment> findBestTour() {
        return RunTSP.runTSP();
    }


}
