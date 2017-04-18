package com.model.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.model.utils.Coord;

public class UserView extends JPanel
{
	private int xLoc;
	private int yLoc;
	private int xSize;
	private int ySize;
	private int userId;

	public UserView(int xLoc, int yLoc){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.xSize = 80;
		this.ySize = 80;
		this.userId = userId;
		this.setPreferredSize(new Dimension((int)xSize, (int)ySize));
	} 

	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.blue);
        g2.fillOval((int)xLoc, (int)yLoc, (int)xSize, (int)ySize);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.white);
        g2.drawOval((int)xLoc, (int)yLoc, (int)xSize, (int)ySize);
        repaint();
    }

    public Coord getCenter()
    {
    	return new Coord(xLoc + xSize/2, yLoc + ySize/2);
    }
}