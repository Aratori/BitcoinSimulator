package model.nodes;

import model.events.Event;

public abstract class Node {
    public abstract void onEvent(Event event);
}
