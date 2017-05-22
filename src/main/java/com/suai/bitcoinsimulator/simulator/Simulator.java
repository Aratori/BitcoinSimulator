package com.suai.bitcoinsimulator.simulator;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.nodes.Network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;

import com.suai.bitcoinsimulator.simulator.nodes.User;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;
import com.suai.bitcoinsimulator.simulator.utils.StepUpdater;
import com.suai.bitcoinsimulator.view.SimulatorGUI;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Simulator {
    private static Simulator currentSimulator;
    private int endTime;
    private int currentTime;
    private Network network;
    private SimulatorGUI gui;
    private Timer hrono; 
    private StepUpdater updater;
    protected PriorityBlockingQueue<Event> events;
    private boolean mode;
    private int step;               //quantity of real time for 1 programm time

    /**
     * Simulator constructor
     * @param endTime   - time, when simulator work will run out
     * @param usersCount    -   count of users in system.
     * @param step  -   update period
     * @param mode  -   GUI/TEXT modes
     */
    public Simulator(int endTime, int usersCount, int step, boolean mode) {
        this.endTime = endTime;
        this.currentTime = 0;
        this.events = new PriorityBlockingQueue<Event>();
        this.network = new Network(this, 20);
        this.step = step;
        this.mode = mode;

        if(mode)
            gui = new SimulatorGUI(this, usersCount);
        else
            gui = null;
        LogKeeper.createLogger(gui);
        currentSimulator = this;
    }

    public static void restartSimulator(String filename)
    {
        //смотрим конец файла
        //построчно считываем ноды
        File file = new File(filename);
        StringBuilder stringBuilder = new StringBuilder();

        if (!file.exists()){
            System.out.println("Config file not exist");
        }

        try{
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            String s;
            int start = -1;
            int i = 0;
            Pattern pat=Pattern.compile("[0-9]*");    //ищем числа
            Matcher matcher;
            //конфигурация симулятора
            int[] simulatorValues = new int[3];
            Boolean simulatorTog = true;
            if((s = in.readLine()) != null)
            {
                matcher=pat.matcher(s);
                //парсим конфигурацию симулятора
                while(matcher.find(start + 1))
                {
                    String value = s.substring(matcher.start(), matcher.end());
                    simulatorValues[i] = Integer.parseInt(value);
                    start = matcher.end();
                    if(i == 2)
                        break;
                    i++;
                }
            }
            //создаем новый симулятор
            //currentSimulator.clean();
            //currentSimulator = new Simulator(simulatorValues[0], simulatorValues[1], simulatorValues[2], simulatorTog);
            //обнулить симулятор
            currentSimulator.pause();
            currentSimulator.endTime = simulatorValues[0];
            currentSimulator.currentTime = 0;
            currentSimulator.step = simulatorValues[2];
            currentSimulator.network.restart();
            currentSimulator.events.clear();
            User.restart();
            //обнулить gui
            currentSimulator.gui.restart(simulatorValues[1]);
            //конфигурация нодов
            int[] nodeValues = new int[2];
            while((s = in.readLine()) != null)
            {
                start = -1;
                i = 0;
                matcher=pat.matcher(s);
                //парсим конфигурацию симулятора
                while(matcher.find(start + 1))
                {
                    String value = s.substring(matcher.start(), matcher.end());
                    nodeValues[i] = Integer.parseInt(value);
                    start = matcher.end();
                    if(i == 1)
                        break;
                    i++;
                }
                //регистрируем новую ноду
                new BitcoinNode(nodeValues[0], nodeValues[1], currentSimulator, false);
            }
            currentSimulator.startSimulation();
        } catch(IOException e) {
            System.err.println("File read error");
        }


    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void startSimulation() {
        //set timer, that will update gui and model every step time
        StepUpdater.setBitcoinSimulator(this);
        hrono = new Timer();
        updater = new StepUpdater();
        hrono.schedule(updater, 3, step);
    }

    public void pause()
    {
        if(hrono != null)
            hrono.cancel();
        else
            LogKeeper.info("Timer not found", currentTime);
    }

    public void resume()
    {
        hrono = new Timer();
        updater = new StepUpdater();
        hrono.schedule(updater, 3, step);
    }

    public void setSpeed(int type)
    {
        if(hrono != null)
            hrono.cancel();
        hrono = new Timer();
        updater = new StepUpdater();
        switch (type)
        {
            case 1:
            {
                hrono.schedule(updater, 3, 1000);
            }
            case 2:
            {
                hrono.schedule(updater, 3, 500);
            }
            case 3:
            {
                hrono.schedule(updater, 3, 100);
            }
        }
    }

    public synchronized void nextStep()
    {
        currentTime++;
        //check end of modeling
        if(currentTime > endTime || events.size() == 0)
        {
            LogKeeper.info("Simulation ended", currentTime);
            return;
        }
        System.out.println(events.size());
        //get most priority event and check it time
        synchronized(events){
            if(events.peek() != null)  {
                System.out.println(" --- --- --- ---");
                System.out.println("Current time: " + currentTime);
                System.out.println("Next event will execute: " + events.peek().getExecutionTime());
                if(events.peek().getExecutionTime() == currentTime)
                {
                    System.out.println("Event execute");    
                    Event e = events.poll();
                    e.getNode().onEvent(e);
                    //check for several events
                    while(events.size() != 0)
                    {
                        if(events.peek().getExecutionTime() == currentTime)
                        {
                            Event eN = events.poll();
                            eN.getNode().onEvent(eN);
                        }
                        else
                            break;

                    }
                }
            }
        }
        if(mode)
            gui.nextStep(currentTime);
    }

    public SimulatorGUI getGUI()
    {
        return gui;
    }

    public Network getNetwork() {
        return network;
    }

    public int getCurrentTime()
    {
        return currentTime;
    }
}
