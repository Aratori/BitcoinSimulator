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


    public Transaction createRecipientTransaction()
    {
        return new Transaction();
    }

    public Transaction createSenderTransaction()
    {
        return new Transaction();
    }

    public boolean checkBlock(Block block) {
        return true;
    }

    /**
     *
     * @param mode - false - recipient or true - sender
     * @return
     */
    public Block mineBlock(boolean mode)
    {
        Block block = new Block();
        if(mode)
            block.addTransaction(createSenderTransaction());
        else
            block.addTransaction(createRecipientTransaction());

        return new Block();
    }

    public void sendBlock(Block newBlock)
    {

    }
    public void sendPubKey()
    {

    }
}
