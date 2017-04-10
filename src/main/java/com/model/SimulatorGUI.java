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
	private Vector<UserView> users = new Vector();
    private int usersCount;

	public SimulatorGUI(int usersCount) {
	    super("Bitcoin Simulator");
	   	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container contentPane = getContentPane();
	    contentPane.setLayout(new BorderLayout());
	    setLocationRelativeTo(null);
	    this.usersCount = usersCount;
	    network = new NetworkView(usersCount);

	    contentPane.add(network);

	    network.setBounds(200,200, 100, 100);

	    setBounds(0, 0, 1000, 1000);
	    setVisible(true);

	}

	/*public UserView createUserView(int userId)
    {
        //calculate position and coordinates
        usersCount++;
        double userX = network.getCenter().getX() + radius * Math.cos(360/usersCount);
        double userY = network.getCenter().getX() + radius * Math.sin(360/usersCount);
        UserView newUser = new UserView(network.getCenter().getX() + radius, network.getCenter().getX() + radius, userId);
        users.addElement(newUser);

        newUser.setSize(100,100);
        newUser.setLocation((int)userX, (int)userY);
        getContentPane().add(newUser);
        validate();
        newUser.setVisible(true);
        setVisible(true);

        return newUser;
    }
*/
}
