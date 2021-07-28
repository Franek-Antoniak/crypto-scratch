package blockchain.block;

import blockchain.messenger.MessageHolder;
import lombok.Getter;

import java.util.List;

/**
 * Builder pattern to encapsulate creating a Block.
 */

@Getter
public class BlockBuilder {
    private long index;
    private long timeStamp;
    /* Sha256 hash of Block*/
    private String hash;
    /* Sha256 hash of previous Block in a BlockChain */
    private String previousHash;
    private long magicNumber;
    private long generatingTime;
    private long authorId;
    private long amountOfZeros;
    private List<MessageHolder> messages;

    public BlockBuilder setIndex(long index) {
        this.index = index;
        return this;
    }

    public BlockBuilder setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
        return this;
    }

    public BlockBuilder setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public BlockBuilder setHash(String hash) {
        this.hash = hash;
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

    public BlockBuilder setAuthor(long authorId) {
        this.authorId = authorId;
        return this;
    }

    public BlockBuilder setAmountOfZeros(long amountOfZeros) {
        this.amountOfZeros = amountOfZeros;
        return this;
    }

    public BlockBuilder setMessages(List<MessageHolder> finalMessages) {
        this.messages = finalMessages;
        return this;
    }

    public Block build() {
        return new Block(index, timeStamp, hash, previousHash, magicNumber, generatingTime, authorId, amountOfZeros,
                messages);
    }
}
