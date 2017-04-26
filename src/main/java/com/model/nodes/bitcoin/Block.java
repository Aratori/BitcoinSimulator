package com.model.nodes.bitcoin;

import java.util.ArrayList;

/**
 * Created by anton on 26.04.17.
 */
public class Block {
    private String blockHeader;
    private ArrayList<Transaction> transactions;

    public void addTransaction(Transaction transaction)
    {
        transactions.add(transaction);
    }
}
