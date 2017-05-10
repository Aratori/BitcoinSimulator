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
    private PublicKey[] publicKeys;
    //1 - sender
    //2 - receiver
    //3 - arbiter

    public TxOut(int satoshisCount, PublicKey pKey, Contract contract)
    {
        this.satoshisCount = satoshisCount;
        this.publicKeys[0] = pKey;
        this.contract = contract;
    }

    public TxOut(int satoshisCount, PublicKey[] publicKeys, Contract contract)
    {
        this.satoshisCount = satoshisCount;
        this.publicKeys = publicKeys;
        this.contract = contract;
    }

    public boolean txMultisigOutputVerification(byte[][] data, byte[][] signature)
    {
        for(int i = 0; i < data.length; i++)
            if(!txOutputVerification(data[i], signature[i]))
                return false;
        return true;
    }

    //test cond
    public boolean txOutputVerification(byte[] data, byte[] signature)
    {
        boolean tog = false;
        try {
            tog = Crypt.verifySig(data, publicKeys[0], signature);
        }catch(Exception ex)
        {
            System.err.println("Public key exception in TxOut");
        }
        return tog;
    }

    //arbitr cond

    public int getSatoshisCount()
    {
        return satoshisCount;
    }

    public PublicKey getPublicKey()
    {
        return publicKeys[0];
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
        String pk = publicKeys[0].toString();
        for(int i = 723; i < 731; i++)
            str += pk.charAt(i);
        return str;
    }
}
