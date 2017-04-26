package com.suai.bitcoinsimulator.bitcoin;

/**
 * Transaction output
 */
public class TxIn {
    //outpoint
    private int prevTxHash;
    private int outputIndex;
    //signature script
    private int sig;
}
