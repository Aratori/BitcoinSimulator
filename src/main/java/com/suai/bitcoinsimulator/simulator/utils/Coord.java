package com.suai.bitcoinsimulator.simulator.utils;


public class Coord
{
	private int x;
	private int y;

	public Coord(int X, int Y)
	{
		this.x = X;
		this.y = Y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}
}