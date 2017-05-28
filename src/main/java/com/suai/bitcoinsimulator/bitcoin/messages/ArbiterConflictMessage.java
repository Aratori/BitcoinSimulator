package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.simulator.Message;

/**
 * Created by anton on 28.05.17.
 */
public class ArbiterConflictMessage  extends Message{
    private Transaction arbiterTx;
    private int arbId;
    public ArbiterConflictMessage(String infoMessage, Transaction arbiterTx, int arbId)
    {
        super(infoMessage, 5);
        this.arbiterTx = arbiterTx;
        this.arbId = arbId;
    }

    public Transaction getArbiterTx()
    {
        return arbiterTx;
    }

    public int getArbId()
    {
        return arbId;
    }
    @Override
    public String toString()
    {
        String str = getInfoMessage() + "\n";
        str += arbiterTx.toString();
        str += "Arbiter id: " + arbId;

        return str;
    }

}
