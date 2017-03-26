package model.nodes;

import model.events.Event;

import java.util.Vector;

public class Network extends Node {
    Vector<Node> users;

    public Network() {
        users = new Vector<>();
    }

    //регистрация нового пользователя
    public void registration(Node newUser) {
        users.add(newUser);
    }

    //получение сообщения пользователя
    public void onEvent(Event event) {
        if (users.contains(event.getNode()))
            System.out.println("Network receive message");
        else
            System.out.println("Unregistered user");
    }
}
