package com.model.nodes;

import com.model.events.Event;

public abstract class Node {
    public abstract void onEvent(Event event);
}
