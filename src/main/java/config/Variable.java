package config;

import com.google.common.collect.Multimap;
import dijkstra.Dijkstra;
import model.Node;
import model.Plan;
import model.Segment;
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
    public static Map<Node, Dijkstra> dijkstras = new HashMap<>();
    public static Map<Node, Set<Node>> shortestPaths = new HashMap<>();
    public static Set<Node> graph = new HashSet<>();
    public static Set<Long> pointsInterestId = new HashSet<>();
    /**
     *  Stores all nodes to traverse (from departure to departure) to obtain optimal tour (minimum distance)
     */
    public static LinkedList<Long> shortestPath = new LinkedList<>();
    public static LinkedList<Long> sPathOfPointsInterests = new LinkedList<>();
    public static Graph g;
    public static TSP tsp;

    public static Node findNodeInFirstGraph(long id){
        for(Node node: Variable.graph){
            if(node.getId() == id){
                return node;
            }
        }
        return null;
    }
}
