package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.simulator.Simulator;
import com.suai.bitcoinsimulator.bitcoin.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.nodes.User;
import com.suai.bitcoinsimulator.simulator.utils.Crypt;

import java.security.PublicKey;

/**
 * Created by anton on 26.04.17.
 */
public class Main {

    /*public static void testJCE()
    {
        try {
            //выбираем хэш-функцию
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            Transaction tx = new Transaction();
            tx.addOutput(new TxOut(1, 1));
            //получаем строку(так проще будет переводить в строку байт
            String txString = Integer.toString(tx.getOutput(0).getSatoshisCount());
            //считаем хэш
            hash.update(txString.getBytes("UTF-8"));
            //MessageDigest
            byte[] hashDigest = hash.digest();
            System.out.println("Хэш:");
            String hexFormat = String.format("%064x", new java.math.BigInteger(1, hashDigest));
            System.out.println(hexFormat);

            //ключи
            KeyPair keyPair = generateKeyPair(4242);

            //подпись и проверка
            byte[] digSignature = signData(hashDigest,keyPair.getPrivate());


            System.out.println("Подпись:" );
            System.out.println(String.format("%064x", new java.math.BigInteger(1, digSignature)));

            System.out.println("Проверка подписи:");
            System.out.println(verifySig(hashDigest, keyPair.getPublic(), digSignature));

        }catch(NoSuchAlgorithmException ex)
        {
            ;
        }
        catch(UnsupportedEncodingException ex)
        {
            ;
        }
        catch(Exception ex)
        {

        }
    }*/

    public static void main(String[] args) {
        Simulator.restartSimulator("config.conf");
    }
}
