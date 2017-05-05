package com.suai.bitcoinsimulator.simulator;

/**
 * Message to send
 */
public class Message {
    private String infoMessage;
    private final int type;

    public Message(String infoMessage, int type)
    {
        this.infoMessage = infoMessage;
        this.type = type;
    }

    public String getInfoMessage()
    {
        return infoMessage;
    }
    public int getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return infoMessage;
    }
}
