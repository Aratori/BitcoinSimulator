package com.suai.bitcoinsimulator.view;

import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;

import com.suai.bitcoinsimulator.simulator.utils.Coord;

public class NetworkView extends JPanel
{
	private int xLoc;
	private int yLoc;
	private int xSize;
	private int ySize;
	private int usersCount;
	private int radius = 300;
	private int userSize = 80;
	private int messageSize = 30;
	private int currentTime;
	private java.util.ArrayList<Coord> usersCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Coord> pathAnimationCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Integer>   startEndAnimation = new ArrayList<Integer>();

	public NetworkView(int usersCount){
		super();
		this.usersCount = usersCount;
		this.xLoc = 500;
		this.yLoc = 350;
		this.xSize = 100;
		this.ySize = 100;
		this.currentTime = 0;
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
        	System.out.println(" x " + userX + " y " + userY);
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

        //paint path animation
        for(int i = 0; i < pathAnimationCoord.size(); i+=2)
        {
        	if((currentTime - startEndAnimation.get(i + 1)) > 1)
        		continue;
        	//System.out.println("Anim size " + (pathAnimationCoord.size()/2));
        	int lenXOrig;
        	int lenYOrig;
        	int lenNewX;
        	int lenNewY;
        	int togX = -1;
        	int togY = -1;
        	//System.out.println("User1 x " + pathAnimationCoord.get(i).getX() + " User1 y " + pathAnimationCoord.get(i).getY());
        	if(pathAnimationCoord.get(i).getX() < pathAnimationCoord.get(i+1).getX())
        	{
        		lenXOrig = pathAnimationCoord.get(i+1).getX() - pathAnimationCoord.get(i).getX();
        		togX = 1;
        	}
        	else
        		lenXOrig = pathAnimationCoord.get(i).getX() - pathAnimationCoord.get(i+1).getX();

        	if(pathAnimationCoord.get(i).getY() < pathAnimationCoord.get(i+1).getY())
        	{
        		lenYOrig = pathAnimationCoord.get(i+1).getY() - pathAnimationCoord.get(i).getY();
        		togY = 1;
        	}
        	else
        		lenYOrig = pathAnimationCoord.get(i).getY() - pathAnimationCoord.get(i+1).getY();
        	//System.out.println("X orig len " + lenXOrig + " Y orig len " + lenYOrig);
        	//отношение времени == отношению старых сторон к новым сторонам
        	double rev = (double)((double)currentTime - (double)startEndAnimation.get(i))/(double)((double)startEndAnimation.get(i + 1) - (double)startEndAnimation.get(i));
        	//System.out.println("rev " + rev);
        	lenNewX = (int)(rev * (double)(lenXOrig));
        	lenNewY = (int)(rev * (double)(lenYOrig));
        	//System.out.println("Len new x " + lenNewX + " y " + lenNewY);

        	g2.setColor(Color.green);
        	//System.out.println("X new : " + lenXOrig + " Y new : " + lenYOrig);
        	g2.drawOval(pathAnimationCoord.get(i).getX() + (lenNewX)*togX,pathAnimationCoord.get(i).getY() + (lenNewY)*togY, messageSize, messageSize);
        }


        repaint();
    }

    public Coord getCenter()
    {
    	return new Coord(xLoc + xSize/2, yLoc + ySize/2);
    }

    public void setCurrentTime(int currentTime)
    {
    	this.currentTime = currentTime;
    }

    public void addPathAnimation(int node1, int node2, int start, int duration)
    {
    	System.out.println("Path anim set: start " + start + " end " + (start+duration))	;
    	pathAnimationCoord.add(usersCoord.get(node1));
    	pathAnimationCoord.add(usersCoord.get(node2));
    	startEndAnimation.add(start);
    	startEndAnimation.add(start+duration);
    }

}