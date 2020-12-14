package command;

import command.Memento;
import config.Variable;
import dijkstra.Dijkstra;
import model.Node;
import tsp.Graph;
import tsp.RunTSP;

import java.util.*;

import org.apache.commons.lang3.SerializationUtils;

public class Memento {

    public final Map<Long,Long> pickUpDeliveryCouplesId;
    public final LinkedList<Long> sPathOfPointsInterests;
    public final Map<Node, Dijkstra> dijkstras;
    public final Set<Node> graph;
    public final Set<Long> pointsInterestId;
    public final Graph g;

    /**
     * Create a memento of current state of the system : save values of important variables 
     * @param pPickUpDeliveryCouplesId
     * @param pSPathOfPointsInterests
     * @param pDijkstras
     * @param pGraph
     * @param pPointsInterestId
     * @param pG
     */
    public Memento(Map<Long,Long> pPickUpDeliveryCouplesId,
                   LinkedList<Long> pSPathOfPointsInterests, Map<Node, Dijkstra> pDijkstras, Set<Node> pGraph, 
                   Set<Long> pPointsInterestId, Graph pG) {
        this.pickUpDeliveryCouplesId =  SerializationUtils.clone((HashMap<Long,Long>) pPickUpDeliveryCouplesId);
        this.sPathOfPointsInterests = SerializationUtils.clone(pSPathOfPointsInterests) ;
        this.dijkstras = SerializationUtils.clone((HashMap<Node, Dijkstra>) pDijkstras);
        this.graph = SerializationUtils.clone((HashSet<Node>) pGraph);
        this.pointsInterestId = SerializationUtils.clone((HashSet<Long>) pPointsInterestId);
        this.g = SerializationUtils.clone(pG);
    }

    /**
     * Restore the system to a previous state
     */
	void restore() {
        Variable.pickUpDeliveryCouplesId = SerializationUtils.clone((HashMap<Long,Long>) this.pickUpDeliveryCouplesId);
        Variable.sPathOfPointsInterests = SerializationUtils.clone(this.sPathOfPointsInterests);
        Variable.dijkstras = SerializationUtils.clone((HashMap<Node,Dijkstra>) this.dijkstras);
        Variable.graph = SerializationUtils.clone((HashSet<Node>) this.graph);
        Variable.pointsInterestId = SerializationUtils.clone((HashSet<Long>) this.pointsInterestId);
        Variable.g = SerializationUtils.clone(this.g);
        RunTSP.computeFullShortestPath();
    }
}
