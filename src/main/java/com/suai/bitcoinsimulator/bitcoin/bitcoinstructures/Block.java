package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Created by anton on 26.04.17.
 */
public class Block {
    private BlockHeader blockHeader;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Block(byte[] prevBlockHeaderHash, int currentTime)
    {
        if(prevBlockHeaderHash != null)
            this.blockHeader = new BlockHeader(prevBlockHeaderHash, currentTime);
        else
            this.blockHeader = new BlockHeader(new byte[1], currentTime);
    }


    public int size()
    {
        return transactions.size();
    }

    public byte[] getBlockHeaderHash()
    {
        return blockHeader.getBlockHeaderHash();
    }

    public Transaction getTransaction(int index)
    {
        return transactions.get(index);
    }


    public void addTransaction(Transaction transaction)
    {
        transactions.add(transaction);
    }

    public String getBlockHash()
    {
        String str = blockHeader.getBlockHeaderHash().toString();
        for(int i = 0; i < transactions.size(); i++)
            str += transactions.get(i).getTxId();

        return str;
    }

    public String toString()
    {
        String str = blockHeader.toString();
        str += "\n";
        for(int i = 0; i < transactions.size();  i++)
            str += transactions.get(i).toString() + "\n";

        return str;
    }
}
