package tsp;

import java.util.Collection;
import java.util.Iterator;

public class TSPEnhanced extends TemplateTSP{
    @Override
	protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIterEnhanced(unvisited, currentVertex, g);
	}
}