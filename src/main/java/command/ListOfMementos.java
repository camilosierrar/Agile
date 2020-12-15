package command;

import java.util.ArrayDeque;

//Class inspired of code in this link : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/

public class ListOfMementos {

    private ArrayDeque<BeforeAfter> undos;
	private ArrayDeque<BeforeAfter> redos;

    /**
     * Initialises the Array Double Ended queue of undos and redos
     */
    public ListOfMementos() {
        undos= new ArrayDeque<BeforeAfter>();
		redos= new ArrayDeque<BeforeAfter>();
    }

    /**
     * Adds a snapshot of the state of the system
     * to the queue of the undos and clears redos queue
     * @param c
     */
    public void add(MementoableCommand c) {
        if(c != null) {
            Memento before = c.takeSnapshot();
            c.execute();
            Memento after = c.takeSnapshot();
            undos.push(new BeforeAfter(before, after));
            redos.clear();
            //printLinst();
        }
    }

    /**
     * Get last snapshot of the system in undos queue and restore it
     * Adds previous snapshot of system to redos queue
     */
    public void undo() {
        BeforeAfter latestMemento = undos.pollFirst();
        if(latestMemento != null) {
            Memento latestBefore = latestMemento.before;
            latestBefore.restore();
            redos.push(latestMemento);
        }
        printLinst();
    }

    /**
     * Get last snapshot of the system in redos queue and restore it
     * Adds previous snapshot of system to undos queue
     */
    public void redo() {
        BeforeAfter latestMemento = redos.pollFirst();
        if(latestMemento != null) {
            Memento latestAfter = latestMemento.after;
            latestAfter.restore();
            undos.push(latestMemento);
        }
        printLinst();
    }

    /**
     * Clear both undos and redos queue
     */
    public void clearLists() {
        this.undos.clear();
        this.redos.clear();
    }

    /**
     * Pretty print of undos and redos queue
     */
    public void printLinst() {
        int i=0;
        System.out.println();
        System.out.println("---------------UNDOS : --------------");
        for(BeforeAfter bf: undos) {
            System.out.println("Before "+ i   +" : " + bf.before.sPathOfPointsInterests);
            System.out.println("After  "+ i++ +" : " + bf.after.sPathOfPointsInterests+"\n");
        }
        i=0;
        System.out.println();
        System.out.println("---------------REDOS : --------------");
        for(BeforeAfter bf: redos) {
            System.out.println("Before "+ i   +" : " + bf.before.sPathOfPointsInterests);
            System.out.println("After  "+ i++ +" : " + bf.after.sPathOfPointsInterests);
        }
    }

}

//Source : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/
class BeforeAfter {
    final Memento before;
    final Memento after;

    /**
     * Create a data structure adapted to undo/redo implementation with Memento
     * @param pBefore
     * @param pAfter
     */
    public BeforeAfter(Memento pBefore, Memento pAfter) {
        this.before = pBefore;
        this.after = pAfter;
    }
}
