package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.bitcoin.BitcoinSimulator;
import com.suai.bitcoinsimulator.bitcoin.nodes.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.nodes.User;

/**
 * Created by anton on 26.04.17.
 */
public class Main {

    public static void main(String[] args)
    {
        BitcoinSimulator bs = new BitcoinSimulator(400, 6, 100, true   );
        User userOne = new BitcoinNode(10, 20, bs);
        User userTwo = new BitcoinNode(20, 30, bs);
        User userThree = new BitcoinNode(15, 40, bs);
        User userFour = new BitcoinNode(80, 10, bs);
        User userFive = new BitcoinNode(45, 50, bs);
        User userSix = new BitcoinNode(45, 60, bs);

        bs.startSimulation();
    }
}
