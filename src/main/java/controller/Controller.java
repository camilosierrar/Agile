package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import config.Config;
import model.*;
import tsp.RunTSP;
import xml.XMLmap;
import xml.XMLrequest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;



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

    public void loadMap(String file) {
        XMLmap.readData(file);
    }

    public void loadRequests(String file) {
        XMLrequest.readData(file);
    }

    public List<Segment> findBestTour() {
        return RunTSP.runTSP();
    }

    public int findCoupleIndex(int index) {
        return TableContent.getCoupleIndex(index);
    }

    public void deleteSelection(int indexMax, int indexMin, TableContent tableContent) { tableContent.removeCouple(indexMax, indexMin); }

    public String getAddress(double lat, double lng) {
        String address = "";
        // create a client
        var client = HttpClient.newHttpClient();
        String uri = "https://api-adresse.data.gouv.fr/reverse/?lon="+lng+"&lat="+lat;
        System.out.println(uri);
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
        System.out.println("The address: " + address);
        return address;
    }

    public Request makeRequest (Intersection pickup, Intersection delivery) {
        Request req = new Request(pickup, delivery, Config.DURATION,Config.DURATION);
        return req;
    }

    public Tour addRequestToTour(Request req, Tour tour) {
        return tour.addRequest(req);
    }

    public Tour remRequest(Tour tour, long id) {
        return tour.removeRequest(id);
    }
}
