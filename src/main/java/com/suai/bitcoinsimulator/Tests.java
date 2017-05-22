package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Contract;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.TxIn;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.TxOut;
import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import junit.framework.TestCase;

import java.security.PublicKey;

public class Tests extends TestCase {

    public Tests(String name) {
        super( name );
        //создаем ноды

    }

    public static void runTests() throws Exception {
        Simulator sim = new Simulator(1000, 2, 1000000, false);
        correctSendTransactionTest(sim);
        incorrectSendTransactionTest(sim);
        //doubleSpendingTest();
    }
    public static void correctSendTransactionTest(Simulator sim)
    {
        //две ноды
        BitcoinNode one = new BitcoinNode(1, 5,sim, true);
        BitcoinNode two = new BitcoinNode(2, 5, sim, true);
        //одна поднимает с нужным выходом
        Transaction tx = new Transaction();
        //выход
        TxOut output = new TxOut(tx.getOutput(
                tx.checkCondition(one.getPublicKey().toString().getBytes(),new  String("111").getBytes())).getSatoshisCount(), //количество биткоинов на выходе
                new PublicKey[]{one.getPublicKey()}, Contract.SIMPLE);
        //вторая поднимает с нужным входом
        //вход
        //в данном случае вход без подписи
        byte[] signature = null;
        try {
            signature = Crypt.signData(one.getPublicKey().toString().getBytes(), one.getPrivateKey());
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        TxIn input = new TxIn(
                tx.getTxId(),       //TxId транзакции-источника
                tx.checkCondition(one.getPublicKey().toString().getBytes(), signature), //индекс выхода на транзакции-источнике
                signature);              //подпись(сейчас не действительна)

        assertFalse(false);
        //проверяем, что транзакция получает данные
    }

    public static void incorrectSendTransactionTest(Simulator sim)
    {
        //две ноды
        BitcoinNode one = new BitcoinNode(1, 5,sim, true);
        BitcoinNode two = new BitcoinNode(2, 5, sim, true);
        //одна поднимает с нужным выходом
        Transaction tx = new Transaction();
        //выход
        TxOut output = new TxOut(tx.getOutput(
                tx.checkCondition(one.getPublicKey().toString().getBytes(),new  String("111").getBytes())).getSatoshisCount(), //количество биткоинов на выходе
                new PublicKey[]{one.getPublicKey()}, Contract.SIMPLE);
        //вторая поднимает с нужным входом
        //вход
        //в данном случае вход без подписи
        byte[] signature = null;
        try {
            signature = Crypt.signData(one.getPublicKey().toString().getBytes(), one.getPrivateKey());
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        TxIn input = new TxIn(
                tx.getTxId(),       //TxId транзакции-источника
                tx.checkCondition(one.getPublicKey().toString().getBytes(), signature), //индекс выхода на транзакции-источнике
                signature);              //подпись(сейчас не действительна)

        //проверяем, что транзакция получает данные
        assertTrue(true);
    }

    public static void doubleSpendingTest(Simulator sim)
    {

    }
}