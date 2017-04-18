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
	private java.util.ArrayList<Coord> usersCoord = new ArrayList<Coord>();

	public NetworkView(int usersCount){
		super();
		this.usersCount = usersCount;
		this.xLoc = 500;
		this.yLoc = 350;
		this.xSize = 100;
		this.ySize = 100;
		setUsersCoords();
		this.setPreferredSize(new Dimension(100, 100));
	} 

	public void setUsersCoords()
	{
		for(int i = 0; i < usersCount; i++)
	    {
	    	int togX = 1;
	    	int togY = 1;
        	//устанавливаем, в каком секторе находится нод
	    	if((360/usersCount * i) > 90 && (360/usersCount * i)<= 180)		//x- y+
	    	{
	    		togX = -1;
	    	} else if((360/usersCount * i) > 180 && (360/usersCount * i)<= 270)
	    	{
	    		togX = -1;
	    		togY = -1;
	    	} else if((360/usersCount * i) > 270 && (360/usersCount * i)<= 360)
	    	{
	    		togY = -1;
	    	}

	    	int userX = xLoc + (int)(radius  * Math.cos((360/usersCount)* i))*togX;
        	int userY = yLoc + (int)(radius  * Math.sin((360/usersCount)* i))*togY;

        	usersCoord.add(new Coord(userX, userY));

        }
	}

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //paint users and connections
        for(int i = 0; i < usersCount; i++)
	    {
        	//paint user
        	g2.setColor(Color.blue);
        	g2.fillOval(usersCoord.get(i).getX()- userSize/2, usersCoord.get(i).getY() - userSize/2, userSize, userSize);
        	g2.setStroke(new BasicStroke(3));
        	g2.setColor(Color.orange);
        	g2.drawOval(usersCoord.get(i).getX() - userSize/2, usersCoord.get(i).getY() - userSize/2, userSize, userSize);
        	//paint lines between users
        	for(int j = 0; j < i; j++)
        	{
        		g2.setColor(Color.red);
        		g2.drawLine(usersCoord.get(j).getX(),
        					usersCoord.get(j).getY(),
        					usersCoord.get(i).getX(),
        					usersCoord.get(i).getY());
        	}
        	
        }


        //repaint();
    }

    public Coord getCenter()
    {
    	return new Coord(xLoc + xSize/2, yLoc + ySize/2);
    }
}