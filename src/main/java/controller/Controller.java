package controller;

import java.util.LinkedList;
import java.util.List;

import command.ListOfMementos;
import command.AddRequestCommand;
import command.ModifyOrderCommand;
import command.RemoveRequestCommand;

import model.Request;
import model.Segment;
import model.TableContent;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

public class Controller {

    private ListOfMementos l;

    public Controller() {
    }
    
    public void undo() {
        l.undo();
    }

    public void redo() {
        l.redo();
    }

    public void addRequest(Request request, Boolean recalculatePath) {
        l.add(new AddRequestCommand(request, recalculatePath));
    }

    public void removeRequest(Request request, Boolean recalculatePath) {
        l.add(new RemoveRequestCommand(request, recalculatePath));
    }

    public void modifyOrder(LinkedList<Long> newPath) {
        l.add(new ModifyOrderCommand(newPath));
    }

    public void loadMap(String file) {
        l.clearLists();
        XMLmap.readData(file);
    }

    public void loadRequests(String file) {
        l.clearLists();
        XMLrequest.readData(file);
    }

    public List<Segment> findBestTour() {
        return RunTSP.runTSP();
    }

    public int findCoupleIndex(int index) {
        return TableContent.getCoupleIndex(index);
    }

    public void deleteSelection(int indexMax, int indexMin, TableContent tableContent) { 
        tableContent.removeCouple(indexMax, indexMin);
    }

}
