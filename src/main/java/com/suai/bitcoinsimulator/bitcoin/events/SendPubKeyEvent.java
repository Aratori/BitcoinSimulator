package com.suai.bitcoinsimulator.bitcoin.events;

import com.suai.bitcoinsimulator.simulator.Event;
import com.suai.bitcoinsimulator.simulator.nodes.Node;

/**
 * Created by anton on 01.05.17.
 */
    public class SendPubKeyEvent extends Event {
        public SendPubKeyEvent(int executionTime, Node execNode)
        {
            super(executionTime, execNode);
        }
    }

