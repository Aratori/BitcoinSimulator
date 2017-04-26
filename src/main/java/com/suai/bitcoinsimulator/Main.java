package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.simulator.BitcoinSimulator;
import com.suai.bitcoinsimulator.simulator.nodes.User;

/**
 * Created by anton on 26.04.17.
 */
public class Main {

    public static void main(String[] args)
    {
        BitcoinSimulator bs = new BitcoinSimulator(400, 6, 1000, true);
        User userOne = new User(10, 20, bs);
        User userTwo = new User(20, 30, bs);
        User userThree = new User(15, 15, bs);
        User userFour = new User(15, 15, bs);
        User userFive = new User(45, 20, bs);
        User userSix = new User(45, 20, bs);

        bs.startSimulation();
    }
}
