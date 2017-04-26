package com.suai.bitcoinsimulator.simulator.events;

import com.suai.bitcoinsimulator.simulator.nodes.Node;

public class Event implements java.lang.Comparable<Event> {
    private int executionTime;
    private Node execNode;

    public Event(int executionTime, Node execNode)
    {
        this.executionTime = executionTime;
        this.execNode = execNode;
    }

    public synchronized int getExecutionTime()
    {
        return executionTime;
    }
    public Node getNode()
    {
        return execNode;
    }

    @Override
    public int compareTo(Event e)
    {
        if(e == null)
            return 1;

        return ((Event)this).executionTime - ((Event)e).executionTime;
    }
}
