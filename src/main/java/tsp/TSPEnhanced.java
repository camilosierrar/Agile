package tsp;

import java.util.Collection;
import java.util.Iterator;
/**
 * The TSP enhanced class
 */
public class TSPEnhanced extends TSP1 {

    @Override
    protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
        //l is lowest cost's arc between last vertex visited and an unvisited one
        double l = Double.MAX_VALUE;
        for (int i = 0; i < g.getNbVertices(); i++) {
            if (i != currentVertex && !unvisited.contains(i)) {
                l = Double.min(l, g.getCost(currentVertex, i));
            }
        }
        // Sum of li's for each unvisited nodes
        double somme = 0; 
        for (int i : unvisited) {
            //li is lowest cost's arc going out of current evaluation node to another unvisited node or source
            double li = g.getCost(i, 0);
            for (int j : unvisited) 
                if (i != j) 
                    li = Double.min(li, g.getCost(i, j));
            somme += li;
        }
        return (int)l + somme;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g, Collection<Integer> visited) {
        return new SeqIterEnhanced(unvisited, currentVertex, g, visited);
    }
}
