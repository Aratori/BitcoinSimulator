package com.suai.bitcoinsimulator.simulator.nodes;

import com.suai.bitcoinsimulator.simulator.Event;

public abstract class Node {
    public abstract void onEvent(Event event);
}
