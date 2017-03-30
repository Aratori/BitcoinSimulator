package com.model;

import com.model.events.AbstractEvent;
import com.model.utils.OrderedSet;

public class AbstractSimulator {
    protected int time = 0;
    protected OrderedSet events;
    public int getTime()
    {
        return time;
    }
    public void insert(AbstractEvent e) {
        events.insert(e);
    }

    AbstractEvent cancel(AbstractEvent e)  {
        throw new java.lang.RuntimeException("Method not implemented");
    }
}
