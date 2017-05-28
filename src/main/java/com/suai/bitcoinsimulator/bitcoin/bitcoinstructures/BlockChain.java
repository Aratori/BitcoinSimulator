package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Save information about blockchain
 */
public class BlockChain {
    private BitcoinNode node;
    private boolean staticMode;   //static/nonstatic
    private ArrayList<Block> blockchain = new ArrayList<Block>();
    //для статической версии
    private PublicKey publicKey;
    /**
     * Хранит список транзакций, из которых можно совершить вывод.
     */
    private ArrayList<Transaction> myTransactions = new ArrayList<Transaction>();

    public BlockChain(BitcoinNode node)
    {
        this.node = node;
        this.publicKey = node.getPublicKey();
        this.staticMode = false;
    }
    public BlockChain(PublicKey publicKey)
    {
        this.staticMode = true;
        this.publicKey = publicKey;
    }

    private boolean addBlock(Block block)
    {

        blockchain.add(block);

        return true;
    }

    /**
     * Node get from network. User verify block and if its true, block added to blockchain
     * @param block
     * @return
     */
    public boolean checkBlock(Block block) {
        //смотрим, есть повторяемая транзакция
        //идем по транзакциям нового блока
        for(int i = 0; i < block.size(); i++)
        {
            Transaction tx = block.getTransaction(i);
            //и сверяем ее со всеми транзакциями в блокчейне
            for(int j = 0; j < blockchain.size(); j++)
            {
                Block BCBlock = blockchain.get(j);
                for(int m = 0; m < BCBlock.size(); m++)
                {
                    Transaction BCTx = BCBlock.getTransaction(m);
                    if(tx.getTxId().equals(BCTx))
                        return false;
                }
            }
        }
        //собираем транзакции, в которых есть выходы на эту транзакцию
        //и собираем индексы нужных выходов
        ArrayList<Transaction> inputsTx = new ArrayList<Transaction>();
        ArrayList<Integer> inputsTxOutputIndex = new ArrayList<Integer>();
        Transaction myDeleteTx = null;
        boolean myTxTog = false;
        //проверяем, все ли входы выполняют условие получение биткоина
        for(int i = 0; i < block.size(); i++)
        {
            Transaction tx = block.getTransaction(i);
            //смотрим входы
            for(int j = 0; j < tx.getInputsSize(); j++)
            {
                TxIn input = tx.getInput(j);
                byte[] inputPrevHash = input.getPrevTxHash();
                boolean tog = false;    //выход не найден
                //ищем выходы с таким же хэшем
                for(int m = 0; m < blockchain.size(); m++)
                {
                    Block BCBlock = blockchain.get(m);
                    for(int k = 0; k < BCBlock.size(); k++)
                    {
                        Transaction BCBlockTransaction = BCBlock.getTransaction(k);
                        //если та самая транзакция, из которой берутся биткоины
                        byte[] BCBlockTxId = BCBlockTransaction.getTxId();
                        boolean eqId = true;
                        for(int r = 0; r < BCBlockTxId.length; r++)
                            if(BCBlockTxId[r] != inputPrevHash[r]) {
                                eqId = false;
                                break;
                            }
                        if(eqId) {
                            //если такой индекс существует
                            if(BCBlockTransaction.getOutputsSize() > input.getOutputIndex())
                            {
                                TxOut output = BCBlockTransaction.getOutput(input.getOutputIndex());
                                //если множество публичных ключей

                                //если проверка проходит
                                if(output.txOutputVerification(input.getPrevTxHash(), input.getSignature(0))) {
                                    tog = true;
                                    //добавляем для проверки на перевод верной суммы
                                    inputsTx.add(BCBlockTransaction);
                                    inputsTxOutputIndex.add(input.getOutputIndex());
                                    //обновляем myTransactions, если это был выход нашей транзакции
                                    if(output.getPublicKey().equals(publicKey)) {
                                        myDeleteTx = BCBlockTransaction;
                                        myTxTog = true;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                //проверка, что пересылается приемлемая сумма
                int inputsSatoshisCount = inputBalance(inputsTx, inputsTxOutputIndex);  //смотрим, что приходит на входы
                int outputSatoshisCount = outputBalance(tx); //смотрим, какие суммы на выходах
                if(outputSatoshisCount > inputsSatoshisCount)
                    return false;
                //удаляем использованную транзакцию, если она есть
                if(myTxTog)
                    deleteMyTransaction(myDeleteTx);
                //если выход не был найден, то блок вылетает
                if(!tog)
                    return false;
            }
        }
        //update my transactions
        findMyTransaction(block);


        //add block
        addBlock(block);
        return true;
    }

    public void deleteMyTransaction(Transaction used)
    {
        for(int i = 0; i < myTransactions.size(); i++)
        {
            if(myTransactions.get(i).equals(used)) {
                myTransactions.remove(i);
                break;
            }
        }
    }


    private Integer inputBalance(ArrayList<Transaction> myTx, ArrayList<Integer> myTxOutputIndex)
    {
       Integer inputBalance = 0;
       Iterator<Transaction> it = myTx.iterator();
       Iterator<Integer> index = myTxOutputIndex.iterator();

       while(it.hasNext())
           inputBalance += it.next().getOutput(index.next()).getSatoshisCount();

       return inputBalance;
    }

    private Integer outputBalance(Transaction tx)
    {
        Integer outputBalance = 0;

        for(int i = 0; i < tx.getOutputsSize(); i++)
            outputBalance += tx.getOutput(i).getSatoshisCount();

        return outputBalance;
    }

    /**
     * Ищет в блокчейне транзакции с выводом на адрес(pubkey) этой ноды.
     */
    public void findMyTransaction(Block block) {
        //идем по блоку, ищем выходы на наш публичный ключ
        for(int i = 0; i < block.size(); i++)
        {
            Transaction tx = block.getTransaction(i);
            for(int j = 0; j < tx.getOutputsSize(); j++)
            {
                TxOut output = tx.getOutput(j);
                if(output.getPublicKey().equals(publicKey))
                {
                    myTransactions.add(tx);
                    break;
                }
            }
        }
    }

    public ArrayList<Transaction> getMyTransactions()
    {
        return myTransactions;
    }

    public Block getBlock(int index)
    {
        return blockchain.get(index);
    }

    public Block getLastBlock()
    {
        return blockchain.get(blockchain.size() - 1);
    }

    public int size()
    {
        return blockchain.size();
    }

    @Override
    public String toString()
    {
        String str = "BlockChain:\n";

        for(int i = 0; i < blockchain.size(); i++)
            str += blockchain.get(i).toString() + "\n";

        return str;
    }
}
