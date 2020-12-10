package command;

import command.Memento;
import config.Variable;
import dijkstra.Dijkstra;
import model.Node;
import tsp.Graph;
import tsp.TSP;
import java.util.*;

import org.apache.commons.lang3.SerializationUtils;

public class Memento {

    public Map<Long,Long> pickUpDeliveryCouplesId = new HashMap<>();
    public LinkedList<Long> shortestPath = new LinkedList<>();
    public LinkedList<Long> sPathOfPointsInterests = new LinkedList<>();
    public Map<Node, Dijkstra> dijkstras = new HashMap<>();
    public Set<Node> graph = new HashSet<>();
    public Set<Long> pointsInterestId = new HashSet<>();
    public Graph g;

    public Memento() {
    }

    public Memento(Map<Long,Long> pPickUpDeliveryCouplesId, LinkedList<Long> pShortestPath,
                   LinkedList<Long> pSPathOfPointsInterests, Map<Node, Dijkstra> pDijkstras, Set<Node> pGraph, 
                   Set<Long> pPointsInterestId, Graph pG) {
        this.pickUpDeliveryCouplesId =  SerializationUtils.clone( (HashMap<Long,Long>) pPickUpDeliveryCouplesId) ;
        this.shortestPath = SerializationUtils.clone(pShortestPath);
        this.sPathOfPointsInterests = SerializationUtils.clone(pSPathOfPointsInterests) ;
        this.dijkstras = SerializationUtils.clone((HashMap<Node, Dijkstra>) pDijkstras);
        this.graph = SerializationUtils.clone((HashSet<Node>) pGraph);
        this.pointsInterestId = SerializationUtils.clone((HashSet<Long>) pPointsInterestId);
        this.g = SerializationUtils.clone(pG);
    }

	void restore() {
        Variable.pickUpDeliveryCouplesId = this.pickUpDeliveryCouplesId;
        Variable.shortestPath = this.shortestPath;
        Variable.sPathOfPointsInterests = this.sPathOfPointsInterests;
        Variable.dijkstras = this.dijkstras;
        Variable.graph = this.graph;
        Variable.pointsInterestId = this.pointsInterestId;
        Variable.g = this.g;
    }
}
