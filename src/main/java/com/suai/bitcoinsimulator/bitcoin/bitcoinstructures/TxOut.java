package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.security.PublicKey;

import static com.suai.bitcoinsimulator.simulator.utils.Crypt.verifySig;

/**
 * Transaction input
 */
public class TxOut {
    //number of satoshis
    private int satoshisCount;
    //conditions to spent this output
    private PublicKey publicKey;

    public TxOut(int satoshisCount, PublicKey pKey)
    {
        this.satoshisCount = satoshisCount;
        this.publicKey = pKey;
    }

    //test cond
    public boolean txOutputVerification(byte[] data, byte[] signature)
    {
        boolean tog = false;
        try {
            tog = Crypt.verifySig(data, publicKey, signature);
        }catch(Exception ex)
        {
            System.err.println("Public key exception in TxOut");
        }
        return tog;
    }

    public int getSatoshisCount()
    {
        return satoshisCount;
    }

    @Override
    public String toString()
    {
        String str = "TxOut:\n";
        str += "SatoshisCount: " + satoshisCount + "\n";
        str += "Output condition type " + 1;

        return str;
    }
}
