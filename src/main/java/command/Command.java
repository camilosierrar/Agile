package command;

public interface Command {
    
    public default void doCommand() {};
    public default void undoCommand() {};
}
