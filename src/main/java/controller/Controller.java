package controller;

import model.Plan;
import model.Segment;
import model.Tour;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.List;

import command.InitialState;
import command.ListOfCommands;
import command.State;

public class Controller {

    private ListOfCommands l;
    private State currentState;

    public Controller() {
        currentState = new InitialState();
    }

    public void setCurrentState(State s){
        this.currentState = s;
    }
    
    public void undo() {
        currentState.undo();
    }

    public void redo() {
        currentState.redo();
    }

    public void addRequest() {
        currentState.addRequest();
    }

    public void removeRequest() {
        currentState.removeRequest();
    }

    public void modifyOrder() {
        currentState.modifyOrder();
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
