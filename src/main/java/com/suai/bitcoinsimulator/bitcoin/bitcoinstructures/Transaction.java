package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import java.util.ArrayList;

/**
 * Transaction
 */
public class Transaction {
    /**
     * TxId - идентификатор транзакции, являющийся хэшом по ней.
     * В оригинальной структуре его нет, но для удобства был сюда помещен.
     */
    private int TxId;
    private static int TxIdCounter = 0;
    private ArrayList<TxIn> inputs = new ArrayList<TxIn>();
    private ArrayList<TxOut> outputs = new ArrayList<TxOut>();

    public Transaction()
    {
        TxId =TxIdCounter;
        TxIdCounter++;
    }

    /**
     * Идем по выводам и смотрим, к каким транзакциям можно получить доступ данному ключу
     * @param privKey
     * @return  возвращает индекс нужного выхода
     */
    public int checkCondition(int privKey)
    {
        for(int i = 0; i < outputs.size(); i++)
        {
            if(outputs.get(i).txOutputVerification(privKey))
                return i;
        }

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

    public int getTxId()
    {
        return TxId;
    }
}
