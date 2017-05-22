package com.suai.bitcoinsimulator.view.listeners;

import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.simulator.utils.Coord;
import com.suai.bitcoinsimulator.view.NetworkView;
import com.suai.bitcoinsimulator.view.SimulatorGUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by anton on 04.05.17.
 */
public class NetworkMouseListener implements MouseListener {
    private SimulatorGUI gui;
    private ArrayList<Coord> centers;
    private NetworkView network;
    private int userSize, messageSize;

    public NetworkMouseListener(SimulatorGUI gui,
                                NetworkView network,
                                ArrayList<Coord> centers,
                                int userSize,
                                int messageSize)
    {
        this.gui = gui;
        this.network = network;
        this.centers = centers;
        this.userSize = userSize;
        this.messageSize = messageSize;
    }

    public void restart(ArrayList<Coord> centers){
        this.centers = centers;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        // пробегаем по координатам нодов
        // и смотрим в какие области попадает событие
        for(int i = 0; i < centers.size(); i++)
        {
            int x = centers.get(i).getX();
            int y = centers.get(i).getY();

            if(e.getX() > (x - userSize/2)
                    && e.getX() < (x + userSize/2)
                    && e.getY() > (y - userSize/2)
                    && e.getY() < (y + userSize/2)) {
                gui.setUserDebugInfo(i);
                break;
            }
        }
        //пробегаем по сообщениям
        ArrayList<Coord> messages = network.getMessagesCoord();
        for(int i = 0; i < messages.size(); i++)
        {
            int x = messages.get(i).getX();
            int y = messages.get(i).getY();

            if(e.getX() > (x - messageSize/2)
                    && e.getX() < (x + messageSize/2)
                    && e.getY() > (y - messageSize/2)
                    && e.getY() < (y + messageSize/2)) {
                gui.setMessageDebugInfo(network.getMessage(i));
                break;
            }
        }
    }

}
