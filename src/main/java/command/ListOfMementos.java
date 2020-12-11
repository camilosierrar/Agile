package command;

import java.util.ArrayDeque;
import java.util.LinkedList;

import config.Variable;
import tsp.RunTSP;

//Class inspired of code in this link : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/

public class ListOfMementos {

    LinkedList<BeforeAfter> undos;
	LinkedList<BeforeAfter> redos;

    public ListOfMementos() {
        undos= new LinkedList<BeforeAfter>();
		redos= new LinkedList<BeforeAfter>();
    }

    public void add(MementoableCommand c) {
        if(c != null) {
            Memento before = c.takeSnapshot();
            c.execute();
            Memento after = c.takeSnapshot();
            undos.push(new BeforeAfter(before, after));
            redos.clear();
        }
    }

    public void undo() {
        BeforeAfter latestMemento = undos.pollFirst();
        if(latestMemento != null) {
            Memento latestBefore = latestMemento.before;
            latestBefore.restore();
            redos.push(latestMemento);
        }
    }

    public void redo() {
        BeforeAfter latestMemento = redos.pollFirst();
        if(latestMemento != null) {
            Memento latestAfter = latestMemento.after;
            latestAfter.restore();
            undos.push(latestMemento);
        }
    }

    public void clearLists() {
        this.undos.clear();
        this.redos.clear();
    }
}

//Source : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/
class BeforeAfter {
    final Memento before;
    final Memento after;

    public BeforeAfter(Memento before, Memento after) {
        this.before = before;
        this.after = after;
    }
}
