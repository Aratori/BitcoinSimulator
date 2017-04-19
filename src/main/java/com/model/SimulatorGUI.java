package com.model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;


import com.model.view.UserView;
import com.model.view.NetworkView;
import com.model.utils.Coord;

public class SimulatorGUI extends JFrame {
	private NetworkView network;
	private JSplitPane split = new JSplitPane();
	private Vector<UserView> users = new Vector();
    private int usersCount;
    private DefaultListModel logModel = new DefaultListModel();
    private JList log = new JList(logModel); 							//	list of log's messages that will show on left side 
    												// app

	public SimulatorGUI(int usersCount) {
	    super("Bitcoin Simulator");
	   	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	   	//place base components
	    Container contentPane = getContentPane();
	    contentPane.setLayout(new BorderLayout());
	    setLocationRelativeTo(null);
	    this.usersCount = usersCount;
	    network = new NetworkView(usersCount);

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

	    network.setBounds(400,400, 300, 300);

	    setBounds(0, 0, 1300, 1000);
	    setVisible(true);
	}

	public void nextStep(int currentTime)
	{
		network.setCurrentTime(currentTime);
		network.repaint();
	}

	public void addSendAnimation(int node1, int node2, int start, int duration)
	{
		network.addPathAnimation(node1, node2, start, duration);
	}

	public synchronized void addLogMessage(String message)
	{
		logModel.addElement(message);
	}
}
