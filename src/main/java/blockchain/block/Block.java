package blockchain.block;

import blockchain.messenger.MessageHolder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * Represents a Block of a BlockChain.
 *
 * @author https://github.com/Franek-Antoniak
 * <p> Formula of hash in the block: </p> {@link BlockBuilder#generateHash()}
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Block {
    private final long index;
    private final long timeStamp;
    /* Sha256 hash of Block*/
    private final String hash;
    /* Sha256 hash of previous Block in a BlockChain */
    private final String previousHash;
    private final long magicNumber;
    /* Time in seconds to generate this block */
    private final long generatingTime;
    private final long authorId;
    private final long amountOfZeros;
    private final List<MessageHolder> messages;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String dataBlock = "Block data:" + (messages.isEmpty() ? " no messages" : "");
        StringBuilder messagesBuilder = new StringBuilder();
        messages.forEach(messagesBuilder::append);
        String authorInfo = "Block: \n" + "Created by miner # " + authorId + '\n';
        String idInfo = "Id: " + index + '\n';
        String timestampInfo = "Timestamp: " + timeStamp + '\n';
        String magicNumberInfo = "Magic number: " + magicNumber + '\n';
        String previousHashInfo = "Hash of the previous block:\n" + previousHash + '\n';
        String hashOfTheBlockInfo = "Hash of the block:\n" + hash + '\n';
        String dataBlockInfo = dataBlock + '\n' + messagesBuilder;
        String generatingTimeInfo = "Block was generating for: " + generatingTime + " seconds" + '\n';
        String zerosChangesInfo;
        if (generatingTime < 5)
            zerosChangesInfo = "N was increased to " + (amountOfZeros + 1);
        // You can create some math function
        else if (generatingTime > 60)
            zerosChangesInfo = "N was decreased by 1";
        else
            zerosChangesInfo = "N stays the same";
        return stringBuilder
                .append(authorInfo)
                .append(idInfo)
                .append(timestampInfo)
                .append(magicNumberInfo)
                .append(previousHashInfo)
                .append(hashOfTheBlockInfo)
                .append(dataBlockInfo)
                .append(generatingTimeInfo)
                .append(zerosChangesInfo)
                .toString();
    }
}
