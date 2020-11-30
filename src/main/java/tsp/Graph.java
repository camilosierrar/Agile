package tsp;

import java.util.Map;
import java.util.Set;

import dijkstra.Node;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	public abstract int getNbVertices();

	/**
	 * @param i 
	 * @param j 
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	public abstract double getCost(int i, int j);

	/**
	 * @param i 
	 * @param j 
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	public abstract boolean isArc(int i, int j);

	/**
	 * @return The map that translate id of node to integer indexes
	 */
	public abstract  Map<Long, Integer> getNodeAsInteger();

	/**
	 * @return the set of nodes of the graph
	 */
	public abstract  Set<Node> getNodes();

	/**
	 * 
	 * @param index
	 * @return Node of the corresponding index
	 */
	public abstract long findIdNodeByIndex(int index);

	/**
	 * 
	 * @param id
	 * @return Node of corresponding id
	 */
	public abstract Node findNodeById(long id);
}
