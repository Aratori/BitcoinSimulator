package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.security.PublicKey;

import static com.suai.bitcoinsimulator.simulator.utils.Crypt.verifySig;

/**
 * Transaction input
 */
public class TxOut {
    private Contract contract;
    //number of satoshis
    private int satoshisCount;
    //conditions to spent this output
    private PublicKey[] publicKeys;
    //1 - sender
    //2 - receiver
    //3 - arbiter

    public TxOut(int satoshisCount, PublicKey[] publicKeys, Contract contract)
    {
        this.satoshisCount = satoshisCount;
        this.publicKeys = publicKeys;
        this.contract = contract;
    }

    public boolean txMultisigOutputVerification(byte[] data, byte[][] signatures)
    {
        //проверяем каждую сигнатуру
        for(int i = 0; i < signatures.length; i++)
        {
            boolean verified = false;
            //идем по ключам, если хоть один дает верный результат, то идем дальше
            //иначе ошибка
            for(int j = 0; j < publicKeys.length; j++)
            {
                try {
                    if(Crypt.verifySig(data, publicKeys[j], signatures[i]))
                        verified = true;
                }catch(Exception ex)
                {
                    System.err.println("Public key exception in TxOut");
                }
            }
            if(!verified)
                return false;
        }

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

    /**
     * Определенный публичный ключ по индексу
     */
    public PublicKey getCertainPublicKey(int index)
    {
        return publicKeys[index];
    }

    public Contract getContract()
    {
        return contract;
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
        str += "Public Keys: ";
        for(int i = 0; i < publicKeys.length; i++) {
            String pk = publicKeys[i].toString();
            for (int j = 723; j < 731; j++)
                str += pk.charAt(j);
            str += "; ";
        }
        return str;
    }
}
