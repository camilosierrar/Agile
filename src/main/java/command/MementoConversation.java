package command;

public class MementoConversation extends AbstractConversation<MementoableCommand, BeforeAfter> {
    @Override public void exec(MementoableCommand todo) {
        Memento before = todo.takeSnapshot();
        todo.execute();
        Memento after = todo.takeSnapshot();
        undos.push(new BeforeAfter(before, after));
        redos.clear();
    }
    @Override public void undo() {
        BeforeAfter latestMemento = undos.pop();
        if(latestMemento==null) return;
        Memento latestBefore = latestMemento.before;
        latestBefore.restore();
        redos.push(latestMemento);
    }
    @Override public void redo() {
        BeforeAfter latestMemento = redos.pop();
        if(latestMemento==null) return;
        Memento latestAfter = latestMemento.after;
        latestAfter.restore();
        undos.push(latestMemento);
    }
}
//@Immutable
class BeforeAfter {
    final Memento before, after;
    /**
     * @param before must be immutable
     * @param after  must be immutable
     */
    public BeforeAfter(Memento before, Memento after) {
        this.before = before;
        this.after = after;
    }
}
