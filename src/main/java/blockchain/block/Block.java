package blockchain.block;

import blockchain.messenger.MessageHolder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Block of a BlockChain.
 *
 * @author https://github.com/Franek-Antoniak
 * Formula of hash in the block:
 * StringUtil.applySha256(
 * String.valueOf(index) + String.valueOf(timeStamp) + previousHash + String.valueOf(magicNumber));
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
        stringBuilder.append("Block: \n" + "Created by miner # ")
                .append(authorId)
                .append('\n')
                .append("Id: ")
                .append(index)
                .append('\n')
                .append("Timestamp: ")
                .append(timeStamp)
                .append('\n')
                .append("Magic number: ")
                .append(magicNumber)
                .append('\n')
                .append("Hash of the previous block:\n")
                .append(previousHash)
                .append('\n')
                .append("Hash of the block:\n")
                .append(hash)
                .append('\n')
                .append(dataBlock)
                .append('\n')
                .append(messagesBuilder)
                .append("Block was generating for: ")
                .append(generatingTime)
                .append(" seconds")
                .append('\n');
        if (generatingTime < 5)
            stringBuilder.append("N was increased to ")
                    .append(amountOfZeros + 1);
        else if (generatingTime > 60)
            stringBuilder.append("N was decreased by ")
                    .append(1);
        else
            stringBuilder.append("N stays the same");
        return stringBuilder.append('\n')
                .toString();
    }
}
