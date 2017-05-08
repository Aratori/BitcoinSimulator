package com.suai.bitcoinsimulator.bitcoin.bitcoinstructures;

import com.suai.bitcoinsimulator.simulator.utils.Crypt;

/**
 * Created by anton on 30.04.17.
 */
public class BlockHeader {
    /**
     * A SHA256(SHA256()) hash in internal byte order of the previous block’s header.
     * This ensures no previous block can be changed without also changing this block’s header.
     */
    private byte[] prevBlockHeaderHash;
    /**
     * A SHA256(SHA256()) hash in internal byte order.
     * The merkle root is derived from the hashes of all transactions included in this block,
     * ensuring that none of those transactions can be modified without modifying the header.
     * See the merkle trees section below.
     * (not using in this simulator)
     */
    private String mercleRootHash;
    /**
     * The block time is a Unix epoch time when the miner started hashing the header (according to the miner).
     * Must be strictly greater than the median time of the previous 11 blocks.
     * Full nodes will not accept blocks with headers more than two hours in the future according to their clock.
     */
    int time;
    /**
     * An encoded version of the target threshold
     * this block’s header hash must be less than or equal to.
     * (not using in this simulator)
     */
    int nBits = 0;
    /**
     * An arbitrary number miners change to modify the header hash
     * in order to produce a hash less than or equal to the target threshold.
     * If all 32-bit values are tested,
     * the time can be updated or the coinbase transaction can be changed and the merkle root updated.
     * (not using in this simulator)
     */
    int nonce = 0;

    public BlockHeader(byte[] prevBlockHeaderHash, int currentTime)
    {
        this.time = currentTime;
        this.prevBlockHeaderHash = prevBlockHeaderHash;
    }

    public byte[] getBlockHeaderHash()
    {
        return Crypt.getHash(this.toString());
    }

    @Override
    public String toString()
    {
        String str = "Block Header::";

        str += "Prev Block Header Hash:";
        for(int i = 0; i < prevBlockHeaderHash.length && i < 5; i++)
            str += String.format("%x", prevBlockHeaderHash[i]);
        str += "; ";
        str += "time:" + String.format("%3d", time);
        return str;
    }
}
