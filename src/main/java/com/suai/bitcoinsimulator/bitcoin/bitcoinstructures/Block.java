package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Created by anton on 26.04.17.
 */
public class Block {
    private BlockHeader blockHeader = new BlockHeader();
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public void addTransaction(Transaction transaction)
    {
        transactions.add(transaction);
    }
}
