package command;

public class InitialState implements State {
    
    public void undo(ListOfCommands l) {
        l.undo();
    }

    public void redo(ListOfCommands l) {
        l.redo();
    }
}
