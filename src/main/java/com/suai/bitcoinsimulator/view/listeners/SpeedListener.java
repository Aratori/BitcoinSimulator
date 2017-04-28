package com.suai.bitcoinsimulator.view.listeners;

import com.suai.bitcoinsimulator.simulator.BitcoinSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by anton on 29.04.17.
 */
public class SpeedListener implements ActionListener{
    private BitcoinSimulator bs;
    private int type;

    public SpeedListener(BitcoinSimulator bs, int type)
    {
        this.bs = bs;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        bs.setSpeed(type);
    }
}
