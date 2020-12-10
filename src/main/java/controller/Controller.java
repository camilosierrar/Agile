package controller;

import model.Plan;
import model.Segment;
import model.TableContent;
import model.Tour;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.LinkedList;
import java.util.List;

public class Controller {
    public XMLmap xmlMap;
    public Controller() {
        // TODO constructor
    }

    public Plan loadMap(String file) {
        return XMLmap.readData(file);
    }

    public Tour loadRequests(String file) {
        return XMLrequest.readData(file);
    }

    public List<Segment> findBestTour(Tour tour) {
        return RunTSP.getSolution(tour);
    }

    public int findCoupleIndex(int index) {
        return TableContent.getCoupleIndex(index);
    }

    public void deleteSelection(int indexMax, int indexMin, TableContent tableContent) { tableContent.removeCouple(indexMax, indexMin); }
}
