package controller;

import model.Segment;
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

    public void loadMap(String file) {
        XMLmap.readData(file);
    }

    public void loadRequests(String file) {
        XMLrequest.readData(file);
    }

    public List<Segment> findBestTour() {
        setCurrentState(new InitialState());
        return RunTSP.runTSP();
    }

}
