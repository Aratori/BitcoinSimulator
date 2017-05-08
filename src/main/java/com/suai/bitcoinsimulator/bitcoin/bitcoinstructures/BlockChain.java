package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;

import java.util.ArrayList;

/**
 * Save information about blockchain
 */
public class BlockChain {
    private BitcoinNode node;
    private ArrayList<Block> blockchain = new ArrayList<Block>();
    /**
     * Хранит список транзакций, из которых можно совершить вывод.
     */
    private ArrayList<Transaction> myTransactions = new ArrayList<Transaction>();

    public BlockChain(BitcoinNode node)
    {
        this.node = node;
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

        //проверяем, все ли входы выполняют условие получение биткоина
        for(int i = 0; i < block.size(); i++)
        {
            Transaction tx = block.getTransaction(i);
            //смотрим входы
            for(int j = 0; j < tx.getInputsSize(); j++)
            {
                TxIn input = tx.getInput(j);
                boolean tog = false;    //выход не найден
                //ищем выходы с таким же хэшем
                for(int m = 0; m < blockchain.size(); m++)
                {
                    Block BCBlock = blockchain.get(m);
                    for(int k = 0; k < BCBlock.size(); k++)
                    {
                        Transaction BCBlockTransaction = BCBlock.getTransaction(k);
                        //если та самая транзакция, из которой берутся биткоины
                        if(BCBlockTransaction.getTxId().equals(input.getPrevTxHash())) {
                            //если такой индекс существует
                            if(BCBlockTransaction.getOutputsSize() > input.getOutputIndex())
                            {
                                TxOut output = BCBlockTransaction.getOutput(input.getOutputIndex());
                                //если проверка проходит
                                if(output.txOutputVerification(input.getPrevTxHash(), input.getSignature())) {
                                    tog = true;
                                    //обновляем myTransactions, если это был выход нашей транзакции
                                    if(output.getPublicKey().equals(node.getPublicKey()))
                                        deleteMyTransaction(BCBlockTransaction);
                                    break;
                                }
                            }
                        }
                    }
                }
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
                if(output.getPublicKey().equals(node.getPublicKey()))
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
