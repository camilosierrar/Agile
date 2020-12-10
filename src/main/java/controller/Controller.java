package controller;

import com.fasterxml.jackson.databind.JsonNode;
import model.Plan;
import model.Segment;
import model.TableContent;
import model.Tour;
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

    public String getAddress(double lat, double lng) throws IOException, InterruptedException {
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
        var response = client.send(request,HttpResponse.BodyHandlers.ofString());

        // the response:
        final JsonNode node = new ObjectMapper().readTree(response.body());
        address = node.get("features").get(0).get("properties").get("label").asText();
        System.out.println("The address: " + address);
        return address;
    }
}
