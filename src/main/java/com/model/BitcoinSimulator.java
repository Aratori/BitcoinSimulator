package com.model;

import com.model.events.Event;
import com.model.nodes.Network;
import com.model.nodes.Node;
import com.model.nodes.User;
import com.model.utils.ListQueue;
import com.model.utils.LogKeeper;


public class BitcoinSimulator extends AbstractSimulator {
    private int endTime;
    private Network network;
    private static  SimulatorGUI gui;

    public BitcoinSimulator(int endTime, int usersCount) {
        this.endTime = endTime;
        this.events = new ListQueue();
        this.network = new Network();
        gui = new SimulatorGUI(6);
        LogKeeper.createLogger(gui);
    }

    public void addEvent(int time, Node node) {
        Event event = new Event(time, node);
        events.insert(event);
    }

    public void startSimulation() {
        //идем по очереди
        //на каждой итерации выбираем наиболее приоритетное событие
        //и выполняем его
        while (time < endTime && events.size() != 0) {
            Event e = (Event) events.removeFirst();
            time += e.getExecutionTime();
            e.getNode().onEvent();
        }
    }

    public static void main(String[] args)
    {
        BitcoinSimulator bs = new BitcoinSimulator(400, 6);
        User userOne = new User(10, 20, bs);
        User userTwo = new User(20, 30, bs);
        User userThree = new User(15, 15, bs);
        User userFour = new User(15, 15, bs);
        User userFive = new User(45, 20, bs);
        User userSiz = new User(45, 20, bs);
        
        bs.startSimulation();
    }

    public SimulatorGUI getGUI()
    {
        return gui;
    }

    public Network getNetwork() {
        return network;
    }
}
