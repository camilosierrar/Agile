package config;

import dijkstra.Dijkstra;
import dijkstra.Node;
import model.Plan;
import model.Tour;

import java.util.*;

public class Variable {
    public static Plan cityPlan;
    public static Tour tour;
    public static Map<Node, Dijkstra> dijkstras = new HashMap<>();
    public static Map<Node, Set<Node>> shortestPaths = new HashMap<>();
    public static List<Long> pointsInterestId = new LinkedList<>();
    public static Map<Long,Long> pickUpDeliveryCouplesId = new HashMap<>(); // key: pickup, value: delivery
}
