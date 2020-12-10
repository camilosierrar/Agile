package config;

import com.google.common.collect.Multimap;

import command.Memento;
import dijkstra.Dijkstra;
import model.Node;
import model.Plan;
import model.Tour;
import tsp.Graph;
import tsp.TSP;

import java.util.*;

public class Variable {
    public static Plan cityPlan;
    public static Tour tour;
    /**
     *  Key: pickup id, Value: delivery id
     */
    public static Map<Long,Long> pickUpDeliveryCouplesId = new HashMap<>();
    /**
     *  Stores all nodes to traverse (from departure to departure) to obtain optimal tour (minimum distance)
     */
    public static LinkedList<Long> shortestPath = new LinkedList<>();
    /**
     * Store only id of points of interest in order retrieved by the tsp
     */
    public static LinkedList<Long> sPathOfPointsInterests = new LinkedList<>();
    /**
     * Store for each point of interest its corresponding dijkstra graph
     */
    public static Map<Node, Dijkstra> dijkstras = new HashMap<>();
    /**
     * Store all the node of the graph once, so that intersections and segments lists don't need to be percorred multiple time
     */
    public static Set<Node> graph = new HashSet<>();
    /**
     * ID of points of interest, not ordered
     */
    public static Set<Long> pointsInterestId = new HashSet<>();
    /**
     * Complete graph
     */
    public static Graph g;
    /**
     * Instance of TSP
     */
    public static TSP tsp;

    public static ArrayDeque<Memento> undos = new ArrayDeque<>();
    public static ArrayDeque<Memento> redos = new ArrayDeque<>();

    /**
     * @param id
     * @return Corresponding Node in graph
     */
    public static Node findNodeInFirstGraph(long id){
        for(Node node: Variable.graph){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }
}
