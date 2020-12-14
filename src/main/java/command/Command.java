package command;

public interface Command {
    /**
     * Default method for executing a command is doing nothing
     */
    public default void execute() {};
}
