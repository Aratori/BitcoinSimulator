package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.simulator.Message;

/**
 * Created by anton on 28.05.17.
 */
public class ProduceSignatureMessage extends Message {
    private Transaction tx;
    public ProduceSignatureMessage(String infoMessage, Transaction arbiterTx)
    {
        super(infoMessage, 3);
        tx = arbiterTx;
    }

    public Transaction getArbiterTx()
    {
        return tx;
    }

    @Override
    public String toString()
    {
        String str = getInfoMessage() + "\n";
        str += tx.toString();

        return str;
    }
}
