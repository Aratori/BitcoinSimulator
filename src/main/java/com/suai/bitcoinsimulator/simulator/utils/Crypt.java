package com.suai.bitcoinsimulator.simulator.utils;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by anton on 04.05.17.
 */
public class Crypt {


    public static byte[] getHash(String data)
    {
        byte[] hashDigest = null;
        try {
            //выбираем хэш-функцию
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            //считаем хэш
            hash.update(data.getBytes("UTF-8"));
            //MessageDigest
            hashDigest = hash.digest();
        }catch(NoSuchAlgorithmException ex)
            {
                ;
            }
            catch(UnsupportedEncodingException ex)
            {
                ;
            }

        return hashDigest;
    }
    /**
     * Sign data
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    /**
     * Verify data
     * @param data
     * @param key
     * @param sig
     * @return
     * @throws Exception
     */
    public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initVerify(key);
        signer.update(data);
        return (signer.verify(sig));

    }

    /**
     * Generate key pair
     * @param seed
     * @return
     * @throws Exception
     */
    public static KeyPair generateKeyPair(long seed) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
        SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
        rng.setSeed(seed);
        keyGenerator.initialize(1024, rng);

        return (keyGenerator.generateKeyPair());
    }

}
