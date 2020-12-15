package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import config.Config;
import model.*;
import model.Plan;
import java.util.LinkedList;
import java.util.List;

import command.ListOfMementos;
import command.AddRequestCommand;
import command.ModifyOrderCommand;
import command.RemoveRequestCommand;
import config.Variable;
import model.Request;
import model.Segment;
import model.TableContent;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Our Controller class that we will call whenever we need to use a function related to the algorithms
 * or to manage our best tour, or load our map from a file
 */
public class Controller {

    private ListOfMementos l;

    /**
     * Initialises Controller and List of undos and redos to empty queue
     */
    public Controller() {
        l = new ListOfMementos();
    }

    /**
     * The undo function
     * @return list of segments updated
     */
    public List<Segment> undo() {
        l.undo();
        return RunTSP.getSegmentsSolution();
    }

    /**
     * The redo function
     * @return list of segments updated
     */
    public List<Segment> redo() {
        l.redo();
        return RunTSP.getSegmentsSolution();
    }

    /**
     * Adding a request to our list
     * @param request the request we add
     * @param recalculatePath the boolean stating if we want to recalculate the whole best tour
     * @return list of segments updated
     */
    public List<Segment> addRequest(Request request, Boolean recalculatePath) {
        l.add(new AddRequestCommand(request, recalculatePath));
        return RunTSP.getSegmentsSolution();
    }

    /**
     * Removes a request from our list
     * @param request the request we remove
     * @param recalculatePath the boolean stating if we want to recalculate the whole best tour
     * @return list of segments updated
     */
    public List<Segment> removeRequest(Request request, Boolean recalculatePath) {
        l.add(new RemoveRequestCommand(request, recalculatePath));
        return RunTSP.getSegmentsSolution();
    }

    /**
     * Modify the order of the tour
     * @param newPath the path we want to update and add
     * @return an update list of segments
     */
    public List<Segment> modifyOrder(LinkedList<Long> newPath) {
        l.add(new ModifyOrderCommand(newPath));
        return RunTSP.getSegmentsSolution();
    }

    /**
     * Loads the map from a file
     * @param file the file
     */
    public void loadMap(String file) {
        l.clearLists();
        XMLmap.readData(file);
    }

    /**
     * Loads the request from a file
     * @param file the file
     */
    public void loadRequests(String file) {
        l.clearLists();
        XMLrequest.readData(file);
    }

    /**
     * Calls the algorithm to find the best tour
     * @return the list of segments composing the best tour
     */
    public List<Segment> findBestTour() {
        return RunTSP.runTSP();
    }

    /**
     * Find the couple index
     * @param index the index
     * @return the couple index
     */
    public int findCoupleIndex(int index) {
        return TableContent.getCoupleIndex(index);
    }

    /**
     * Delete the selected couple
     * @param indexMax the index max
     * @param indexMin the index min
     * @param tableContent the content of the table
     */
    public void deleteSelection(int indexMax, int indexMin, TableContent tableContent) { 
        tableContent.removeCouple(indexMax, indexMin);
    }

    /**
     * Stop the algorithm to find the current shortest path
     */
    public void stopAlgo() {
        Variable.cutAlgo = true;
    }

    /**
     * Finds the address from a latitude and longitude
     * @param lat the latitude
     * @param lng the longitude
     * @return a string containing the address
     */
    public String getAddress(double lat, double lng) {
        String address = "";
        // create a client
        var client = HttpClient.newHttpClient();
        String uri = "https://api-adresse.data.gouv.fr/reverse/?lon="+lng+"&lat="+lat;
        //System.out.println(uri);
        // create a request
        var request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .build();

        // use the client to send the request
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // the response:
        final JsonNode node;
        try {
            node = new ObjectMapper().readTree(response.body());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "-";
        }
        address = node.get("features").get(0).get("properties").get("label").asText();
        //System.out.println("The address: " + address);
        return address;
    }
    /**
     * Constructs a request based on two intersections
     * @param pickup the pickup intersection
     * @param delivery the delivery intersection
     * @return req the new request that is added
     */
    public Request makeRequest (Intersection pickup, Intersection delivery) {
        Request req = new Request(pickup, delivery, Config.DURATION,Config.DURATION);
        return req;
    }
    /**
     * Adding a request to a tour
     * @param req the request to add to the tour
     * @param tour the tour we add the request to
     * @return tour the updated tour
     */
    public Tour addRequestToTour(Request req, Tour tour) {
        return tour.addRequest(req);
    }
    /**
     * Removes a request from the tour
     * @param tour the tour
     * @param id the id of the request we want to delete
     * @return tour the tour updated
     */
    public Tour remRequest(Tour tour, long id) {
        return tour.removeRequest(id);
    }
}
