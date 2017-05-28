package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.simulator.Message;

/**
 * Created by anton on 28.05.17.
 */
public class ReturnSignatureMessage extends Message {
    private byte[] signature;
    private Transaction arbiterTx;

    public ReturnSignatureMessage(String infoMessage, byte[] signature, Transaction arbiterTx)
    {
        super(infoMessage, 6);
        this.signature = signature;
        this.arbiterTx = arbiterTx;
    }

    public byte[] getSignature()
    {
        return signature;
    }

    public Transaction getArbiterTx()
    {
        return arbiterTx;
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
