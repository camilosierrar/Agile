package tsp;

import dijkstra.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CompleteGraph implements Graph {
	int nbVertices;
	double[][] cost;
	Map<Node, Integer> idPoints;
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(int nbVertices, Map<Node, Set<Node>> shortestPaths, Set<Node> pointsInterest){
		//System.out.println(pointsInterest);
		this.nbVertices = nbVertices;
		this.idPoints = new HashMap<>();
		cost = new double[nbVertices][nbVertices];
		int i = 0;
		for(Node pointInterest: pointsInterest){
			this.idPoints.put(pointInterest, i);
			i++;
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
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

}
