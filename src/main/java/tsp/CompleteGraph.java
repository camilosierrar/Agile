package tsp;

import dijkstra.Dijkstra;
import dijkstra.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static config.Config.Type_Request.*;

public class CompleteGraph implements Graph {
	private int nbVertices;
	private double[][] cost;
	private Map<Long, Integer> nodeAsInteger;
	
	/**
	 * Create a complete graph of shortest path
	 * @param nbVertices
	 * @param shortestPaths
	 */
	public CompleteGraph(int nbVertices, Map<Node, Set<Node>> shortestPaths){
		this.nbVertices = nbVertices;
		this.nodeAsInteger = new HashMap<>();
		cost = new double[nbVertices][nbVertices];
		int i = 1;

		for(Node pointInterest: Dijkstra.getPointsInterest())
			if(pointInterest.getTypeOfNode().equals(DEPARTURE_ADDRESS))
				this.nodeAsInteger.put(pointInterest.getId(), 0);
			else
				this.nodeAsInteger.put(pointInterest.getId(), i++);
		
		//prints id and typedelivery
		for(Map.Entry<Long, Integer> entry: nodeAsInteger.entrySet()){
			System.out.println("Node " + entry.getValue() + ", type of node : " + Dijkstra.findNodeInterest(entry.getKey()).getTypeOfNode());
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

		/*for(int a = 0 ; a < nbVertices; a++){
			for(int b = 0; b < nbVertices; b++){
				System.out.print(cost[a][b] + "\t");
			}
			System.out.println();
		}*/
	}

	public Integer findIndexNodeById(long id){
		return nodeAsInteger.get(id);
	}

	public long findIdNodeByIndex(int index){
		long idCorrespondingNode = -1;
		for(Map.Entry<Long, Integer> entry : nodeAsInteger.entrySet())
			if(entry.getValue().equals(index)){
				idCorrespondingNode = entry.getKey();
				break;
			}
		return idCorrespondingNode;
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
