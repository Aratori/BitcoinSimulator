package com.model.nodes;

import com.model.BitcoinSimulator;
import com.model.events.Event;
import com.model.utils.LogKeeper;

import java.util.logging.Logger;

/**
 * Узел, отвечающий за создание событий пользователя.
 * Создает событие при инициализации и через некоторые интервалы времени.
 */
public class User  {
    private int userId;
    private int startTime;
    private Node parentNode;
    private int interval;
    private BitcoinSimulator simulator;
    private static Logger log = Logger.getLogger(User.class.getName());


}
