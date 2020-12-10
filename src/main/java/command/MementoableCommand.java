package command;

public interface MementoableCommand extends Command {
    Memento takeSnapshot();
}
