package tsp;

import config.Config;
import config.Variable;
import model.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SeqIter implements Iterator<Integer> {
	/**
	 * Vertices Candidates that can be visited after current vertex
	 */
	protected Integer[] candidates;

	/**
	 * number of vertices that can be visited after current vertex
	 */
	protected int nbCandidates;

	/**
	 * Creates an iterator to traverse the set of vertices in <code>unvisited</code>
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited set of unvisted nodes
	 * @param currentVertex current node's index
	 * @param g Corresponding graph
	 */
	public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g, Collection<Integer> visited){
		nbCandidates = 0;
		Map<Long, Integer> nodeAsInteger = g.getNodeAsInteger();
		//Candidates to be visited after currentVertex
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			//Always true since graph is complete
			if (g.isArc(currentVertex, s)) {
				Node curNode = null;
				//Retrieves point of interest corresponding to "s" index
				for(Map.Entry<Long, Integer> entry : nodeAsInteger.entrySet())
					if(entry.getValue().equals(s)){
						curNode = g.findNodeById(entry.getKey());
						break;
					}
				assert curNode != null;
				//if node is a delivery checks if corresponding pickup is visited
				if(curNode.getTypeOfNode().equals(Config.Type_Request.DELIVERY)){
					long pickupId = -1;
					for(Map.Entry<Long,Long> entry: Variable.pickUpDeliveryCouplesId.entrySet())
						if(entry.getValue() == curNode.getId()){
							pickupId = entry.getKey();
							break;
						}
					//retrieves index of pickup point
					int pickupIndex = nodeAsInteger.get(pickupId);
					if(visited.contains(pickupIndex))
						candidates[nbCandidates++] = s;
				}else{
					//Else adds anyway because it's either a departure address or a pickup
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
