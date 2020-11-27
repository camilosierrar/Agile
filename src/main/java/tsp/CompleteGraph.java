package tsp;

import dijkstra.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static config.Config.Type_Request.DEPARTURE_ADDRESS;
import static config.Config.Type_Request.PICK_UP;

public class CompleteGraph implements Graph {
	int nbVertices;
	double[][] cost;
	Map<Node, Integer> idPoints;
	Map<Node, Node> pickupDeliveryCouples;
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(int nbVertices, Map<Node, Set<Node>> shortestPaths, Set<Node> pointsInterest, Map<Node,Node> pickupDeliveryCouples){
		this.pickupDeliveryCouples = pickupDeliveryCouples;
		//System.out.println(pointsInterest);
		this.nbVertices = nbVertices;
		this.idPoints = new HashMap<>();
		cost = new double[nbVertices][nbVertices];
		int i = 0;

		for(Node pointInterest: pointsInterest){
			if(pointInterest.getTypeOfNode().equals(DEPARTURE_ADDRESS)){
				this.idPoints.put(pointInterest, i++);
			}
		}
		for(Node pointInterest: pointsInterest){
			if(!this.idPoints.containsKey(pointInterest)) this.idPoints.put(pointInterest, i++);
		}
		//prints id and typedelivery
		for(Map.Entry<Node, Integer> entry: idPoints.entrySet()){
			System.out.println("Node " + entry.getValue() + ", type of node : " + entry.getKey().getTypeOfNode());
		}
		for(Map.Entry<Node, Set<Node>> entry: shortestPaths.entrySet()){
			Node source = entry.getKey();
			Set<Node> destinations = entry.getValue();
			int x = findNodeIndex(source.getId());
			for(Node destination: destinations){
				int y = findNodeIndex(destination.getId());
				cost[x][y] = destination.getDistance();
			}
		}
		/*for(int a = 0 ; a < nbVertices; a++){
			for(int b = 0; b < nbVertices; b++){
				System.out.print(cost[a][b] + "\t");
			}
			System.out.println();
		}*/
	}

	public Integer findNodeIndex(long id){
		for(Map.Entry<Node, Integer> entry: idPoints.entrySet()){
			if(entry.getKey().getId() == id){
				return entry.getValue();
			}
		}
		return null;
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
	public Map<Node, Node> getPickupDeliveryCouples() {
		return pickupDeliveryCouples;
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

	@Override
	public Map<Node, Integer> getIdPoints() {
		return idPoints;
	}
}
