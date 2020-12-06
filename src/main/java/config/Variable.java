package config;

import dijkstra.Dijkstra;
import model.Node;
import model.Plan;
import model.Tour;
import tsp.Graph;

import java.util.*;

public class Variable {
    public static Plan cityPlan;
    public static Tour tour;
    /**
     *  key: pickup, value: delivery
     */
    public static Map<Long,Long> pickUpDeliveryCouplesId = new HashMap<>();
    public static Map<Node, Dijkstra> dijkstras = new HashMap<>();
    public static Map<Node, Set<Node>> shortestPaths = new HashMap<>();
    public static Set<Node> graph = new HashSet<>();
    public static Set<Long> pointsInterestId = new HashSet<>();
    public static Graph g;


    public static Node findNodeInFirstGraph(long id){
        for(Node node: Variable.graph){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }
}
