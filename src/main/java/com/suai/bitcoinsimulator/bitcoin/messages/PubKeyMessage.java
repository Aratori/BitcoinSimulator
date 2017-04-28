package com.suai.bitcoinsimulator.bitcoin.messages;

import com.suai.bitcoinsimulator.simulator.messages.Message;

/**
 * Created by anton on 28.04.17.
 */
public class PubKeyMessage extends Message {
    private int pubKey;

    public PubKeyMessage(int senderId, int receiverId, String infoMessage, int pubKey)
    {
        super(senderId, receiverId, infoMessage, 1);
        this.pubKey = pubKey;
    }


}
