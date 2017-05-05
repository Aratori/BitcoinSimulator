package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Block;
import com.suai.bitcoinsimulator.simulator.Message;

/**
 * Created by anton on 28.04.17.
 */
public class CheckBlockMessage extends Message {
    private Block block;

    public CheckBlockMessage(String infoMessage, Block block)
    {
        super(infoMessage, 2);
        this.block = block;
    }

    public Block getBlock()
    {
        return block;
    }

    @Override
    public String toString()
    {
        String str = getInfoMessage() + "\n";
        str += block.toString();

        return str;
    }
}
