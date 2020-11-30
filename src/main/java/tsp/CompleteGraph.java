package tsp;

import dijkstra.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static config.Config.Type_Request.*;


public class CompleteGraph implements Graph {
	private int nbVertices;
	private double[][] cost;
	/**
	 * Assigns an index (integer) to a node's id
	 * Used to store points of interest distance in cost variable (matrix)
	 */
	private Map<Long, Integer> nodeAsInteger;
	/**
	 * Points of interest of the graph - the distance is 0
	 */
	private Set<Node> nodes;

	/**
	 * Creates a complete graph from all points of interests and shortest paths to every point of interest
	 * Fills cost and nodeAsInteger variables
	 * @param nbVertices number of points of interest
	 * @param shortestPaths shortest paths to every point of interest, for every point of interest
	 */
	public CompleteGraph(int nbVertices, Map<Node, Set<Node>> shortestPaths){
		//Initialized variables
		this.nbVertices = nbVertices;
		this.nodeAsInteger = new HashMap<>();
		this.nodes = new HashSet<>();
		cost = new double[nbVertices][nbVertices];
		//Match points of interest id to index (starting from 0 to nbVertices)
		//Adds departure address with index set at 0 (first value in the matrix)
		//Important as it enables TSP to start with departure address
		int i = 1;
		for(Map.Entry<Node,Set<Node>> pointInterest: shortestPaths.entrySet()) {
			if(pointInterest.getKey().getTypeOfNode().equals(DEPARTURE_ADDRESS))
				this.nodeAsInteger.put(pointInterest.getKey().getId(), 0);
			else
				this.nodeAsInteger.put(pointInterest.getKey().getId(), i++);
			nodes.add(pointInterest.getKey());
		}
		for(Map.Entry<Node, Set<Node>> entry: shortestPaths.entrySet()){
			Node source = entry.getKey();
			Set<Node> destinations = entry.getValue();
			int x = findIndexNodeById(source.getId());
			for(Node destination: destinations){
				int y = findIndexNodeById(destination.getId());
				cost[x][y] = destination.getDistance();
			}
		}
	}

	/**
	 * Finds node index in nodeAsInteger given its id
	 * @param id node's id
	 * @return corresponding index for given id
	 */
	public Integer findIndexNodeById(long id){
		return nodeAsInteger.get(id);
	}

	/**
	 * Finds node id in nodeAsInteger given its index
	 * @param index node's index
	 * @return corresponding id for given index
	 */
	@Override
	public long findIdNodeByIndex(int index){
		long idCorrespondingNode = -1;
		for(Map.Entry<Long, Integer> entry : nodeAsInteger.entrySet())
			if(entry.getValue().equals(index)){
				idCorrespondingNode = entry.getKey();
				break;
			}
		return idCorrespondingNode;
	}

	/**
	 * Finds point of interest with id given as input
	 * @param id node's id
	 * @return point of interest with input's id or null otherwise
	 */
	@Override
	public Node findNodeById(long id){
		Node correspondingNode = null;
		for(Node node : nodes)
			if(node.getId() == id) {
				correspondingNode = node;
				break;
			}
		return correspondingNode;
	}

	@Override
	public Set<Node> getNodes() {
		return this.nodes;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

	@Override
	public Map<Long, Integer> getNodeAsInteger() {
		return nodeAsInteger;
	}
}
