package tsp;

import model.Node;

import java.util.*;

import config.Variable;

import static config.Config.Type_Request.*;


public class CompleteGraph implements Graph {
	private int nbVertices;
	private double[][] cost;
	/**
	 * Assigns an index (integer) to a node's id
	 * Used to store points of interest distance in cost variable (matrix)
	 */
	private final Map<Long, Integer> nodeAsInteger;
	/**
	 * Points of interest of the graph - the distance is 0
	 */
	private final Set<Node> nodes;

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
		for(Map.Entry<Node,Set<Node>> entry: shortestPaths.entrySet()) {
			if(entry.getKey().getTypeOfNode().equals(DEPARTURE_ADDRESS))
				this.nodeAsInteger.put(entry.getKey().getId(), 0);
			else
				this.nodeAsInteger.put(entry.getKey().getId(), i++);
			nodes.add(entry.getKey());
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

	public void prettyPrint(){
		System.out.println("-----START PRINTING COMPLETE GRAPH INFOS-----");
		System.out.println("nbVertices: " + this.nbVertices);
		System.out.println("Cost table: ");
		for (double[] doubles : cost) System.out.println(Arrays.toString(doubles));
		System.out.println("Points of interest : ");
		for(Node node: this.nodes) System.out.println(node.getId());
		for(Map.Entry<Long, Integer> entry: this.nodeAsInteger.entrySet())
			System.out.println("id: " + entry.getKey() + " index:" + entry.getValue());
		System.out.println("-----END PRINTING COMPLETE GRAPH INFOS--------");

	}

    /**
     * TODO
     * @param shortestPathsAddedRequest
     * @param addedNodes
     */
    public void addRequest(List<Node> addedNodes) {
        //Updates vertices
        this.nbVertices += 2;
        int addIndex = this.nbVertices - 2;
        //Adds nodes to variables
        this.nodes.addAll(addedNodes);
        for (Node addNode : addedNodes) {
            this.nodeAsInteger.put(addNode.getId(), addIndex++);
        }
        //Updates cost
		double[][] newCost = new double[nbVertices][nbVertices];
        //Computes new cost from new points of interests to every old points of interest
        //And from old points of interests to newly added points of interest
        for (Map.Entry<Node, Set<Node>> entry : Variable.shortestPaths.entrySet()) {
            Node source = entry.getKey();
            int x = findIndexNodeById(source.getId());
            Set<Node> destinations = entry.getValue();
            for (Node destination : destinations) {
                int y = findIndexNodeById(destination.getId());
                newCost[x][y] = destination.getDistance();
            }
        }
        this.cost = newCost;
	}
	
	/**
     * TODO
     * @param shortestPathsAddedRequest
     * @param addedNodes
     */
    public void removeRequest(List<Node> nodesRemoved) {
        //Updates vertices
        this.nbVertices -= 2;
        //Removes nodes to variables
		this.nodes.removeAll(nodesRemoved);
		this.nodeAsInteger.clear();
		int i = 1;
		for(Map.Entry<Node,Set<Node>> entry: Variable.shortestPaths.entrySet()) 
			if(entry.getKey().getTypeOfNode().equals(DEPARTURE_ADDRESS))
				this.nodeAsInteger.put(entry.getKey().getId(), 0);
			else
				this.nodeAsInteger.put(entry.getKey().getId(), i++);
		
        //Updates cost
		double[][] newCost = new double[nbVertices][nbVertices];
		
        //Update cost
        for (Map.Entry<Node, Set<Node>> entry : Variable.shortestPaths.entrySet()) {
            Node source = entry.getKey();
            int x = findIndexNodeById(source.getId());
            Set<Node> destinations = entry.getValue();
            for (Node destination : destinations) {
                int y = findIndexNodeById(destination.getId());
                newCost[x][y] = destination.getDistance();
            }
        }
        this.cost = newCost;
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
		for(Node node : this.nodes)
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
