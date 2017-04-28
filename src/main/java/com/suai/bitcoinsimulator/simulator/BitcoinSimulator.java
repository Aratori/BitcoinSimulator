package com.suai.bitcoinsimulator.simulator;

import com.suai.bitcoinsimulator.simulator.events.Event;
import com.suai.bitcoinsimulator.simulator.nodes.Network;

import java.util.concurrent.PriorityBlockingQueue;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;
import com.suai.bitcoinsimulator.simulator.utils.StepUpdater;
import com.suai.bitcoinsimulator.view.SimulatorGUI;

import java.util.*;


public class BitcoinSimulator{
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
     * BitcoinSimulator constructor
     * @param endTime   - time, when simulator work will run out
     * @param usersCount    -   count of users in system.
     * @param step  -   update period
     * @param mode  -   GUI/TEXT modes
     */
    public BitcoinSimulator(int endTime, int usersCount, int step, boolean mode) {
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
                    e.getNode().onEvent();
                    //check for several events
                    while(events.size() != 0)
                    {
                        if(events.peek().getExecutionTime() == currentTime)
                        {
                            Event eN = events.poll();
                            eN.getNode().onEvent();
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
