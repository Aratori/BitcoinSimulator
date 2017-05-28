package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.*;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PublicKey;

public class BitcoinTest extends Assert {

    @Test
    public void correctSendTransactionTest() throws Exception
    {
        //формируем два узла, не генерирующие новые события
        KeyPair keyPairA = Crypt.generateKeyPair(1);
        BlockChain blockChainA = new BlockChain(keyPairA.getPublic());
        KeyPair keyPairB = Crypt.generateKeyPair(2);
        BlockChain blockChainB = new BlockChain(keyPairB.getPublic());

        //майним каждому по начальному блоку
        Block startA = BitcoinNode.mineStartBlock(1, 1, keyPairA.getPublic());
        Block startB = BitcoinNode.mineStartBlock(2, 2, keyPairB.getPublic());
        //добавляем блоки в блокчейны
        assertTrue(blockChainA.checkBlock(startA));
        assertTrue(blockChainA.checkBlock(startB));
        assertTrue(blockChainB.checkBlock(startA));
        assertTrue(blockChainB.checkBlock(startB));

        ////////////////////////////////
        ////A отправляет биткоины B
        ////////////////////////////////
        //для этого формируем нужные вход и выход
        //а потом формируем из них блок
        Transaction txSource = startA.getTransaction(0);
        byte[] txSourceId = txSource.getTxId();
        //вход
        //на входе закладываем подпись
        byte[] signature = null;
        try {
            signature = Crypt.signData(txSourceId, keyPairA.getPrivate());
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        //индекс выхода
        int outputIndex = txSource.checkCondition(txSourceId, signature);
        TxIn input = new TxIn(
                txSourceId,       //TxId транзакции-источника
                outputIndex, //индекс выхода на транзакции-источнике
                new byte[][]{signature});              //подпись
        //выход
        TxOut output = new TxOut(
                txSource.getOutput(outputIndex).getSatoshisCount(), //количество биткоинов на выходе
                new PublicKey[]{keyPairB.getPublic()}, Contract.SIMPLE);                                        //условие выхода(сейчас не работает)
        Block sendABBlock = BitcoinNode.mineBlock(input, output, startB, 3, keyPairA.getPublic());

        //////////////////////////////////////////
        //новый блок добавляем в блокчейны и проверяем, прошел ли он проверку
        //////////////////////////////////////////
        assertTrue(blockChainA.checkBlock(sendABBlock));
        assertTrue(blockChainB.checkBlock(sendABBlock));
    }
    /*
       Тест на неверный ключ
     */
    @Test
    public  void incorrectSendTransactionTest() throws Exception
    {
        //формируем два узла, не генерирующие новые события
        KeyPair keyPairA = Crypt.generateKeyPair(1);
        BlockChain blockChainA = new BlockChain(keyPairA.getPublic());
        KeyPair keyPairB = Crypt.generateKeyPair(2);
        BlockChain blockChainB = new BlockChain(keyPairB.getPublic());

        //майним каждому по начальному блоку
        Block startA = BitcoinNode.mineStartBlock(1, 1, keyPairA.getPublic());
        Block startB = BitcoinNode.mineStartBlock(2, 2, keyPairB.getPublic());
        //добавляем блоки в блокчейны
        assertTrue(blockChainA.checkBlock(startA));
        assertTrue(blockChainA.checkBlock(startB));
        assertTrue(blockChainB.checkBlock(startA));
        assertTrue(blockChainB.checkBlock(startB));

        ////////////////////////////////
        ////A отправляет биткоины B
        ////////////////////////////////
        //для этого формируем нужные вход и выход
        //а потом формируем из них блок
        Transaction txSource = startA.getTransaction(0);
        byte[] txSourceId = txSource.getTxId();
        //вход
        //на входе закладываем подпись
        byte[] signature = null;
        try {
            signature = Crypt.signData(txSourceId, keyPairB.getPrivate());
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        //индекс выхода
        int outputIndex = txSource.checkCondition(txSourceId, signature);
        TxIn input = new TxIn(
                txSourceId,       //TxId транзакции-источника
                0, //индекс выхода на транзакции-источнике
                new byte[][]{signature});              //подпись
        //выход
        TxOut output = new TxOut(
                txSource.getOutput(0).getSatoshisCount(), //количество биткоинов на выходе
                new PublicKey[]{keyPairB.getPublic()}, Contract.SIMPLE);                                        //условие выхода(сейчас не работает)
        Block sendABBlock = BitcoinNode.mineBlock(input, output, startB, 3, keyPairA.getPublic());

        //////////////////////////////////////////
        //новый блок добавляем в блокчейны и проверяем, прошел ли он проверку
        //////////////////////////////////////////
        assertFalse(blockChainA.checkBlock(sendABBlock));
        assertFalse(blockChainB.checkBlock(sendABBlock));
    }
    /*
        Тест на перевод лишней суммы
     */
    @Test
    public void incorrectOutputSumTest() throws Exception
    {
        //формируем два узла, не генерирующие новые события
        KeyPair keyPairA = Crypt.generateKeyPair(1);
        BlockChain blockChainA = new BlockChain(keyPairA.getPublic());
        KeyPair keyPairB = Crypt.generateKeyPair(2);
        BlockChain blockChainB = new BlockChain(keyPairB.getPublic());

        //майним каждому по начальному блоку
        Block startA = BitcoinNode.mineStartBlock(1, 1, keyPairA.getPublic());
        Block startB = BitcoinNode.mineStartBlock(2, 2, keyPairB.getPublic());
        //добавляем блоки в блокчейны
        assertTrue(blockChainA.checkBlock(startA));
        assertTrue(blockChainA.checkBlock(startB));
        assertTrue(blockChainB.checkBlock(startA));
        assertTrue(blockChainB.checkBlock(startB));

        ////////////////////////////////
        ////A отправляет биткоины B
        ////////////////////////////////
        //для этого формируем нужные вход и выход
        //а потом формируем из них блок
        Transaction txSource = startA.getTransaction(0);
        byte[] txSourceId = txSource.getTxId();
        //вход
        //на входе закладываем подпись
        byte[] signature = null;
        try {
            signature = Crypt.signData(txSourceId, keyPairA.getPrivate());
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        //индекс выхода
        int outputIndex = txSource.checkCondition(txSourceId, signature);
        TxIn input = new TxIn(
                txSourceId,       //TxId транзакции-источника
                outputIndex, //индекс выхода на транзакции-источнике
                new byte[][]{signature});              //подпись
        //выход
        TxOut output = new TxOut(
                txSource.getOutput(outputIndex).getSatoshisCount() + 50, //количество биткоинов на выходе
                new PublicKey[]{keyPairB.getPublic()}, Contract.SIMPLE);                                        //условие выхода(сейчас не работает)
        Block sendABBlock = BitcoinNode.mineBlock(input, output, startB, 3, keyPairA.getPublic());

        //////////////////////////////////////////
        //новый блок добавляем в блокчейны и проверяем, прошел ли он проверку
        //////////////////////////////////////////
        assertFalse(blockChainA.checkBlock(sendABBlock));
        assertFalse(blockChainB.checkBlock(sendABBlock));
    }
}