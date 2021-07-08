package blockchain.block;

import blockchain.util.StringUtil;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Builder pattern to encapsulate creating a Block.
 */
@Data
public class BlockBuilder {
    private long index;
    private long timeStamp;
    /* Sha256 hash of Block*/
    private String hash;
    /* Sha256 hash of previous Block in a BlockChain */
    private String previousHash;
    private long magicNumber;
    private long generatingTime;

    /**
     * Generate new Index by adding one to the previousIndex
     *
     * @param previousIndex Index of the preceding block in the blockchain
     */
    public BlockBuilder createNewIndex(long previousIndex) {
        this.index = ++previousIndex;
        return this;
    }

    public BlockBuilder setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
        return this;
    }

    public BlockBuilder generateTimeStamp() {
        this.timeStamp = new Date().getTime();
        return this;
    }

    public BlockBuilder generateHash() {
        hash = StringUtil.applySha256(index + timeStamp + previousHash + magicNumber);
        return this;
    }

    public BlockBuilder setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
        return this;
    }

    public BlockBuilder setGeneratingTime(long generatingTime) {
        this.generatingTime = generatingTime;
        return this;
    }

    public void reset() {
        index = -1;
        timeStamp = 0;
        hash = "";
        previousHash = "";
    }

    public Block build() {
        return new Block(index, timeStamp, hash, previousHash, magicNumber, generatingTime);
    }
}
