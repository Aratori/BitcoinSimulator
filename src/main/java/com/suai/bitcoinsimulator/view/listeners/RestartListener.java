package com.suai.bitcoinsimulator.view.listeners;

import com.suai.bitcoinsimulator.simulator.Simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by anton on 09.05.17.
 */
public class RestartListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Simulator.restartSimulator("config.conf");
    }
}
