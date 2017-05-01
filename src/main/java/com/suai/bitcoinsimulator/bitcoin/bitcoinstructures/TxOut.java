package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

/**
 * Transaction input
 */
public class TxOut {
    //number of satoshis
    private int satoshisCount;
    //conditions to spent this output
    private int cond;

    public TxOut(int satoshisCount, int cond)
    {
        this.satoshisCount = satoshisCount;
        this.cond = cond;
    }

    //test cond
    public boolean txOutputVerification(int privKey)
    {
        return privKey == (cond + 42);
    }

    public int getSatoshisCount()
    {
        return satoshisCount;
    }

}
