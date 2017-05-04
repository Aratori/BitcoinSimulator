package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Created by anton on 26.04.17.
 */
public class Block {
    private BlockHeader blockHeader = new BlockHeader();
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public int size()
    {
        return transactions.size();
    }

    public Transaction getTransaction(int index)
    {
        return transactions.get(index);
    }


    public void addTransaction(Transaction transaction)
    {
        transactions.add(transaction);
    }

    public String toString()
    {
        String str = "Block:\n";

        for(int i = 0; i < transactions.size(); i++)
            str += transactions.get(i).toString() + "\n";

        return str;
    }
}
