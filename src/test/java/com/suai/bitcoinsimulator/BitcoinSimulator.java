package com.model;

import com.model.events.Event;
import com.model.nodes.Network;
import com.model.nodes.Node;
import com.model.nodes.User;
import com.model.utils.LogKeeper;

public class BitcoinSimulator extends AbstractSimulator {
    private Network network;

    public BitcoinSimulator(int endTime) {
        this.network = new Network();
        LogKeeper.createLogger();
    }

    public void addEvent(int time, Node node) {
        Event event = new Event(time, node);
    }

    public void startSimulation() {
        //идем по очереди
        //на каждой итерации выбираем наиболее приоритетное событие
        //и выполняем его

    }

    public static void main(String[] args)
    {
        BitcoinSimulator bs = new BitcoinSimulator(400);
    }

    public Network getNetwork() {
        return network;
    }
}
