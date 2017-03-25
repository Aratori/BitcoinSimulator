package model.events;

import model.AbstractSimulator;
import model.nodes.Node;
import model.utils.Comparable;

public class Event extends AbstractEvent implements model.utils.Comparable{
    private int executionTime;
    private Node execNode;

    public Event(int executionTime, Node execNode)
    {
        this.executionTime = executionTime;
        this.execNode = execNode;
    }

    public void execute(AbstractSimulator simulator)
    {
        ;
    }

    public boolean lessThan(Comparable y) {
        Event e = (Event) y;  // Will throw an exception if y is not an Event
        return this.executionTime < e.getExecutionTime();
    }

    public int getExecutionTime()
    {
        return executionTime;
    }
    public Node getNode()
    {
        return execNode;
    }
}
