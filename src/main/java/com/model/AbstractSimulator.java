package com.model;

import com.model.events.AbstractEvent;
import com.model.utils.OrderedSet;
import com.model.events.Event;
import java.util.concurrent.PriorityBlockingQueue;

public class AbstractSimulator {
    protected int time = 0;
    protected PriorityBlockingQueue<Event> events;
    public int getTime()
    {
        return time;
    }
    public void insert(AbstractEvent e) {
        //events.add(e);
    }

    AbstractEvent cancel(AbstractEvent e)  {
        throw new java.lang.RuntimeException("Method not implemented");
    }
}
