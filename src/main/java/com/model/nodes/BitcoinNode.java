package com.model.nodes;


import com.model.BitcoinSimulator;
import com.model.nodes.bitcoin.Block;
import com.model.nodes.bitcoin.Transaction;

import java.util.ArrayList;

/**
 * TransactionCreate()
 checkBlock()
 mineBlock()
 sendBlock()
 sendPubKey()
 */
public class BitcoinNode extends User {
    private int privKey;
    private int pubKey;
    private int pubKeyHash;

    public BitcoinNode(int startTime, int interval, BitcoinSimulator bs) {
        super(startTime, interval, bs);
    }

    public Transaction createTransaction() {
        return new Transaction();
    }

    public boolean checkBlock(Block block) {
        return true;
    }

    public Block mineBlock(ArrayList<Transaction> transactions)
    {

        return new Block();
    }

    public void sendBlock(Block newBlock)
    {

    }
    public void sendPubKey()
    {

    }
}
