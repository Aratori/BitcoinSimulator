package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.simulator.Message;

/**
 * Created by anton on 28.05.17.
 */
public class ArbiterResolveMessage extends Message {
    private Transaction arbiterTx;
    private byte[] signature;
    public ArbiterResolveMessage(String infoMessage, Transaction arbiterTx, byte[] signature)
    {
        super(infoMessage, 4);
        this.arbiterTx = arbiterTx;
        this.signature = signature;
    }

    public Transaction getArbiterTx()
    {
        return arbiterTx;
    }
    public byte[] getSignature()
    {
        return signature;
    }

    @Override
    public String toString()
    {
        String str = getInfoMessage() + "\n";
        str += arbiterTx.toString();
        str += "Signature:\n";
        for(int i = 0; i < 8 && i < signature.length; i++)
            str += String.format("%x", signature[i]);

        return str;
    }
}
