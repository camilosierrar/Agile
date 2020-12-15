package tsp;

import com.google.errorprone.annotations.Var;
import config.Variable;
import controller.Controller;
import junit.framework.Assert;
import model.Request;
import model.Segment;
import org.junit.jupiter.api.Test;
import xml.XMLmap;
import xml.XMLrequest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestTSP {

    /**
     * Computes best tour and compare expected visiting order with given visiting order
     */
    @Test
    public void testFindBestTour() {
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        List<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 1679901320);
        expectedSolution.add((long) 208769457);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        Assert.assertEquals(expectedSolution, orderedSolution);

    }

    /**
     * Computes best tour and add a request (recalculating the path).
     * Compare expected visiting order with given visiting order
     */
    @Test
    public void testAddRequest(){
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        Controller c = new Controller();
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26086127),
                Variable.cityPlan.getIntersectionById(26086128),
                20,
                40);
        c.addRequest(request, true);
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        List<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 26086127);
        expectedSolution.add((long) 26086128);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 1679901320);
        expectedSolution.add((long) 208769457);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        Assert.assertEquals(expectedSolution, orderedSolution);
    }

    /**
     * Computes best tour and delete oldest request
     */
    @Test
    public void testRemoveRequest(){
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        Controller c = new Controller();
        c.removeRequest(Variable.tour.getRequests().get(0), true);
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        List<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        Assert.assertEquals(expectedSolution, orderedSolution);

    }

    /**
     * Computes best tour and verifies order with modified order
     */
    @Test
    public void testModifyOrder(){
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        Controller c = new Controller();
        LinkedList<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 1679901320);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 208769457);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        c.modifyOrder(expectedSolution);
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        Assert.assertEquals(expectedSolution, orderedSolution);
    }

    /**
     * Computes best tour, add a request and undo
     * Compares if solutions are equal
     */
    @Test
    public void testUndo(){
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        Controller c = new Controller();
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26086127),
                Variable.cityPlan.getIntersectionById(26086128),
                20,
                40);
        c.addRequest(request, true);
        c.undo();
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        List<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 1679901320);
        expectedSolution.add((long) 208769457);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        Assert.assertEquals(expectedSolution, orderedSolution);
    }

    /**
     * Computes best tour, add a request,undo and redo
     * Compares if solutions are equal
     */
    @Test
    public void testRedo(){
        XMLmap.readData("smallMap.xml");
        XMLrequest.readData("requestsSmall2.xml");
        Variable.pickUpDeliveryCouplesId.clear();
        for (Request request : Variable.tour.getRequests()){
            Variable.pickUpDeliveryCouplesId.put(request.getPickupAddress().getId(), request.getDeliveryAddress().getId());
        }
        RunTSP.fillGraph();
        RunTSP.computeDijkstra();
        Variable.g = new CompleteGraph(Variable.pointsInterestId.size(), Variable.dijkstras);
        RunTSP.getSolution();
        Controller c = new Controller();
        Request request = new Request(
                Variable.cityPlan.getIntersectionById(26086127),
                Variable.cityPlan.getIntersectionById(26086128),
                20,
                40);
        c.addRequest(request, true);
        c.undo();
        c.redo();
        List<Long> orderedSolution = Variable.sPathOfPointsInterests;
        List<Long> expectedSolution =  new LinkedList<>();
        expectedSolution.add(2835339774L);
        expectedSolution.add((long) 26086127);
        expectedSolution.add((long) 26086128);
        expectedSolution.add((long) 208769120);
        expectedSolution.add((long) 1679901320);
        expectedSolution.add((long) 208769457);
        expectedSolution.add((long) 25336179);
        expectedSolution.add(2835339774L);
        Assert.assertEquals(expectedSolution, orderedSolution);
    }
}
