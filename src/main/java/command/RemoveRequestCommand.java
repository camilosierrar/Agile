package command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.Variable;
import dijkstra.Dijkstra;
import model.Node;
import model.Request;
import tsp.RunTSP;

public class RemoveRequestCommand implements MementoableCommand{
    private Request request;
    private Boolean recalculatePath;
    
    public RemoveRequestCommand(Request pRequest, Boolean pRecalculatePath) {
        this.request = pRequest;
        this.recalculatePath = pRecalculatePath;        
    }

    /**
     * Removes a request from the current best tour. Recalculates complete
     * tour if recalculatePath is true
     * @param request request to be removed
     * @param recalculatePath
     */
    @Override
    public void execute() {
        long pickupId = this.request.getPickupAddress().getId();
        long deliveryId = this.request.getDeliveryAddress().getId();

        if(Variable.pointsInterestId.contains(pickupId) && Variable.pointsInterestId.contains(deliveryId)) {
            Node pickupNode = Variable.g.findNodeById(pickupId);
            Node deliveryNode = Variable.g.findNodeById(deliveryId);
            
            Variable.pickUpDeliveryCouplesId.remove(pickupId, deliveryId);
            Variable.pointsInterestId.remove(pickupId);
            Variable.pointsInterestId.remove(deliveryId);
            Variable.dijkstras.remove(pickupNode);
            Variable.dijkstras.remove(deliveryNode);
            System.out.print("ICI-----\n"+Variable.dijkstras+ "\n\n");
            Variable.sPathOfPointsInterests.remove(pickupId);
            Variable.sPathOfPointsInterests.remove(deliveryId);
            for (Map.Entry<Node, Dijkstra> entry : Variable.dijkstras.entrySet()) 
                entry.getValue().removeRequest(pickupId, deliveryId, entry.getKey().getId());
            
            List<Node> nodesToRemove = Arrays.asList(pickupNode, deliveryNode);
            Variable.g.removeRequest(nodesToRemove);

            Variable.shortestPath.clear();
            if(this.recalculatePath)
                RunTSP.getSolution();
            else 
                RunTSP.computeFullShortestPath();
        }
    }

    @Override
    public Memento takeSnapshot() {
        return new Memento(Variable.pickUpDeliveryCouplesId, Variable.shortestPath, Variable.sPathOfPointsInterests,
                           Variable.dijkstras, Variable.graph, Variable.pointsInterestId, Variable.g );
    }
}
