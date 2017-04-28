package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Block;
import com.suai.bitcoinsimulator.simulator.messages.Message;

/**
 * Created by anton on 28.04.17.
 */
public class CheckBlockMessage extends Message {
    private Block block;

    public CheckBlockMessage(int senderId, int receiverId, String infoMessage, Block block)
    {
        super(senderId, receiverId, infoMessage, 2);
        this.block = block;
    }

}
