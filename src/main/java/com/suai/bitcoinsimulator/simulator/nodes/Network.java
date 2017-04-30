package com.suai.bitcoinsimulator.simulator.nodes;

import com.suai.bitcoinsimulator.bitcoin.BitcoinSimulator;
import com.suai.bitcoinsimulator.simulator.events.Event;
import com.suai.bitcoinsimulator.simulator.messages.Message;
import com.suai.bitcoinsimulator.view.NetworkView;

import java.util.*;
import java.util.logging.Logger;

public class Network extends Node {
    private BitcoinSimulator bs;
    private ArrayList<User> users;
    private static Logger log = Logger.getLogger(Network.class.getName());
    private NetworkView view;
    private PriorityQueue<Integer> sendersId = new PriorityQueue<Integer>();
    private PriorityQueue<Integer> receiversId = new PriorityQueue<Integer>();
    private ArrayList<Message> messages = new ArrayList<Message>();
    private int delay;      //задержка на пересылку сообщения

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
    public void sendMessage(int senderId, int receiverId, Message message)
    {
        sendersId.add(senderId);
        receiversId.add(receiverId);
        messages.add(message);
        bs.addEvent(new Event(bs.getCurrentTime()+delay, this));
    }

    public void sendMessageToAll(int senderId, Message message)
    {
        //пробегаем по всем нодам и отправляем им сообщения
        for(int i = 0; i < users.size(); i++)
        {
            if(i == senderId)
                continue;
            sendersId.add(senderId);
            receiversId.add(i);
            messages.add(message);
            bs.addEvent(new Event(bs.getCurrentTime() + delay, this));
            //запускаем отсюда анимацию
            if(bs.getGUI() != null)
                bs.getGUI().addSendAnimation(senderId, i, bs.getCurrentTime(), bs.getCurrentTime() + delay);
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
    public void onEvent(Event event) {
        users.get(receiversId.poll()).receiveMessage(sendersId.poll(), messages.get(0));
        messages.remove(0);
    }

    public int getDelay()
    {
        return delay;
    }
}
