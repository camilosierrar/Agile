package command;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 *  @param <C> Type of executed commands
 * @param <S> Type of stored state (commands or mementos)
 */
public abstract class AbstractConversation<C extends Command, S> implements Conversation<C> {
    protected final Stack<S> undos, redos;
    public AbstractConversation() {
        this.undos= new Stack<S>();
        this.redos= new Stack<S>();
    }
}


