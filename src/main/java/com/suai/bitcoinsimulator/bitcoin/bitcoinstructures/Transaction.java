package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Transaction
 */
public class Transaction {
    private ArrayList<TxIn> inputs = new ArrayList<TxIn>();
    private ArrayList<TxOut> outputs = new ArrayList<TxOut>();

    public void addInput(TxIn input)
    {
        inputs.add(input);
    }

    public void addOutput(TxOut output)
    {
        outputs.add(output);
    }
}
