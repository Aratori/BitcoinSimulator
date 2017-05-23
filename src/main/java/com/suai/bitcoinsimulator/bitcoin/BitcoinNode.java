package com.suai.bitcoinsimulator.bitcoin;


import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.*;
import com.suai.bitcoinsimulator.bitcoin.events.BlockCheckedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.BlockMinedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.SendPubKeyEvent;
import com.suai.bitcoinsimulator.bitcoin.messages.CheckBlockMessage;
import com.suai.bitcoinsimulator.bitcoin.messages.PubKeyMessage;
import com.suai.bitcoinsimulator.simulator.Event;
import com.suai.bitcoinsimulator.simulator.Message;
import com.suai.bitcoinsimulator.simulator.nodes.User;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;
import com.suai.bitcoinsimulator.simulator.utils.LogKeeper;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionCreate()
 * checkBlock()
 * mineBlock()
 * sendBlock()
 * sendPubKey()
 */
public class BitcoinNode extends User {
    private BlockChain blockChain;
    private static int mineDelay = 50;
    private static int coinbaseReward = 50;
    private KeyPair keyPair;
    private int pubKeyHash;
    private Block currentMinedBlock;
    private Map<Integer, PublicKey> pubKeys = new HashMap<Integer, PublicKey>();

    /**
     * BitcoinNode states
     * - mine block;
     * - check block;
     */
    private int currentState;

    public BitcoinNode(int startTime, int interval, Simulator bs, boolean stat) {

        super(startTime, interval, bs);
        //test priv and pub keys
        try {
            blockChain = new BlockChain(this);
            keyPair = Crypt.generateKeyPair(getId());
            //на старте сразу майним блок, чтобы получить биткоины
            if(!stat) {
                mineStartBlock();
                //записываем событие на передачу публичного ключа другим нодам
                simulator.addEvent(new SendPubKeyEvent(simulator.getCurrentTime() + 4, this));
            }
        } catch (Exception ex) {
            LogKeeper.info("Can't generate key pair for node " + getId(), simulator.getCurrentTime());
        }
    }

    @Override
    public synchronized void onEvent(Event event) {

        //if need to send pubkey to other nodes
        if (event instanceof SendPubKeyEvent) {
            LogKeeper.info("User " + getId() + " send pubkey to other nodes", simulator.getCurrentTime());
            PubKeyMessage message = new PubKeyMessage("Pubkey of  user " + getId(), keyPair.getPublic());
            simulator.getNetwork().sendMessageToAll(getId(), message);
        }

        //if block checked
        if (event instanceof BlockCheckedEvent) {

        }

        //пробуем намайнить блок с транзакцией, для другого пользователя
        ArrayList<Transaction> myTransactions = blockChain.getMyTransactions();
        if (myTransactions.size() != 0 && pubKeys.size() != 0 && currentMinedBlock == null) {
            //получатель
            int receiver = simulator.getNetwork().getRandomUser(getId());
            //публичный ключ предназначенный для выхода
            PublicKey receiverPubKey = pubKeys.get(receiver);
            //транзакция-источник
            Transaction txSource = myTransactions.get(0);
            byte[] txSourceId = txSource.getTxId();

            //вход
            //на входе закладываем подпись
            byte[] signature = null;
            try {
                signature = Crypt.signData(txSourceId, keyPair.getPrivate());
            } catch (Exception ex) {
                System.err.println("Signature creation failed");
            }
            //индекс выхода
            int outputIndex = txSource.checkCondition(txSourceId, signature);
            TxIn input = new TxIn(
                    txSourceId,       //TxId транзакции-источника
                    outputIndex, //индекс выхода на транзакции-источнике
                    signature);              //подпись
            //выход
            TxOut output = new TxOut(
                    txSource.getOutput(outputIndex).getSatoshisCount(), //количество биткоинов на выходе
                    new PublicKey[]{receiverPubKey}, Contract.SIMPLE);                                        //условие выхода(сейчас не работает)
            mineBlock(input, output);
        }

        //if block mined
        if (event instanceof BlockMinedEvent) {
            LogKeeper.info("Mined new block by user " + getId(), simulator.getCurrentTime());
            //добавляем новый блок в блокчейн
            blockChain.checkBlock(currentMinedBlock);
            //отправляем блок на проверку остальным нодам, чтобы они тоже добавили блок в блокчейн
            CheckBlockMessage message = new CheckBlockMessage("Check new block from node " + getId(), currentMinedBlock);
            simulator.getNetwork().sendMessageToAll(getId(), message);
            currentMinedBlock = null;
        }

        //добавляем событие на следующее пробуждение
        simulator.addEvent(new Event(simulator.getCurrentTime() + interval, this));
    }

    @Override
    public void receiveMessage(int senderId, Message message) {
        //если пришел публичный ключ
        if (message.getType() == 1) {
            LogKeeper.info("Node " + getId() + " receive public key from node " + senderId,
                    simulator.getCurrentTime());
            pubKeys.put(senderId, ((PubKeyMessage) message).getPubKey());
        }

        //если пришел блок для проверки
        if (message.getType() == 2) {
            LogKeeper.info("Node " + getId() + " receive block from node " + senderId,
                    simulator.getCurrentTime());
            blockChain.checkBlock(((CheckBlockMessage) message).getBlock());
        }
    }

    /**
     * Create coinbase block only with coinbase transaction.
     *
     * @return
     */
    public void mineStartBlock() {
        try {
            currentMinedBlock = new Block(Integer.toString(getId()).getBytes(), simulator.getCurrentTime());
            //создаем транзакцию с выплатой создателю блока
            Transaction coinbaseTx = new Transaction();
            coinbaseTx.addOutput(new TxOut(coinbaseReward, new PublicKey[]{keyPair.getPublic()}, Contract.SIMPLE));
            currentMinedBlock.addTransaction(coinbaseTx);
            //добавлем событие, на момент, когда блок смайнится
            simulator.addEvent(new BlockMinedEvent(simulator.getCurrentTime() + mineDelay + interval, this));
        } catch (Exception ex) {
            LogKeeper.info("Mine start block exception", simulator.getCurrentTime());
        }
    }

    /**
     * Mine block with coinbase and other transactions
     *
     * @param input  - transaction's input
     * @param output - transaction' output
     * @return
     */
    public Block mineBlock(TxIn input, TxOut output) {
        currentMinedBlock = new Block(blockChain.getLastBlock().getBlockHeaderHash(), simulator.getCurrentTime());
        //set coinbase transaction
        Transaction coinbaseTx = new Transaction();
        coinbaseTx.addOutput(new TxOut(coinbaseReward, new PublicKey[]{keyPair.getPublic()}, Contract.SIMPLE));
        currentMinedBlock.addTransaction(coinbaseTx);
        //set base transaction
        Transaction tx = new Transaction();
        tx.addInput(input);
        tx.addOutput(output);
        currentMinedBlock.addTransaction(tx);
        //добавлем событие, на момент, когда блок смайнится
        simulator.addEvent(new BlockMinedEvent(simulator.getCurrentTime() + mineDelay + interval, this));

        return new Block(blockChain.getLastBlock().getBlockHeaderHash(), simulator.getCurrentTime());
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public static int getIdCounter()
    {
        return BitcoinNode.userCounter;
    }


    @Override
    public String toString() {
        String str = "Bitcoin Node " + Integer.toString(getId()) + "\n";
        str += blockChain.toString();
        return str;
    }
    //////////////////////////////////////////////////////
    //STATIC API FOR TESTS
    //////////////////////////////////////////////////////
    public static Block mineStartBlock(int id,int time, PublicKey pk)
    {
        Block startBlock = null;
        try {
            startBlock = new Block(Integer.toString(id).getBytes(), time);
            //создаем транзакцию с выплатой создателю блока
            Transaction coinbaseTx = new Transaction();
            coinbaseTx.addOutput(new TxOut(coinbaseReward, new PublicKey[]{pk}, Contract.SIMPLE));
            startBlock.addTransaction(coinbaseTx);
        } catch (Exception ex) {;}
        return startBlock;
    }
    public static Block mineBlock(TxIn input, TxOut output, Block prevBlock, int time, PublicKey pk) {
        Block newBlock = new Block(prevBlock.getBlockHeaderHash(), time);
        //set coinbase transaction
        Transaction coinbaseTx = new Transaction();
        coinbaseTx.addOutput(new TxOut(coinbaseReward, new PublicKey[]{pk}, Contract.SIMPLE));
        newBlock.addTransaction(coinbaseTx);
        //set base transaction
        Transaction tx = new Transaction();
        tx.addInput(input);
        tx.addOutput(output);
        newBlock.addTransaction(tx);

        return newBlock;
    }
}
