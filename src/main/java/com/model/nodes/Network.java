package com.model.nodes;

import com.model.BitcoinSimulator;
import com.model.events.Event;
import com.model.utils.LogKeeper;
import com.model.nodes.User;
import com.model.view.NetworkView;

import java.util.*;
import java.util.logging.Logger;

public class Network extends Node {
    private BitcoinSimulator bs;
    private ArrayList<User> users;
    private static Logger log = Logger.getLogger(Network.class.getName());
    private NetworkView view;
    private PriorityQueue<Integer> sendersId = new PriorityQueue<Integer>();
    private PriorityQueue<Integer> receiversId = new PriorityQueue<Integer>();
    private PriorityQueue<String> messages = new PriorityQueue<String>();
    private int delay;

    public Network(BitcoinSimulator bs, int delay) {
        this.bs = bs;
        this.delay = delay;
        users = new ArrayList<User>();
    }

    //регистрация нового пользователя
    public void registration(User newUser) {
        users.add(newUser);
    }

    //регистация события пересылки сообщения
    public void sendMessage(int senderId, int receiverId, String message)
    {
        sendersId.add(senderId);
        receiversId.add(receiverId);
        messages.add(message);
        bs.addEvent(new Event(bs.getCurrentTime()+delay, this));
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
        users.get(receiversId.poll()).receiveMessage(sendersId.poll(), messages.poll());
    }

    public int getDelay()
    {
        return delay;
    }
}
