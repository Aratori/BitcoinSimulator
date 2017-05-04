package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Save information about blockchain
 */
public class BlockChain {
    private ArrayList<Block> blockchain = new ArrayList<Block>();

    public void addBlock(Block block)
    {
        blockchain.add(block);
    }

    /*public ArrayList<Block> getBlockChain()
    {
        return blockchain;
    }*/


    public Block getBlock(int index)
    {
        return blockchain.get(index);
    }

    public int size()
    {
        return blockchain.size();
    }

    @Override
    public String toString()
    {
        String str = "BlockChain:\n";

        for(int i = 0; i < blockchain.size(); i++)
            str += blockchain.get(i).toString() + "\n";

        return str;
    }
}
