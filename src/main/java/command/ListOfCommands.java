package command;

import java.util.LinkedList;

public class ListOfCommands {

    private LinkedList<Command> l;
    private int i;

    public ListOfCommands() {
        i = -1;
        l = new LinkedList<Command>();
    }

    public void add(Command c) {
        i++;
        l.add(i,c);
        //c.execute();
    }

    public void undo() {
        if(i>=0) {
            //l.get(i).undoCommand(); 
            i--;
        }
    }

    public void redo() {
        i++;
       // l.get(i).execute(); 
    }
}
