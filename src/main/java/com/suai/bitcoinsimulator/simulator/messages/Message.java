package com.suai.bitcoinsimulator.simulator.messages;

/**
 * Message to send
 */
public class Message {
    private String infoMessage;
    private int senderId;
    private int receiverId;
    private final int type;

    public Message(int senderId, int receiverId, String infoMessage, int type)
    {
        this.infoMessage = infoMessage;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

}
