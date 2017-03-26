package model.nodes;

import model.BitcoinSimulator;
import model.events.Event;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User extends Node {
    private int startTime;
    private Node parentNode;
    private int interval;
    private BitcoinSimulator simulator;

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
        System.out.println("User event happened");
        simulator.insert(new Event(simulator.getTime() + interval, this));
        //добавляем событие на передачу сообщения в сеть
        simulator.insert(new Event(simulator.getTime() + networkWaiting, simulator.getNetwork()));
    }
}
