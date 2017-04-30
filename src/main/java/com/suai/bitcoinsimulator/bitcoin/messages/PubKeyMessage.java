package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.simulator.messages.Message;

/**
 * Created by anton on 28.04.17.
 */
public class PubKeyMessage extends Message {
    private int pubKey;

    public PubKeyMessage(String infoMessage, int pubKey)
    {
        super(infoMessage, 1);
        this.pubKey = pubKey;
    }

    public int getPubKey()
    {
        return pubKey;
    }
}
