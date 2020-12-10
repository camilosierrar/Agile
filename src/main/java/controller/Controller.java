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

public class Controller {

    private ListOfMementos l;

    public Controller() {
        l = new ListOfMementos();
    }
    
    public void undo() {
        l.undo();
    }

    public void redo() {
        l.redo();
    }

    public List<Segment> addRequest(Request request, Boolean recalculatePath) {
        l.add(new AddRequestCommand(request, recalculatePath));
        return RunTSP.getSegmentsSolution();
    }

    public List<Segment> removeRequest(Request request, Boolean recalculatePath) {
        l.add(new RemoveRequestCommand(request, recalculatePath));
        return RunTSP.getSegmentsSolution();
    }

    public List<Segment> modifyOrder(LinkedList<Long> newPath) {
        l.add(new ModifyOrderCommand(newPath));
        return RunTSP.getSegmentsSolution();
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
