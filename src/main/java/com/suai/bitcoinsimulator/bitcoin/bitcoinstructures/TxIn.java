package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.security.Signature;

/**
 * Transaction output
 */
public class TxIn {
    //outpoint
    private int prevTxHash;     //in test mode it number of transaction
    private int outputIndex;    //index of output in prev transaction
    //signature script
    private String signature;       //check, that this transaction can get bitcoin
                                    // from output above described transaction

    public TxIn(int prevTxHash, int outputIndex, String sig)
    {
        this.prevTxHash = prevTxHash;
        this.outputIndex = outputIndex;
        this.signature = signature;
    }

    public int getPrevTxHash()
    {
        return prevTxHash;
    }

    public int getOutputIndex()
    {
        return outputIndex;
    }

    public String getSignature()
    {
        return signature;
    }
}
