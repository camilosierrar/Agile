package command;

public class ReverseCommand implements Command {
    private Command cmd;

    public ReverseCommand(Command cmd) {
        this.cmd = cmd;
    }

    public void doCommand() {
        cmd.doCommand();
    }

    public void undoCommand() {
        cmd.undoCommand();
    }
}
