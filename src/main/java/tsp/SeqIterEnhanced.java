package tsp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class SeqIterEnhanced extends SeqIter {
	public class VertexComparator implements Comparator<Integer> {
		Graph graph;
		int origin;

		public VertexComparator(Graph graph, int origin) {
			this.graph = graph;
			this.origin = origin;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			return (int) (graph.getCost(origin, o1) - graph.getCost(origin, o2));
		}

	}

	public SeqIterEnhanced(Collection<Integer> unvisited, int currentVertex, Graph g, Collection<Integer> visited) {
		super(unvisited, currentVertex, g, visited);
		candidates = Arrays.copyOf(candidates, nbCandidates);
		Arrays.sort(candidates, new VertexComparator(g, currentVertex));
	}
}
