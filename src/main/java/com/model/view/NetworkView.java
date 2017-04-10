package com.model.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;

import com.model.utils.Coord;

public class NetworkView extends JPanel
{
	private int xLoc;
	private int yLoc;
	private int xSize;
	private int ySize;
	private int usersCount;
	private int radius = 300;
	private int userSize = 80;

	public NetworkView(int usersCount){
		super();
		this.usersCount = usersCount;
		this.xLoc = 200;
		this.yLoc = 200;
		this.xSize = 100;
		this.ySize = 100;
		this.setPreferredSize(new Dimension(100, 100));
	} 

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.orange);
        g2.fillOval(xLoc, yLoc, xSize, ySize);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.red);
        g2.drawOval(xLoc, yLoc, xSize, ySize);
        
        for(int i = 0; i < usersCount; i++)
	    {
	    	int userX = xLoc +(int)(radius  * Math.sin((360/usersCount) * i));
        	int userY = yLoc + (int)(radius  * Math.cos((360/usersCount)* i));
        	g2.setColor(Color.blue);
        	g2.fillOval(userX, userY, userSize, userSize);
        	g2.setStroke(new BasicStroke(3));
        	g2.setColor(Color.orange);
        	g2.drawOval(userX, userY, userSize, userSize);
        	g2.setColor(Color.red);
        	g2.drawLine(xLoc+xSize/2, yLoc+ySize/2, userX + userSize/2, userY + userSize/2);
        }


        //repaint();
    }

    public Coord getCenter()
    {
    	return new Coord(xLoc + xSize/2, yLoc + ySize/2);
    }
}