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
    private byte[] signature;       //подписанный секретным ключом хэш транзакции

    public TxIn(int prevTxHash, int outputIndex, byte[] signature)
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

    public byte[] getSignature()
    {
        return signature;
    }

    @Override
    public String toString()
    {
        String str = "TxIn:\n";
        str += "Previous transaction hash:\n";
        str += Integer.toString(prevTxHash) + "\n";
        str += "Output index:\n";
        str += outputIndex + "\n";
        str += "Signature:\n";
        str += String.valueOf(signature) + "\n";

        return str;
    }
}
