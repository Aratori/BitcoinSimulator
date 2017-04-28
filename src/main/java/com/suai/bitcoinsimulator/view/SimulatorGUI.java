package com.suai.bitcoinsimulator.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.*;
import java.util.*;


import com.suai.bitcoinsimulator.simulator.BitcoinSimulator;
import com.suai.bitcoinsimulator.view.UserView;
import com.suai.bitcoinsimulator.view.NetworkView;
import com.suai.bitcoinsimulator.view.ViewManager;
import com.suai.bitcoinsimulator.view.listeners.SpeedListener;

public class SimulatorGUI extends JFrame implements ViewManager {
	private final BitcoinSimulator bs;
	private NetworkView network;
	private JSplitPane split = new JSplitPane();
	private JMenuBar menu = new JMenuBar();
	private Vector<UserView> users = new Vector();
    private int usersCount;
    private DefaultListModel logModel = new DefaultListModel();
    private JList log = new JList(logModel); 							//	list of log's messages that will show on left side 
  	private PauseButton pauseButton;
    												// app

	public SimulatorGUI(BitcoinSimulator bs, int usersCount) {
	    super("Bitcoin Simulator");
	   	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.bs = bs;

	   	//place base components
	    Container contentPane = getContentPane();
	    contentPane.setLayout(new BorderLayout());
	    setLocationRelativeTo(null);
	    this.usersCount = usersCount;
	    network = new NetworkView(this, usersCount);



	    //set log scroll pane for right side of SplitPane
	    log.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    log.setSelectedIndex(0);
	    JScrollPane logScrollPane = new JScrollPane(log);

	    //place split view
	   	split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setOneTouchExpandable(true);
        split.setDividerLocation(300);

	   	split.setRightComponent(network);
	   	split.setLeftComponent(logScrollPane);
	   	logModel.addElement("Log started:");

	    //contentPane.add(network);
	    contentPane.add(split);
	    //addLogMessage("Log started:");
		//set toolbar
		pauseButton = new PauseButton(bs, 20, 20);
		pauseButton.setSize(20,20);
		//pauseButton.setMaximumSize(new Dimension(100, 50));
		//menu.add(exit);

		//toolBar.add(pauseButton);
		//menu.setMaximumSize(new Dimension(getWidth(), 50));
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JMenu speed = new JMenu("Speed");
		JMenuItem speed1 = new JMenuItem("Speed 1");
		JMenuItem speed2 = new JMenuItem("Speed 2");
		JMenuItem speed3 = new JMenuItem("Speed 3");
		speed1.addActionListener(new SpeedListener(bs, 1));
		speed2.addActionListener(new SpeedListener(bs, 2));
		speed3.addActionListener(new SpeedListener(bs, 3));
		speed.add(speed1);
		speed.add(speed2);
		speed.add(speed3);
		menu.add(speed);
		menu.add(exit);
		menu.add(pauseButton);
		contentPane.add(menu, BorderLayout.NORTH);

	    network.setBounds(400,400, 300, 300);

	    setBounds(0, 0, 1000, 1000);
	    setVisible(true);
	}

	public void nextStep(int currentTime)
	{
		network.setCurrentTime(currentTime);
		network.repaint();
		pauseButton.repaint();
	}

	public void addSendAnimation(int node1, int node2, int start, int duration)
	{
		network.addPathAnimation(node1, node2, start, duration);
	}

	public BitcoinSimulator getSimulator()
	{
		return bs;
	}

	public synchronized void addLogMessage(String message)
	{
		logModel.addElement(message);
	}
}
