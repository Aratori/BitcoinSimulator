package com.suai.bitcoinsimulator.view;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.security.PublicKey;
import java.util.*;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.Message;
import com.suai.bitcoinsimulator.simulator.utils.Coord;
import com.suai.bitcoinsimulator.view.listeners.NetworkMouseListener;

public class NetworkView extends JPanel {
	private int usersCount;
	private int userSize = 80;
	private int messageSize = 30;
	private int currentTime;
	private BufferedImage desktop;
	private BufferedImage mail;
	private SimulatorGUI gui;
	private java.util.ArrayList<String> usersStrings = new ArrayList<String>();
	private java.util.ArrayList<Coord> usersCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Coord> messagesCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Message> messagesInfo = new ArrayList<Message>();
	private java.util.ArrayList<Integer> sendersId = new ArrayList<Integer>();
	private java.util.ArrayList<Coord> pathAnimationCoord = new ArrayList<Coord>();
	private java.util.ArrayList<Integer> startEndAnimation = new ArrayList<Integer>();
	private java.util.ArrayList<Color> usersColors = new ArrayList<Color>();
	private NetworkMouseListener mouseListener;

	public NetworkView(SimulatorGUI gui, int usersCount) {
		super();
		this.gui = gui;
		this.usersCount = usersCount;
		//set colors
		Random rand = new Random(45);
		for (int i = 0; i < usersCount; i++)
			usersColors.add(new Color(
					rand.nextInt(255),
					rand.nextInt(255),
					rand.nextInt(255)
			));
		this.currentTime = 0;
		this.setSize(700, 550);
		this.setBackground(Color.white);
		this.setLocation(300, 0);
		try {
			this.desktop = ImageIO.read(new File("img/desktopSmall.png"));
			this.mail = ImageIO.read(new File("img/messageSmall.png"));
		} catch (IOException ex) {
			System.out.println("Desktop image download failed");
		}
		setUsersCoords();
		this.mouseListener = new NetworkMouseListener(gui, this, usersCoord, userSize, messageSize);
		this.addMouseListener(mouseListener);
	}

	public void setUsersStrings() {
		String str = null;
		for (int i = 0; i < usersCount; i++) {
			BitcoinNode node = (BitcoinNode) (gui.getSimulator().getNetwork().getUser(i));
			str = "NODE " + i + "; PUBLIC KEY ";
			String pk = node.getPublicKey().toString();
			for (int j = 723; j < 731; j++) {
				str += pk.charAt(j);
			}
			usersStrings.add(str);
		}
	}

	public void setUsersCoords() {
		for (int i = 0; i < usersCount; i++) {
			double angle = (360 / usersCount) * i;
			double x = 0.5 * getWidth();
			double y = 0.5 * getHeight();
			double r = 0.75 * Math.min(x, y);
			double sin = Math.sin(Math.toRadians(angle));
			double cos = Math.cos(Math.toRadians(angle));
			x += r * cos;
			y += r * sin;

			usersCoord.add(new Coord((int) x, (int) y));
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (currentTime != 0)
			setUsersStrings();
		//paint users and connections
		for (int i = 0; i < usersCount; i++) {
			if (currentTime != 0) {
				g2.setColor(usersColors.get(i));
				g2.drawString(usersStrings.get(i), usersCoord.get(i).getX() - userSize, usersCoord.get(i).getY() - userSize / 2);
			}
			//paint background
			g2.fillRect(usersCoord.get(i).getX() - userSize / 2, usersCoord.get(i).getY() - userSize / 2, userSize, userSize);
			//paint user
			g2.drawImage(desktop, usersCoord.get(i).getX() - userSize / 2, usersCoord.get(i).getY() - userSize / 2, userSize, userSize, null);
			//paint lines between users
			for (int j = 0; j < i; j++) {
				g2.setColor(Color.gray);
				g2.drawLine(usersCoord.get(j).getX(),
						usersCoord.get(j).getY(),
						usersCoord.get(i).getX(),
						usersCoord.get(i).getY());
			}

		}
		boolean lastDeleted = false;
		ArrayList<Integer> deleteThis = new ArrayList<Integer>();
		//paint path animation
		for (int i = 0, j = 0; i < pathAnimationCoord.size(); i += 2, j++) {
			currentTime = gui.getSimulator().getCurrentTime();
			//если сообщение прошло свой путь убираем из списка
			if ((currentTime - startEndAnimation.get(i + 1)) > 0) {
				deleteThis.add(j);
				continue;
			}
			//System.out.println("Anim size " + (pathAnimationCoord.size()/2));
			int lenXOrig;
			int lenYOrig;
			int lenNewX;
			int lenNewY;
			int togX = -1;
			int togY = -1;
			//System.out.println("User1 x " + pathAnimationCoord.get(i).getX() + " User1 y " + pathAnimationCoord.get(i).getY());
			if (pathAnimationCoord.get(i).getX() < pathAnimationCoord.get(i + 1).getX()) {
				lenXOrig = pathAnimationCoord.get(i + 1).getX() - pathAnimationCoord.get(i).getX();
				togX = 1;
			} else
				lenXOrig = pathAnimationCoord.get(i).getX() - pathAnimationCoord.get(i + 1).getX();

			if (pathAnimationCoord.get(i).getY() < pathAnimationCoord.get(i + 1).getY()) {
				lenYOrig = pathAnimationCoord.get(i + 1).getY() - pathAnimationCoord.get(i).getY();
				togY = 1;
			} else
				lenYOrig = pathAnimationCoord.get(i).getY() - pathAnimationCoord.get(i + 1).getY();
			//System.out.println("X orig len " + lenXOrig + " Y orig len " + lenYOrig);
			//отношение времени == отношению старых сторон к новым сторонам
			double rev = (double) ((double) currentTime - (double) startEndAnimation.get(i)) / (double) ((double) startEndAnimation.get(i + 1) - (double) startEndAnimation.get(i));
			//System.out.println("rev " + rev);
			lenNewX = (int) (rev * (double) (lenXOrig));
			lenNewY = (int) (rev * (double) (lenYOrig));
			//paint background
			g2.setColor(usersColors.get(sendersId.get(i / 2)));
			g2.fillRect(pathAnimationCoord.get(i).getX() + (lenNewX) * togX - messageSize / 2, pathAnimationCoord.get(i).getY() + (lenNewY) * togY - messageSize / 2, messageSize, messageSize);
			//paint message and put new coordinates
			g2.drawImage(mail, pathAnimationCoord.get(i).getX() + (lenNewX) * togX - messageSize / 2, pathAnimationCoord.get(i).getY() + (lenNewY) * togY - messageSize / 2, messageSize, messageSize, null);
			messagesCoord.get(j).setX(pathAnimationCoord.get(i).getX() + (lenNewX) * togX);
			messagesCoord.get(j).setY(pathAnimationCoord.get(i).getY() + (lenNewY) * togY);
		}
		deleteClosedAnimation(deleteThis);
	}

	private void deleteClosedAnimation(ArrayList<Integer> deleteThis) {
		int offset = 0;
		for (int i = 0; i < deleteThis.size(); i++) {
			messagesCoord.remove(deleteThis.get(i) - offset);
			messagesInfo.remove(deleteThis.get(i) - offset);
			sendersId.remove(deleteThis.get(i) - offset);//}
			pathAnimationCoord.remove((deleteThis.get(i) - offset) * 2);
			pathAnimationCoord.remove((deleteThis.get(i) - offset) * 2);
			startEndAnimation.remove((deleteThis.get(i) - offset) * 2);
			startEndAnimation.remove((deleteThis.get(i) - offset) * 2);
			offset++;
		}
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void addPathAnimation(int node1, int node2, int start, int duration, Message message) {
		System.out.println("Path anim set: start " + start + " end " + (start + duration));
		sendersId.add(node1);
		pathAnimationCoord.add(usersCoord.get(node1));
		pathAnimationCoord.add(usersCoord.get(node2));
		messagesCoord.add(new Coord(usersCoord.get(node1).getX(), usersCoord.get(node1).getY()));
		messagesInfo.add(message);
		startEndAnimation.add(start);
		startEndAnimation.add(start + duration);
	}

	public ArrayList<Coord> getMessagesCoord() {
		return messagesCoord;
	}

	public Message getMessage(int index) {
		return messagesInfo.get(index);
	}

	public void restart(int usersCount)
	{
		this.usersCount = usersCount;
		this.currentTime = 0;
		this.usersStrings.clear();
		this.usersCoord.clear();
		this.messagesCoord.clear();
		this.messagesInfo.clear();
		this.sendersId.clear();
		this.pathAnimationCoord.clear();
		this.startEndAnimation.clear();
		this.usersColors.clear();
		//set colors
		Random rand = new Random(45);
		for (int i = 0; i < usersCount; i++)
			usersColors.add(new Color(
					rand.nextInt(255),
					rand.nextInt(255),
					rand.nextInt(255)
			));
		setUsersCoords();
		this.mouseListener.restart(usersCoord);
	}
}