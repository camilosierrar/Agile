package command;

public interface MementoableCommand extends Command {
    /**
     * Take a snapshot of current state of the system
     * @return Memento object containing current state of the system
     */
    Memento takeSnapshot();
}
