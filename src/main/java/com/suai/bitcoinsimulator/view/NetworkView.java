package com.suai.bitcoinsimulator.view;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	private BufferedImage desktop;
	private BufferedImage mail;
	private SimulatorGUI gui;
	private java.util.ArrayList<Coord> usersCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Coord> pathAnimationCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Integer>   startEndAnimation = new ArrayList<Integer>();

	public NetworkView(SimulatorGUI gui, int usersCount){
		super();
		this.gui = gui;
		this.usersCount = usersCount;
		this.xLoc = 500;
		this.yLoc = 350;
		this.xSize = 100;
		this.ySize = 100;
		this.currentTime = 0;
		this.setSize(700, 700);
		this.setBackground(Color.white);
		this.setLocation(300, 0);
		try {
			this.desktop = ImageIO.read(new File("img/desktopSmall.png"));
			this.mail = ImageIO.read(new File("img/message.png"));
		}catch(IOException ex)
		{
			System.out.println("Desktop image download failed");
		}
		setUsersCoords();
		//this.setPreferredSize(new Dimension(100, 100));
	} 

	public void setUsersCoords()
	{
		for(int i = 0; i < usersCount; i++)
		{
			double angle = (360/usersCount) * i;
	    	double x = 0.5 * getWidth();
	    	double y = 0.5 * getHeight();
	    	double r = 0.75 * Math.min(x,y);
	    	double sin = Math.sin(Math.toRadians(angle));
	    	double cos = Math.cos(Math.toRadians(angle));
	    	x += r* cos;
	    	y += r* sin;

        	usersCoord.add(new Coord((int)x, (int)y));
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
			g2.drawImage(desktop,usersCoord.get(i).getX() - userSize/2, usersCoord.get(i).getY()-userSize/2,null);
        	//paint lines between users
        	for(int j = 0; j < i; j++)
        	{
        		g2.setColor(Color.gray);
        		g2.drawLine(usersCoord.get(j).getX(),
        					usersCoord.get(j).getY(),
        					usersCoord.get(i).getX(),
        					usersCoord.get(i).getY());
        	}
        	
        }

        //paint path animation
        for(int i = 0; i < pathAnimationCoord.size(); i+=2)
        {
        	currentTime = gui.getSimulator().getCurrentTime();
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

			g2.drawImage(mail,pathAnimationCoord.get(i).getX() + (lenNewX)*togX - messageSize/2, pathAnimationCoord.get(i).getY() + (lenNewY)*togY - messageSize/2, messageSize, messageSize, null );
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