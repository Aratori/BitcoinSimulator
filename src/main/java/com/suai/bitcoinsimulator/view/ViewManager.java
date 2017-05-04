package com.suai.bitcoinsimulator.view;

import com.suai.bitcoinsimulator.simulator.messages.Message;

/**
 * Created by anton on 25.04.17.
 */
public interface ViewManager {
    void addSendAnimation(int node1, int node2, int start, int duration, Message message);
    void addLogMessage(String message);
    void setUserDebugInfo(int userId);
    void setMessageDebugInfo(Message message);
}
