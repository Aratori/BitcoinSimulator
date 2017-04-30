package com.suai.bitcoinsimulator.bitcoin.events;

import com.suai.bitcoinsimulator.simulator.events.Event;
import com.suai.bitcoinsimulator.simulator.nodes.Node;

/**
 * Created by anton on 30.04.17.
 */
public class BlockCheckedEvent extends Event {
    public BlockCheckedEvent(int executionTime, Node execNode)
    {
        super(executionTime, execNode);
    }
}
