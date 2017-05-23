package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.nodes.User;
public class Main {

    public static void main(String[] args) {
        Simulator bs = new Simulator(1000, 6, 100, true);
        User userOne = new BitcoinNode(60, 20, bs, false);
        User userTwo = new BitcoinNode(120, 30, bs, false);
        User userThree = new BitcoinNode(165, 40, bs, false);
        User userFour = new BitcoinNode(230, 10, bs, false);
        User userFive = new BitcoinNode(290, 50, bs, false);
        User userSix = new BitcoinNode(355, 60, bs, false);

        bs.startSimulation();
        //MainTest.mainteststart(args);
    }
}
