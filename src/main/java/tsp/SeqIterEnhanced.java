package tsp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SeqIterEnhanced extends SeqIter {
	/**
	 * Order heuristic
	 * Sorts vertices by their distance to the current vertex in ascending order
	 * @param unvisited
	 * @param curEdge
	 * @param g
	 * @param visited
	 */
	public SeqIterEnhanced(Collection<Integer> unvisited, int curEdge, Graph g, Collection<Integer> visited) {
		super(unvisited, curEdge, g, visited);
		//To get rid of null values
		candidates = Arrays.copyOf(candidates, nbCandidates);
		//Sort
		Arrays.sort(candidates, new EdgeComparator(g, curEdge));
	}

	/**
	 * Comparator class for graph of unvisited
	 */
	public class EdgeComparator implements Comparator<Integer> {
		private Graph unvisitedGraph;
		private int currentVertex;

		public EdgeComparator(Graph graph, int pCurrentVertex) {
			this.unvisitedGraph = graph;
			this.currentVertex = pCurrentVertex;
		}

		@Override
		public int compare(Integer v1, Integer v2) {
			return (int)(unvisitedGraph.getCost(currentVertex, v1) - unvisitedGraph.getCost(currentVertex, v2));
		}

	}
}
