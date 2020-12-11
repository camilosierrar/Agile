package command;

import java.util.ArrayDeque;

//Class inspired of code in this link : https://blog.zenika.com/2014/12/15/pattern-command-undo-variations-compensation-replay-memento2/

public class ListOfMementos {

    private ArrayDeque<BeforeAfter> undos;
	private ArrayDeque<BeforeAfter> redos;

    public ListOfMementos() {
        undos= new ArrayDeque<BeforeAfter>();
		redos= new ArrayDeque<BeforeAfter>();
    }

    public void add(MementoableCommand c) {
        if(c != null) {
            Memento before = c.takeSnapshot();
            c.execute();
            Memento after = c.takeSnapshot();
            undos.push(new BeforeAfter(before, after));
            redos.clear();
            printLinst();
        }
    }

    public void undo() {
        BeforeAfter latestMemento = undos.pollFirst();
        if(latestMemento != null) {
            Memento latestBefore = latestMemento.before;
            latestBefore.restore();
            redos.push(latestMemento);
        }
        printLinst();
    }

    public void redo() {
        BeforeAfter latestMemento = redos.pollFirst();
        if(latestMemento != null) {
            Memento latestAfter = latestMemento.after;
            latestAfter.restore();
            undos.push(latestMemento);
        }
        printLinst();
    }

    public void clearLists() {
        this.undos.clear();
        this.redos.clear();
    }

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

    public BeforeAfter(Memento pBefore, Memento pAfter) {
        this.before = pBefore;
        this.after = pAfter;
    }
}
