package command;

import command.Memento;
import config.Variable;
import dijkstra.Dijkstra;
import model.Node;
import tsp.Graph;
import tsp.TSP;
import java.util.*;

public class Memento {

    public Map<Long,Long> pickUpDeliveryCouplesId = new HashMap<>();
    public LinkedList<Long> shortestPath = new LinkedList<>();
    public LinkedList<Long> sPathOfPointsInterests = new LinkedList<>();
    public Map<Node, Dijkstra> dijkstras = new HashMap<>();
    public Set<Node> graph = new HashSet<>();
    public Set<Long> pointsInterestId = new HashSet<>();
    public Graph g;
    public TSP tsp;

    public Memento() {
    }

    public Memento(Map<Long,Long> pPickUpDeliveryCouplesId, LinkedList<Long> pShortestPath,
                   LinkedList<Long> pSPathOfPointsInterests, Map<Node, Dijkstra> pDijkstras, Set<Node> pGraph, 
                   Set<Long> pPointsInterestId, Graph pG, TSP pTsp) {
        this.pickUpDeliveryCouplesId = pPickUpDeliveryCouplesId;
        this.shortestPath = pShortestPath;
        this.sPathOfPointsInterests = pSPathOfPointsInterests;
        this.dijkstras = pDijkstras;
        this.graph = pGraph;
        this.pointsInterestId = pPointsInterestId;
        this.g = pG;
        this.tsp = pTsp;
    }

	void restore() {
        Variable.pickUpDeliveryCouplesId = this.pickUpDeliveryCouplesId;
        Variable.shortestPath = this.shortestPath;
        Variable.sPathOfPointsInterests = this.sPathOfPointsInterests;
        Variable.dijkstras = this.dijkstras;
        Variable.graph = this.graph;
        Variable.pointsInterestId = this.pointsInterestId;
        Variable.g = this.g;
        Variable.tsp = this.tsp;
    }
}
