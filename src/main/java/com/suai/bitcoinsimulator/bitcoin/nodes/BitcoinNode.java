package com.suai.bitcoinsimulator.bitcoin.nodes;


import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.*;
import com.suai.bitcoinsimulator.bitcoin.events.BlockCheckedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.BlockMinedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.SendPubKeyEvent;
import com.suai.bitcoinsimulator.bitcoin.messages.CheckBlockMessage;
import com.suai.bitcoinsimulator.bitcoin.messages.PubKeyMessage;
import com.suai.bitcoinsimulator.simulator.events.Event;
import com.suai.bitcoinsimulator.simulator.messages.Message;
import com.suai.bitcoinsimulator.simulator.nodes.User;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionCreate()
 checkBlock()
 mineBlock()
 sendBlock()
 sendPubKey()
 */
public class BitcoinNode extends User {
    private BlockChain blockChain = new BlockChain();
    private static int mineDelay = 50;
    private static int coinbaseReward = 50;
    private int privKey;
    private int pubKey;
    private int pubKeyHash;
    private Block currentMinedBlock;
    private Map<Integer, Integer> pubKeys = new HashMap<Integer, Integer>();

    /**
     * Хранит список транзакций, из которых можно совершить вывод.
     */
    private ArrayList<Transaction> myTransactions = new ArrayList<Transaction>();

    /**
     * BitcoinNode states
     *  - mine block;
     *  - check block;
     */
    private int currentState;

    public BitcoinNode(int startTime, int interval, Simulator bs) {
        super(startTime, interval, bs);
        //test priv and pub keys
        pubKey = getId();
        privKey = getId() + 42;
        //на старте сразу майним блок, чтобы получить биткоины
        mineStartBlock();
        //записываем событие на передачу публичного ключа другим нодам
        simulator.addEvent(new SendPubKeyEvent(simulator.getCurrentTime() + 4, this));
    }

    @Override
    public synchronized void onEvent(Event event)
    {

        //if need to send pubkey to other nodes
        if(event instanceof SendPubKeyEvent)
        {
            LogKeeper.info("User " + getId() + " send pubkey to other nodes", simulator.getCurrentTime());
            PubKeyMessage message = new PubKeyMessage("Pubkey of  user " + getId(), pubKey);
            simulator.getNetwork().sendMessageToAll(getId(), message);
        }

        //if block checked
        if(event instanceof BlockCheckedEvent)
        {

        }

        //пробуем намайнить блок с транзакцией, для другого пользователя
        findMyTransactions();
        if(myTransactions.size() != 0 && pubKeys.size() != 0 && currentMinedBlock == null)
        {
            //получатель
            int receiver = simulator.getNetwork().getRandomUser(getId());
            int receiverPubKey = pubKeys.get(receiver);
            //транзакция-источник
            Transaction tx = myTransactions.get(0);

            //вход
            //в данном случае вход без подписи
            TxIn input = new TxIn(
                    tx.getTxId(),       //TxId транзакции-источника
                    tx.checkCondition(privKey), //индекс выхода на транзакции-источнике
                    null);              //подпись(сейчас не действительна)
            //выход
            TxOut output = new TxOut(tx.getOutput(
                                        tx.checkCondition(privKey)).getSatoshisCount(), //количество биткоинов на выходе
                                        pubKey);                                        //условие выхода(сейчас не работает)
            mineBlock(input, output);
        }

        //if block mined
        if(event instanceof BlockMinedEvent)
        {
            LogKeeper.info("Mined new block by user " + getId(), simulator.getCurrentTime());
            //добавляем новый блок в блокчейн
            blockChain.addBlock(currentMinedBlock);
            //отправляем блок на проверку остальным нодам, чтобы они тоже добавили блок в блокчейн
            CheckBlockMessage message = new CheckBlockMessage("Check new block from node " + getId(), currentMinedBlock);
            simulator.getNetwork().sendMessageToAll(getId(), message);
            currentMinedBlock = null;
        }

        //добавляем событие на следующее пробуждение
        simulator.addEvent(new Event(simulator.getCurrentTime() + interval , this));
    }

    @Override
    public void receiveMessage(int senderId, Message message)
    {
        //если пришел публичный ключ
        if(message.getType() == 1)
        {
            LogKeeper.info("Node " + getId() + " receive public key from node " + senderId,
                            simulator.getCurrentTime());
            pubKeys.put(senderId, ((PubKeyMessage)message).getPubKey());
        }

        //если пришел блок для проверки
        if(message.getType() == 2)
        {
            LogKeeper.info("Node " + getId() + " receive block from node " + senderId,
                            simulator.getCurrentTime());
            checkBlock(((CheckBlockMessage)message).getBlock());
        }
    }

    /**
     * Node get from network. User verify block and if its true, block added to blockchain
     * @param block
     * @return
     */
    public boolean checkBlock(Block block) {
        //test, nothing to check
        blockChain.addBlock(block);
        return true;
    }

    /**
     * Create coinbase block only with coinbase transaction.
     * @return
     */
    public void mineStartBlock()
    {
        currentMinedBlock = new Block();
        //создаем транзакцию с выплатой создателю блока
        Transaction coinbaseTx = new Transaction();
        coinbaseTx.addOutput(new TxOut(coinbaseReward, pubKey));
        currentMinedBlock.addTransaction(coinbaseTx);
        //добавлем событие, на момент, когда блок смайнится
        simulator.addEvent(new BlockMinedEvent(simulator.getCurrentTime() + mineDelay + interval, this));
    }

    /**
     * Mine block with coinbase and other transactions
     * @param input - transaction's input
     * @param output - transaction' output
     *
     * @return
     */
    public Block mineBlock(TxIn input, TxOut output)
    {
        currentMinedBlock = new Block();
        //set coinbase transaction
        Transaction coinbaseTx = new Transaction();
        coinbaseTx.addOutput(new TxOut(coinbaseReward, pubKey));
        currentMinedBlock.addTransaction(coinbaseTx);
        //set base transaction
        Transaction tx = new Transaction();
        tx.addInput(input);
        tx.addOutput(output);
        currentMinedBlock.addTransaction(tx);
        //добавлем событие, на момент, когда блок смайнится
        simulator.addEvent(new BlockMinedEvent(simulator.getCurrentTime() + mineDelay + interval, this));

        return new Block();
    }

    /**
     * Ищет в блокчейне транзакции с выводом на адрес(pubkey) этой ноды.
     */
    //!!!!! ДОБАВИТЬ ПРОВЕРКУ, ЧТО ИЗ ТРАНЗАКЦИИ НЕ БЫЛИ УЖЕ ПЕРЕВЕДЕНЫ БИТКОИНЫ
    public void findMyTransactions() {
        myTransactions = new ArrayList<Transaction>();

        //идем по блокам
        for (int i = 0; i < blockChain.size(); i++)
        {
            Block block = blockChain.getBlock(i);
            //идем по транзакциям
            for(int j = 0; j < block.size(); j++)
            {
                Transaction tx = block.getTransaction(j);
                if(tx.checkCondition(privKey) != -1)
                    myTransactions.add(tx);
            }
        }
    }

    public void sendBlock(Block newBlock)
    {

    }
    public void sendPubKey()
    {

    }
}
