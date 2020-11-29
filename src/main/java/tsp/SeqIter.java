package tsp;

import config.Config;
import dijkstra.Dijkstra;
import dijkstra.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SeqIter implements Iterator<Integer> {
	protected Integer[] candidates;
	protected int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g, Collection<Integer> visited){
		Map<Long, Integer> nodeAsInteger = g.getNodeAsInteger();
		//Candidates to be visited after currentVertex
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			//Always true since graph is complete
			if (g.isArc(currentVertex, s)) {
				Node curNode = null;
				for(Map.Entry<Long, Integer> entry : nodeAsInteger.entrySet())
					if(entry.getValue().equals(s)){
						curNode = g.findNodeById(entry.getKey());
						break;
					}
				
				assert curNode != null;
				
				//if node is a delivery check if pickup is visited
				if(curNode.getTypeOfNode().equals(Config.Type_Request.DELIVERY)){
					Node pickup = null;
					for(Map.Entry<Node,Node> entry: Dijkstra.getPickUpDeliveryCouples().entrySet())
						if(entry.getValue().getId() == curNode.getId()){
							pickup = entry.getKey();
							break;
						}
					//retrieves index of pickup point
					int pickupIndex = nodeAsInteger.get(pickup.getId());
					if(visited.contains(pickupIndex)){
						candidates[nbCandidates++] = s;
					}
				}else{
					candidates[nbCandidates++] = s;
				}
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Integer next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

}
