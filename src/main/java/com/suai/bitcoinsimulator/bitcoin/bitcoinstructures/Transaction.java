package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.simulator.utils.Crypt;

import java.security.PrivateKey;
import java.util.ArrayList;

/**
 * Transaction
 */
public class Transaction {
    /**
     * TxId - идентификатор транзакции, являющийся хэшом по ней.
     * В оригинальной структуре его нет, но для удобства был сюда помещен.
     */
    private ArrayList<TxIn> inputs = new ArrayList<TxIn>();
    private ArrayList<TxOut> outputs = new ArrayList<TxOut>();

    public Transaction()
    {
    }

    /**
     * Идем по выводам и смотрим, к каким транзакциям можно получить доступ данному ключу
     * @param
     * @return  возвращает индекс нужного выхода
     */
    public int checkCondition(byte[] data, byte[] signature)
    {
        for(int i = 0; i < outputs.size(); i++)
        {
            if(outputs.get(i).txOutputVerification(data, signature))
                return i;
        }

        return -1;
    }

    public int checkMultisigCondition(byte[] data, byte[][] signatures)
    {
        for(int i = 0; i < outputs.size(); i++)
            if(outputs.get(i).txMultisigOutputVerification(data, signatures))
                return i;

        return -1;
    }

    public void addInput(TxIn input)
    {
        inputs.add(input);
    }

    public void addOutput(TxOut output)
    {
        outputs.add(output);
    }

    public TxOut getOutput(int index)
    {
        return outputs.get(index);
    }

    public TxIn getInput(int index)
    {
        return inputs.get(index);
    }

    public int getInputsSize()
    {
        return inputs.size();
    }

    public int getOutputsSize()
    {
        return outputs.size();
    }

    /**
     * Compute hash of transaction
     * @return TxId - hash of transaction
     */
    public byte[] getTxId()
    {
        //cчитаем хэш транзакции
        String tx = null;
        for(int i = 0; i < inputs.size(); i++)
            tx += inputs.get(i).toString();
        for(int i = 0; i < outputs.size(); i++)
            tx += outputs.get(i).toString();
        return Crypt.getHash(tx);
    }

    @Override
    public String toString()
    {
        String str = "Transaction ";
        byte[] TxId = getTxId();
        for(int i = 0; i < TxId.length && i < 4; i++)
            str += String.format("%x", TxId[i]);
        str += "\n";
        for(int i = 0; i < outputs.size(); i++)
            str += outputs.get(i).toString() + "\n";
        for(int i = 0; i < inputs.size(); i++)
            str += inputs.get(i).toString() + "\n";
        return str;
    }
}
