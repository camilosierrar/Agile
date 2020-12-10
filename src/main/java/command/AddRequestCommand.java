package command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.Variable;
import config.Config.Type_Request;
import dijkstra.Dijkstra;
import model.Node;
import model.Request;
import tsp.RunTSP;

public class AddRequestCommand implements MementoableCommand {

    private Request request;
    private Boolean recalculatePath;
    
    public AddRequestCommand(Request pRequest, Boolean pRecalculatePath) {
        this.request = pRequest;
        this.recalculatePath = pRecalculatePath;        
    }

    /**
     * Adds a request at the end of the current best tour or recalculates complete
     * tour if recalculatePath is true
     * @param request request to be added
     * @param recalculatePath
     */
    @Override
    public void execute() {
        long pickupId = request.getPickupAddress().getId();
        long deliveryId = request.getDeliveryAddress().getId();
        //Update modifications to Variable
        Variable.findNodeInFirstGraph(pickupId).setTypeOfNode(Type_Request.PICK_UP);
        Variable.findNodeInFirstGraph(deliveryId).setTypeOfNode(Type_Request.DELIVERY);
        Variable.pickUpDeliveryCouplesId.put(pickupId, deliveryId);
        Variable.pointsInterestId.add(pickupId);
        Variable.pointsInterestId.add(deliveryId);
        //Updates old Dijkstras
        for(Map.Entry<Node, Dijkstra> entry: Variable.dijkstras.entrySet())
            entry.getValue().addRequest(pickupId, deliveryId, entry.getKey().getId());
        
        //Executes dijkstra for added request
        RunTSP.doDijkstra(pickupId);
        RunTSP.doDijkstra(deliveryId);

        //Updates complete graph
        Node pickup = null;
        Node delivery = null;
        for(Map.Entry<Node, Dijkstra> entry : Variable.dijkstras.entrySet())
            if(entry.getKey().getId() == pickupId)
                pickup = entry.getKey();
            else if(entry.getKey().getId() == deliveryId)
                delivery = entry.getKey();
        
        List<Node> addedNodes = Arrays.asList(pickup,delivery);
        Variable.g.addRequest(addedNodes);

        if(recalculatePath)
            RunTSP.getSolution();
        else {
            Long idLast = Variable.sPathOfPointsInterests.pollLast();
            Variable.sPathOfPointsInterests.add(pickupId);
            Variable.sPathOfPointsInterests.add(deliveryId);
            Variable.sPathOfPointsInterests.add(idLast);
            RunTSP.computeFullShortestPath();
        }
    }

    @Override
    public Memento takeSnapshot() {
        return new Memento(Variable.pickUpDeliveryCouplesId, Variable.shortestPath, Variable.sPathOfPointsInterests,
                           Variable.dijkstras, Variable.graph, Variable.pointsInterestId, Variable.g, Variable.tsp );
    }

}
