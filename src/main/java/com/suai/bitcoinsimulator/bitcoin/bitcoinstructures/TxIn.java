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
    private byte[][] signatures;       //подписанный секретным ключом хэш транзакции

    public TxIn(byte[] prevTxHash, int outputIndex, byte[][] signatures)
    {
        this.prevTxHash = prevTxHash;
        this.outputIndex = outputIndex;
        this.signatures = signatures;
    }

    public byte[] getPrevTxHash()
    {
        return prevTxHash;
    }

    public int getOutputIndex()
    {
        return outputIndex;
    }

    public byte[] getSignature(int index)
    {
        return signatures[index];
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
        str += "Signatures: ";
        for(int j = 0; j < signatures.length; j++) {
            for (int i = 0; i < 8 && i < signatures[0].length; i++)
                str += String.format("%x", signatures[j][i]);
            str += "; ";
        }
        return str;
    }
}
