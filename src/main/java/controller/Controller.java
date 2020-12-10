package controller;

import model.Segment;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.List;
import java.util.Stack;

import command.InitialState;
import command.ListOfCommands;
import command.State;

public class Controller {

    private ListOfCommands l;
    private State currentState;

    //private Stack<Tour> undoStack;
    //private Stack<Tour> redoStack;

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
        /*undoStack = new Stack<>();
        redoStack = new Stack<>();*/
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


    /*public Tour undo() {
        // TODO CHECK IF EMPTY
        System.out.println("Stack size when undoing" + undoStack.size());
        Tour tour = undoStack.pop();
        System.out.println("Tour being poped from undoStack is: " + tour.toString());
        redoStack.push(tour);
        return tour;
    }

    public Tour redo() {
        // TODO CHECK IF EMPTY
        Tour tour = redoStack.pop();
        undoStack.push(tour);
        return tour;
    }

    public Tour removeRequest(Tour tour, Intersection intersection) {
        System.out.println("Tour being added to undoStack is: " + tour.getRequests().toString());
        Tour stackTour = new Tour(tour);
        undoStack.push(stackTour);
        System.out.println("Stack size " + undoStack.size());
        return tour.removeRequest(tour.getRequestbyIntersection(intersection));
    }*/
}
