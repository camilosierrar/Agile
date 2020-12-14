package command;

import java.util.LinkedList;

import config.Variable;
import tsp.RunTSP;

public class ModifyOrderCommand implements MementoableCommand {

    private LinkedList<Long> newPath;

    /**
    * Constructs the command for modifying order of points of interest
    * @param pNewPath
    */ 
    public ModifyOrderCommand(LinkedList<Long> pNewPath) {
        this.newPath = pNewPath;
    }

    /**
     * Modify the tour with one provided
     * @param newPath
     */
    @Override
    public void execute() {
        Variable.sPathOfPointsInterests = this.newPath;
        RunTSP.computeFullShortestPath();
    }

    @Override
    public Memento takeSnapshot() {
        return new Memento(Variable.pickUpDeliveryCouplesId, Variable.sPathOfPointsInterests,
                           Variable.dijkstras, Variable.graph, Variable.pointsInterestId, Variable.g );
    }
}
