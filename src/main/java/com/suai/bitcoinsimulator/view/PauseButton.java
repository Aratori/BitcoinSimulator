package com.suai.bitcoinsimulator.view;

import com.suai.bitcoinsimulator.simulator.BitcoinSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by anton on 27.04.17.
 */
public class PauseButton extends JPanel implements ActionListener{
    private JButton  pauseButton;
    private boolean paused;
    ImageIcon pauseIcon = new ImageIcon("pause.png");
    ImageIcon resumeIcon = new ImageIcon("resume.png");
    private BitcoinSimulator bs;

    public PauseButton(BitcoinSimulator bs, int width, int height)
    {
        super();
        this.bs = bs;
        pauseButton = new JButton();
        //pauseButton.setPreferredSize(new Dimension(width, height));
        add(pauseButton, BorderLayout.CENTER);
        pauseButton.addActionListener(this);
        paused = false;

        pauseButton.setIcon(pauseIcon);
        pauseButton.setSize(width, height);
        repaint();
    }

    public void actionPerformed(ActionEvent event)
    {
        if(paused)
        {
            paused = false;
            bs.resume();
            pauseButton.setIcon(pauseIcon);
        }
        else
        {
            paused = true;
            bs.pause();
            pauseButton.setIcon(resumeIcon);
        }
    }
}
