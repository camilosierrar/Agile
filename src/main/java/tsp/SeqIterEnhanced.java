package tsp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SeqIterEnhanced extends SeqIter {
	/**
	 * Order heuristic
	 * Sorts vertices by their distance to the current vertex in ascending order
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 * @param visited
	 */
	public SeqIterEnhanced(Collection<Integer> unvisited, int currentVertex, Graph g, Collection<Integer> visited) {
		super(unvisited, currentVertex, g, visited);
		//To get rid of null values
		candidates = Arrays.copyOf(candidates, nbCandidates);
		//Sort
		Arrays.sort(candidates, new EdgeComparator(g, currentVertex));
	}

	/**
	 * Comparator class for graph of unvisited
	 */
	public class EdgeComparator implements Comparator<Integer> {
		Graph unvisitedGraph;
		int currentVertex;

		public EdgeComparator(Graph graph, int pCurrentVertex) {
			this.unvisitedGraph = graph;
			this.currentVertex = pCurrentVertex;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			return (int)(unvisitedGraph.getCost(currentVertex, o1) - unvisitedGraph.getCost(currentVertex, o2));
		}

	}
}
