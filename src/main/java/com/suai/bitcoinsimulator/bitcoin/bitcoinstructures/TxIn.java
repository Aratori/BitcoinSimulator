package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.security.Signature;

/**
 * Transaction output
 */
public class TxIn {
    //outpoint
    private byte[] prevTxHash;     //in test mode it number of transaction
    private int outputIndex;    //index of output in prev transaction
    //signature script
    private byte[] signature;       //подписанный секретным ключом хэш транзакции

    public TxIn(byte[] prevTxHash, int outputIndex, byte[] signature)
    {
        this.prevTxHash = prevTxHash;
        this.outputIndex = outputIndex;
        this.signature = signature;
    }

    public byte[] getPrevTxHash()
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
        String str = "TxIn:: ";
        str += "Previous Transaction Hash: ";
        for(int i = 0; i < 8 && i < prevTxHash.length; i++)
            str += String.format("%x", prevTxHash[i]);
        str += "; ";
        str += "Output Index: " + String.format("%+3d", outputIndex);
        str += "; ";
        str += "Signature: ";
        for(int i = 0; i < 8 && i < signature.length; i++)
            str += String.format("%x", signature[i]);
        return str;
    }
}
