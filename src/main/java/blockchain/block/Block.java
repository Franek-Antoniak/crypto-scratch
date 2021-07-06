package blockchain.block;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a Block of a BlockChain.
 *
 * @author https://github.com/Franek-Antoniak
 */
@Getter
@AllArgsConstructor
public class Block {
    private final long index;
    private final long timeStamp;
    /* Sha256 hash of Block*/
    private final String hash;
    /* Sha256 hash of previous Block in a BlockChain */
    private final String previousHash;

    @Override
    public String toString() {
        return "Block: \n" +
                "Id: " + index + '\n' +
                "Timestamp: " + timeStamp + '\n' +
                "Hash of the previous block:\n" + previousHash + '\n' +
                "Hash of the block:\n" + hash + '\n';
    }
}
