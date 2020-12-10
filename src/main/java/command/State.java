package command;

public interface State {
    public default void undo() {};
    public default void redo() {};
    public default void addRequest() {};
    public default void removeRequest() {};
    public default void modifyOrder(){};
}
