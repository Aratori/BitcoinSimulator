package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.security.PublicKey;

import static com.suai.bitcoinsimulator.simulator.utils.Crypt.verifySig;

/**
 * Transaction input
 */
public class TxOut {
    public Contract contract;
    //number of satoshis
    private int satoshisCount;
    //conditions to spent this output
    private PublicKey publicKey;

    public TxOut(int satoshisCount, PublicKey pKey, Contract contract)
    {
        this.satoshisCount = satoshisCount;
        this.publicKey = pKey;
        this.contract = contract;
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

    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    @Override
    public String toString()
    {
        String str = "TxOut:: ";
        str += "Satoshi's Count: " + String.format("%03d",satoshisCount);
        str += "; ";
        str += "Contract Type: " + contract.toString();
        //publicKey
        str += "; ";
        str += "Public Key: ";
        String pk = publicKey.toString();
        for(int i = 723; i < 731; i++)
            str += pk.charAt(i);
        return str;
    }
}
