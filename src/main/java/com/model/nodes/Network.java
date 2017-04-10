package com.model.nodes;

import com.model.events.Event;
import com.model.utils.LogKeeper;
import com.model.nodes.User;
import com.model.view.NetworkView;

import java.util.Vector;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Random;

public class Network extends Node {
    private Vector<User> users;
    private static Logger log = Logger.getLogger(Network.class.getName());
    private NetworkView view; 

    public Network() {
        users = new Vector();
    }

    //регистрация нового пользователя
    public void registration(User newUser) {
        users.add(newUser);
    }

    public void sendMessage(int userId, String message)
    {
        User n;
        for(Iterator<User> i = users.iterator(); i.hasNext();)
            if((n = i.next()).getId() == userId)
            {
                n.receiveMessage(message);
                break;
            }
    }
    /*
    *   Передает id пользователя, которому можно отправить сообщение
    **/ 
    public int getRandomUser(int currentUser)
    {
        Random rand = new Random();
        int id;
        do{
            id = rand.nextInt(users.size());
        }while(id == currentUser);

        return id;
    }
    //получение сообщения пользователя
    public void onEvent() {
        ;
    }
}
