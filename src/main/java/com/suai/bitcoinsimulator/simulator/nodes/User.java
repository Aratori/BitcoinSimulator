package com.suai.bitcoinsimulator.simulator.nodes;

import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.simulator.Event;
import com.suai.bitcoinsimulator.simulator.Message;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.util.logging.Logger;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User extends Node {
    protected static int userCounter = 0;             //counter, that give id to users
    protected int userId;                             //id
    protected int interval;
    protected Simulator simulator;
    protected static Logger log = Logger.getLogger(User.class.getName());

    /**
     * Constructor of User node
     * @param startTime -   time for start, need only for register event in simulator
     * @param intervalTime  -   time period for generation new event
     * @param simulator -   base class of simulator
     */
    public User(int startTime, int intervalTime, Simulator simulator) {
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.getNetwork().registration(this);
        userId = userCounter;
        userCounter++;
        LogKeeper.info("User " + userId + " registered " , simulator.getCurrentTime());
        this.simulator.addEvent(new Event(startTime, this));
    }

    public synchronized void onEvent(Event event)
    {
        //next user awekening
        LogKeeper.info("User " + userId + " event happened " , simulator.getCurrentTime());
        simulator.addEvent(new Event(simulator.getCurrentTime() + interval, this));
        //send message to other user
        int receiverId = simulator.getNetwork().getRandomUser(userId);                    /*receive id of user, whom we can send message*/
        LogKeeper.info("User " + userId + " send message to  user " + receiverId, simulator.getCurrentTime());
        simulator.getNetwork().sendMessage(userId, receiverId, new Message((String)("Message from user " + userId + " user " + receiverId), 0));
        //if(simulator.getGUI() != null)
          //  simulator.getGUI().addSendAnimation(userId, receiverId, simulator.getCurrentTime(), simulator.getNetwork().getDelay());
        }

    public void receiveMessage(int senderId, Message message)
    {

        LogKeeper.info("User " + userId + " receive message - " + "'" + message.getInfoMessage() + "'", simulator.getCurrentTime());
    }

    public int getId()
    {
        return userId;
    }

    public static void restart()
    {
        userCounter = 0;
    }

    @Override
    public String toString()
    {
        return (String)("Network user " + getId());
    }

}    
