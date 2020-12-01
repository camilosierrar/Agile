package tsp;

import java.util.Collection;

public class TSPEnhanced extends TSP1 {

    @Override
    protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
        int l = Integer.MAX_VALUE;
        for (int i = 0; i < g.getNbVertices(); i++) {
            if (i != currentVertex && !unvisited.contains(i)) {
                l = Integer.min(l, (int)g.getCost(currentVertex, i));
            }
        }
        int s = 0; // Sum of l_i's
        for (int i : unvisited) {
            int li = (int)g.getCost(i, 0);
            for (int j : unvisited) {
                if (i != j) {
                    li = Integer.min(li, (int)g.getCost(i, j));
                }
            }
            s += li;
        }
        return l + s;
    }
}
