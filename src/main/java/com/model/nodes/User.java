package com.model.nodes;

import com.model.BitcoinSimulator;
import com.model.events.Event;
import com.model.utils.LogKeeper;
import com.model.view.UserView;
import com.model.SimulatorGUI;

import java.util.logging.Logger;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User extends Node {
    private static int userCounter = 0;             //counter, that give id to users
    private int userId;                             //id
    private int interval;
    private BitcoinSimulator simulator;
    private static Logger log = Logger.getLogger(User.class.getName());

    /**
     * Constructor of User node
     * @param startTime -   time for start, need only for register event in simulator
     * @param intervalTime  -   time period for generation new event
     * @param simulator -   base class of simulator
     */
    public User(int startTime, int intervalTime, BitcoinSimulator simulator) {
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.getNetwork().registration(this);
        userId = userCounter;
        userCounter++;
        LogKeeper.info("User " + userId + " registered " , simulator.getCurrentTime());
        this.simulator.addEvent(new Event(startTime, this));
    }

    public synchronized void onEvent()
    {
        //next user awekening
        LogKeeper.info("User " + userId + " event happened " , simulator.getCurrentTime());
        simulator.addEvent(new Event(simulator.getCurrentTime() + interval, this));
        //send message to other user
        int receiverId = simulator.getNetwork().getRandomUser(userId);                    /*receive id of user, whom we can send message*/
        LogKeeper.info("User " + userId + " send message to  user " + receiverId, simulator.getCurrentTime());
        simulator.getNetwork().sendMessage(userId, receiverId, (String)("Message from user " + userId + " user " + receiverId));
        if(simulator.getGUI() != null)
            simulator.getGUI().addSendAnimation(userId, receiverId, simulator.getCurrentTime(), simulator.getNetwork().getDelay());
        }

    public void receiveMessage(int senderId, String message)
    {

        LogKeeper.info("User " + userId + " receive message - " + "'" + message + "'", simulator.getCurrentTime());
    }

    public int getId()
    {
        return userId;
    }
}    
