package model.nodes;

import model.AbstractSimulator;
import model.events.Event;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User extends Node {
    private int startTime;
    private Node parentNode;
    private int interval;
    private AbstractSimulator simulator;

    public User(int startTime, int intervalTime, AbstractSimulator simulator) {
        this.startTime = startTime;
        this.parentNode = null;
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.insert(new Event(startTime, this));
    }


    public User(int startTime, int intervalTime, Node parentNode, AbstractSimulator simulator) {
        this.startTime = startTime;
        this.parentNode = parentNode;
        this.simulator = simulator;
        this.interval = intervalTime;
        this.simulator.insert(new Event(startTime, this));
    }

    public void onEvent()
    {
        System.out.println("User event happened");
        simulator.insert(new Event(simulator.getTime() + interval, this));
    }
}
