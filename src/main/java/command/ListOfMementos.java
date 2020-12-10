package command;

import java.util.ArrayDeque;

import config.Variable;

//Class inspired of code in this link : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/

public class ListOfMementos {

    ArrayDeque<BeforeAfter> undos;
	ArrayDeque<BeforeAfter> redos;

    public ListOfMementos() {
        undos= new ArrayDeque<BeforeAfter>();
		redos= new ArrayDeque<BeforeAfter>();
    }

    public void add(MementoableCommand c) {
        if(c != null) {
            Memento before = c.takeSnapshot();
            c.execute();
            Memento after = c.takeSnapshot();
            //System.out.print(before.sPathOfPointsInterests);
            //System.out.print(after.sPathOfPointsInterests);
            undos.push(new BeforeAfter(before, after));
            redos.clear();
        }
    }

    public void undo() {
        BeforeAfter latestMemento = undos.pollFirst();
        if(latestMemento != null) {
            Memento latestBefore = latestMemento.before;
            //System.out.print(latestBefore.sPathOfPointsInterests+ "-----");
            latestBefore.restore();
            System.out.print(Variable.sPathOfPointsInterests);
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
