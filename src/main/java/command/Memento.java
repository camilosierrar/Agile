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

    public Memento(Map<Long,Long> pPickUpDeliveryCouplesId,
                   LinkedList<Long> pSPathOfPointsInterests, Map<Node, Dijkstra> pDijkstras, Set<Node> pGraph, 
                   Set<Long> pPointsInterestId, Graph pG) {
        this.pickUpDeliveryCouplesId =  SerializationUtils.clone( (HashMap<Long,Long>) pPickUpDeliveryCouplesId) ;
        this.sPathOfPointsInterests = SerializationUtils.clone(pSPathOfPointsInterests) ;
        this.dijkstras = SerializationUtils.clone((HashMap<Node, Dijkstra>) pDijkstras);
        this.graph = SerializationUtils.clone((HashSet<Node>) pGraph);
        this.pointsInterestId = SerializationUtils.clone((HashSet<Long>) pPointsInterestId);
        this.g = SerializationUtils.clone(pG);
    }

	void restore() {
        Variable.pickUpDeliveryCouplesId = this.pickUpDeliveryCouplesId;
        Variable.sPathOfPointsInterests = this.sPathOfPointsInterests;
        Variable.dijkstras = this.dijkstras;
        Variable.graph = this.graph;
        Variable.pointsInterestId = this.pointsInterestId;
        Variable.g = this.g;
        RunTSP.computeFullShortestPath();
    }
}
