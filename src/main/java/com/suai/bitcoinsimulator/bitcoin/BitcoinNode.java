package com.suai.bitcoinsimulator.bitcoin;


import com.suai.bitcoinsimulator.bitcoin.messages.*;
import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.*;
import com.suai.bitcoinsimulator.bitcoin.events.BlockCheckedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.BlockMinedEvent;
import com.suai.bitcoinsimulator.bitcoin.events.SendPubKeyEvent;
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
import java.util.Random;

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
    private Random conductRandom;

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
            keyPair = Crypt.generateKeyPair(getId());
            blockChain = new BlockChain(this);
            //на старте сразу майним блок, чтобы получить биткоины
            if (!stat) {
                conductRandom = new Random(userId);
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

        //if block mined
        if (event instanceof BlockMinedEvent) {
            LogKeeper.info("Mined new block by user " + getId(), simulator.getCurrentTime());
            //добавляем новый блок в блокчейн
            if (blockChain.checkBlock(currentMinedBlock)) {
                //отправляем блок на проверку остальным нодам, чтобы они тоже добавили блок в блокчейн
                CheckBlockMessage message = new CheckBlockMessage("Check new block from node " + getId(), currentMinedBlock);
                simulator.getNetwork().sendMessageToAll(getId(), message);
                ////В зависимости от контракта нужны дополнительные сообщения
                if (currentMinedBlock.size() > 1)    //не стартовый блок
                {
                    Contract contract = currentMinedBlock.getTransaction(1).getOutput(0).getContract();
                    switch (contract) {
                        case ARBITER: {
                            Transaction arbiterTx = currentMinedBlock.getTransaction(1);
                            arbiterConduct(arbiterTx);
                        }
                    }
                }

                currentMinedBlock = null;
            }
        }

        ArrayList<Transaction> myTransactions = blockChain.getMyTransactions();
        if (myTransactions.size() != 0 && pubKeys.size() != 0 && currentMinedBlock == null) {
            ////////////////////////////////////////////////////////
            //  Набор логик поведения для ноды:
            //  1. Узел не использует контракт на текущем шаге.
            //  2. Узел использует контракт с арбитром.
            //  3. Узел использует контракт с каналом микроплатежей.
            //  4. Узел использует контракт CoinJoin
            ////////////////////////////////////////////////////////
            int currentConduct = conductRandom.nextInt(2);

            switch (currentConduct) {
                case 0:                     // без контракта
                {
                    Transaction txSource = myTransactions.get(0);
                    noContractMineBlock(txSource);
                    break;
                }
                case 1:                     //с арбитром
                {
                    Transaction txSource = myTransactions.get(0);
                    arbiterMineBlock(txSource);
                    break;
                }
                case 2:                     //канал микроплатежей
                {

                    break;
                }
                case 3:                     //CoinJoin
            }


        }


        //добавляем событие на следующее пробуждение
        simulator.addEvent(new Event(simulator.getCurrentTime() + interval, this));
    }

    /**
     * Метод, определяющий и запускающий конфликт для транзакции с арбитром
     * @param arbiterTx
     */
    private void arbiterConduct(Transaction arbiterTx) {
        //получаем id получателя и арбитра
        PublicKey[] recArb = new PublicKey[]
                {
                        arbiterTx.getOutput(0).getCertainPublicKey(1),
                        arbiterTx.getOutput(0).getCertainPublicKey(2)
                };
        int[] recArbId = new int[2];
        for (int i = 0; i < pubKeys.size(); i++) {
            if (pubKeys.get(i) == recArb[0])
                recArbId[0] = i;
            if (pubKeys.get(i) == recArb[1])
                recArbId[1] = i;
        }
        //Выбираем тип конфликта для контракта с арбитром
        int conflict = conductRandom.nextInt(3);
        //нет конфликта, тогда просим подпись у получателя
        if (conflict == 0) {
            ProduceSignatureMessage psMessage = new ProduceSignatureMessage(
                    "Produce signature for transaction",
                    arbiterTx);
            simulator.getNetwork().sendMessage(userId, recArbId[0], psMessage);
        }
        //  есть конфликт, но на нашей стороне
        //  тогда просим создать транзакцию арбитру
        if (conflict == 1) {
            byte[] signature = createMySign(arbiterTx.getTxId());
            ArbiterResolveMessage arMessage = new ArbiterResolveMessage(
                    "Mine block with new output for me",
                    arbiterTx,
                    signature
            );
            simulator.getNetwork().sendMessage(userId, recArbId[1], arMessage);
        }
        //  конфликт на принимающей стороне
        //  просим разрешить его получателю
        if (conflict == 2) {
            ArbiterConflictMessage psMessage = new ArbiterConflictMessage(
                    "Resolve conflict for transaction",
                    arbiterTx,
                    recArbId[1]);
            simulator.getNetwork().sendMessage(userId, recArbId[0], psMessage);
        }
    }

    private void arbiterMineBlock(Transaction txSource) {
        //выбрать получателя
        int receiverIndex = simulator.getNetwork().getRandomUser(userId);
        PublicKey receiver = pubKeys.get(receiverIndex);
        //выбрать арбитра, не являющегося данных пользователем и получателем
        int arbiterIndex;
        do {
            arbiterIndex = simulator.getNetwork().getRandomUser(userId);
        } while (arbiterIndex == receiverIndex);
        PublicKey arbiter = pubKeys.get(arbiterIndex);
        ////Создать транзакцию с тремя подписями
        //вход
        TxIn input = createInput(txSource);
        //выход, который задействует три подписи и открывается при двух
        TxOut output = new TxOut(
                txSource.getOutput(input.getOutputIndex()).getSatoshisCount(),
                new PublicKey[]{keyPair.getPublic(), receiver, arbiter},
                Contract.ARBITER
        );
        mineBlock(input, output);
    }

    /**
     * Создает вход для транзакции
     *
     * @return
     */
    private TxIn createInput(Transaction txSource) {
        byte[] txSourceId = txSource.getTxId();
        byte[] signature = createMySign(txSourceId);
        int outputIndex = txSource.checkCondition(txSourceId, signature);
        TxIn input = new TxIn(
                txSourceId,       //TxId транзакции-источника
                outputIndex, //индекс выхода на транзакции-источнике
                new byte[][]{signature});              //подпись
        return input;
    }

    private TxIn createArbiterInput(
            Transaction txSource, byte[] senderSig, byte[] arbiterSig, int outputIndex)
    {
        byte[] txSourceId = txSource.getTxId();
        TxIn input = new TxIn(
                txSourceId,       //TxId транзакции-источника
                outputIndex, //индекс выхода на транзакции-источнике
                new byte[][]{senderSig, arbiterSig});              //подпись
        return input;
    }

    private void noContractMineBlock(Transaction txSource) {
        //пробуем намайнить блок с транзакцией, для другого пользователя
        //получатель
        int receiver = simulator.getNetwork().getRandomUser(getId());
        //публичный ключ предназначенный для выхода
        PublicKey receiverPubKey = pubKeys.get(receiver);
        //транзакция-источник
        TxIn input = createInput(txSource);
        //выход
        TxOut output = new TxOut(
                txSource.getOutput(input.getOutputIndex()).getSatoshisCount(), //количество биткоинов на выходе
                new PublicKey[]{receiverPubKey}, Contract.SIMPLE);                                        //условие выхода(сейчас не работает)
        mineBlock(input, output);
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
        //обработка запроса на сигнатуру
        if (message.getType() == 3) {
            byte[] signature = createMySign(((ProduceSignatureMessage)message).getArbiterTx().getTxId());
            ReturnSignatureMessage rsMessage = new ReturnSignatureMessage(
                    "Return signature",
                    signature,
                    ((ProduceSignatureMessage) message).getArbiterTx()
            );
            simulator.getNetwork().sendMessage(userId, senderId, rsMessage);
        }
        //конфликт в контракте с арбитром на этой стороне
        //тогда отправляем сообщение арбитру на создание нужной транзакции
        if(message.getType() == 5)
        {
            byte[] signature = createMySign(((ArbiterConflictMessage)message).getArbiterTx().getTxId());
            ArbiterResolveMessage arMessage = new ArbiterResolveMessage(
                    "Mine block with new output for me",
                    ((ArbiterConflictMessage)message).getArbiterTx(),
                    signature
            );
            simulator.getNetwork().sendMessage(userId, ((ArbiterConflictMessage)message).getArbId(), arMessage);
        }
        //если мы - арбитр, и следует создать транзакцию с нужными данными
        if(message.getType() == 4)
        {
            //создаем подпись арбитра
            Transaction arbiterTx = ((ArbiterResolveMessage)message).getArbiterTx();
            byte[] arbiterSig = createMySign(arbiterTx.getTxId());
            byte[] senderSig = ((ArbiterResolveMessage)message).getSignature();
            int outputIndex = arbiterTx.checkMultisigCondition(
                arbiterTx.getTxId(),
                new byte[][]{senderSig, arbiterSig});
            ////Вход
            TxIn input = createArbiterInput(
                    arbiterTx,
                    senderSig,
                    arbiterSig,
                    outputIndex);
            ////Выход
            TxOut output = new TxOut(
                    arbiterTx.getOutput(outputIndex).getSatoshisCount(),
                    new PublicKey[]{arbiterTx.getOutput(outputIndex).getCertainPublicKey(1)}, //публичный ключ получателя
                    Contract.SIMPLE
            );
            mineBlock(input, output);
        }
        //если вернули подпись без конфликта, то сами собираем нужный блок
        if(message.getType() == 6)
        {
            //создаем подпись арбитра
            Transaction arbiterTx = ((ReturnSignatureMessage)message).getArbiterTx();
            byte[] senderSig = createMySign(arbiterTx.getTxId());
            byte[] receiverSig = ((ReturnSignatureMessage)message).getSignature();
            int outputIndex = arbiterTx.checkMultisigCondition(
                    arbiterTx.getTxId(),
                    new byte[][]{senderSig, receiverSig});
            ////Вход
            TxIn input = createArbiterInput(
                    arbiterTx,
                    senderSig,
                    receiverSig,
                    outputIndex);
            ////Выход
            TxOut output = new TxOut(
                    arbiterTx.getOutput(outputIndex).getSatoshisCount(),
                    new PublicKey[]{arbiterTx.getOutput(outputIndex).getCertainPublicKey(1)}, //публичный ключ получателя
                    Contract.SIMPLE
            );
            mineBlock(input, output);
        }
    }

    private byte[] createMySign(byte[] txId)
    {
        byte[] signature = null;
        try {
            signature = Crypt.signData(
                    txId,
                    keyPair.getPrivate()
            );
        } catch (Exception ex) {
            System.err.println("Signature creation failed");
        }
        return signature;
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

    public static int getIdCounter() {
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
    public static Block mineStartBlock(int id, int time, PublicKey pk) {
        Block startBlock = null;
        try {
            startBlock = new Block(Integer.toString(id).getBytes(), time);
            //создаем транзакцию с выплатой создателю блока
            Transaction coinbaseTx = new Transaction();
            coinbaseTx.addOutput(new TxOut(coinbaseReward, new PublicKey[]{pk}, Contract.SIMPLE));
            startBlock.addTransaction(coinbaseTx);
        } catch (Exception ex) {
            ;
        }
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
