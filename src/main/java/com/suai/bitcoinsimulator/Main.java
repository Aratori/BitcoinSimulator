package com.suai.bitcoinsimulator;

import com.suai.bitcoinsimulator.bitcoin.BitcoinSimulator;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.Transaction;
import com.suai.bitcoinsimulator.bitcoin.bitcoinstructures.TxOut;
import com.suai.bitcoinsimulator.bitcoin.nodes.BitcoinNode;
import com.suai.bitcoinsimulator.simulator.nodes.User;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by anton on 26.04.17.
 */
public class Main {

    public static void testJCE()
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
    }

    public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initVerify(key);
        signer.update(data);
        return (signer.verify(sig));

    }

    public static KeyPair generateKeyPair(long seed) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
        SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
        rng.setSeed(seed);
        keyGenerator.initialize(1024, rng);

        return (keyGenerator.generateKeyPair());
    }

    public static void main(String[] args)
    {
        boolean JCETest = true;

        if(!JCETest) {
            BitcoinSimulator bs = new BitcoinSimulator(400, 6, 100, true);
            User userOne = new BitcoinNode(10, 20, bs);
            User userTwo = new BitcoinNode(20, 30, bs);
            User userThree = new BitcoinNode(15, 40, bs);
            User userFour = new BitcoinNode(80, 10, bs);
            User userFive = new BitcoinNode(45, 50, bs);
            User userSix = new BitcoinNode(45, 60, bs);

            bs.startSimulation();
        }
        else
        {
            testJCE();
        }
    }
}
