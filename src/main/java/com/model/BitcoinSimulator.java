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

    public BitcoinSimulator(int endTime) {
        this.endTime = endTime;
        this.events = new ListQueue();
        this.network = new Network();
        LogKeeper.createLogger();
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
        BitcoinSimulator bs = new BitcoinSimulator(400);
        User userOne = new User(10, 20, bs);
        User userTwo = new User(20, 30, bs);
        User userThree = new User(15, 15, bs);
        bs.startSimulation();
    }

    public Network getNetwork() {
        return network;
    }
}
