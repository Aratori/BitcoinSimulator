package com.model.nodes;

import com.model.BitcoinSimulator;
import com.model.events.Event;
import com.model.utils.LogKeeper;

import java.util.logging.Logger;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User extends Node {
    private int userId;
    private int startTime;
    private Node parentNode;
    private int interval;
    private BitcoinSimulator simulator;
    private static Logger log = Logger.getLogger(User.class.getName());

    public User(int startTime, int intervalTime, BitcoinSimulator simulator) {
        this.startTime = startTime;
        this.parentNode = null;
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.insert(new Event(startTime, this));
        this.simulator.getNetwork().registration(this);
    }


    public User(int startTime, int intervalTime, Node parentNode, BitcoinSimulator simulator) {
        this.startTime = startTime;
        this.parentNode = parentNode;
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.insert(new Event(startTime, this));
        this.simulator.getNetwork().registration(this);
    }

    public void onEvent(Event event)
    {
        int networkWaiting = 10;
        LogKeeper.info("User event happened");
        simulator.insert(new Event(simulator.getTime() + interval, this));
        //добавляем событие на передачу сообщения в сеть
        simulator.insert(new Event(simulator.getTime() + networkWaiting, simulator.getNetwork()));
    }
}