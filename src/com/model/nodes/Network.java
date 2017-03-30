package com.model.nodes;

import com.model.events.Event;
import com.model.utils.LogKeeper;

import java.util.Vector;
import java.util.logging.Logger;

public class Network extends Node {
    private Vector<Node> users;
    private static Logger log = Logger.getLogger(Network.class.getName());
    public Network() {
        users = new Vector();
    }

    //регистрация нового пользователя
    public void registration(Node newUser) {
        users.add(newUser);
    }

    //получение сообщения пользователя
    public void onEvent(Event event) {
        if (users.contains(event.getNode()))
            LogKeeper.info("Network receive message ");
        else
            LogKeeper.info("Unregistered user");
    }
}
