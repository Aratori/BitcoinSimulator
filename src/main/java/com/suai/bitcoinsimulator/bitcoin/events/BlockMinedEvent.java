package com.suai.bitcoinsimulator.bitcoin.events;

import com.suai.bitcoinsimulator.simulator.Event;
import com.suai.bitcoinsimulator.simulator.nodes.Node;

/**
 * Created by anton on 30.04.17.
 */
public class BlockMinedEvent extends Event {
    public BlockMinedEvent(int executionTime, Node execNode)
    {
        super(executionTime, execNode);
    }
}
