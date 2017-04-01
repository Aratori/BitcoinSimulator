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
    private static int userCounter = 0;
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
        userId = userCounter;
        userCounter++;
        LogKeeper.info("User " + userId + " registered");
    }


    public User(int startTime, int intervalTime, Node parentNode, BitcoinSimulator simulator) {
        this.startTime = startTime;
        this.parentNode = parentNode;
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.insert(new Event(startTime, this));
        this.simulator.getNetwork().registration(this);
        userId = userCounter;
        userCounter++;
        LogKeeper.info("User " + userId + " registered");
    }

    public void onEvent()
    {
        int networkWaiting = 10;
        //next user awekening
        LogKeeper.info("User event happened " + simulator.getTime());
        simulator.insert(new Event(simulator.getTime() + interval, this));
        //send message to other user
        int id = simulator.getNetwork().getRandomUser(userId);                    /*receive id of user, whom we can send message*/
        simulator.getNetwork().sendMessage(
            id,
            (String)("User " + userId + " send message to User " + id) );
        simulator.insert(new Event(simulator.getTime() + networkWaiting, simulator.getNetwork()));
    }

    public void receiveMessage(String message)
    {
        LogKeeper.info("User " + userId + " receive message - " + "'" + message + "'" );
    }

    public int getId()
    {
        return userId;
    }
}
