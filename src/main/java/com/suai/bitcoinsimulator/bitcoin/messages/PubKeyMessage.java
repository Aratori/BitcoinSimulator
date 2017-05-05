package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.simulator.Message;

import java.security.PublicKey;

/**
 * Created by anton on 28.04.17.
 */
public class PubKeyMessage extends Message {
    private PublicKey pubKey;

    public PubKeyMessage(String infoMessage, PublicKey pubKey)
    {
        super(infoMessage, 1);
        this.pubKey = pubKey;
    }

    public PublicKey getPubKey()
    {
        return pubKey;
    }

    @Override
    public String toString()
    {
        String str = getInfoMessage() + "\n";
        str += pubKey.toString();

        return str;
    }
}
