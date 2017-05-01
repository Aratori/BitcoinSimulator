package com.suai.bitcoinsimulator.view;

import com.suai.bitcoinsimulator.bitcoin.BitcoinSimulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by anton on 27.04.17.
 */
public class PauseButton extends JMenuItem implements ActionListener{
    private boolean paused;
    ImageIcon pauseIcon = new ImageIcon("pause2.png");
    ImageIcon resumeIcon = new ImageIcon("resume2.png");
    private BitcoinSimulator bs;

    public PauseButton(BitcoinSimulator bs, int width, int height)
    {
        super();
        this.bs = bs;
        //pauseButton = new JButton();
        //pauseButton.setPreferredSize(new Dimension(width, height));
        //add(pauseButton, BorderLayout.CENTER);
        addActionListener(this);
        paused = false;

        setIcon(pauseIcon);
        setText("PAUSE");
        //pauseButton.setSize(width, height);
        repaint();
    }

    public void actionPerformed(ActionEvent event)
    {
        if(paused)
        {
            paused = false;
            bs.resume();
            setIcon(pauseIcon);
            setText("PAUSE");
        }
        else
        {
            paused = true;
            bs.pause();
            setIcon(resumeIcon);
            setText("RESUME");
        }
    }
}
