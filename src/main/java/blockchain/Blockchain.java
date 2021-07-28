package blockchain;

import blockchain.block.Block;
import blockchain.block.util.BlockUtil;
import blockchain.mine.CryptoMine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Represents a Blockchain.
 */
@Setter
public class Blockchain {
    /* List that contains Block with info */
    private final LinkedList<Block> blockList = new LinkedList<>();

    private Blockchain() {
    }

    private static class BlockChainSingleton {
        private static final Blockchain instance = new Blockchain();
    }

    public static Blockchain getInstance() {
        return BlockChainSingleton.instance;
    }

    public synchronized boolean isBlockListEmpty() {
        return blockList.isEmpty();
    }

    public synchronized boolean tryAddNewBlock(Block newBlock, Block lastBlock) {
        Optional<Block> blockchainLastBlock = getLastBlock();
        if (blockchainLastBlock.isPresent() && !isBlockListEmpty()) {
            long amountOfZeros = BlockUtil.howManyZerosInNext(lastBlock);
            boolean isEnoughZeros = BlockUtil.isEnoughZeroInHash(newBlock.getHash(), amountOfZeros);
            boolean isTheSameLastBlock = lastBlock.equals(blockchainLastBlock.get());
            if (isTheSameLastBlock && isEnoughZeros) {
                blockList.add(newBlock);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean tryAddNewBlock(Block newBlock) {
        if (isBlockListEmpty()) {
            boolean isEnoughZeros = BlockUtil.isEnoughZeroInHash(newBlock.getHash(), 0);
            if (isEnoughZeros) {
                blockList.add(newBlock);
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public Optional<Block> getLastBlock() {
        if (isBlockListEmpty())
            return Optional.empty();
        return Optional.of(blockList.getLast());
    }

    /**
     * Returns a string representation of this Blockchain. The string
     * representation consists of a list of Blocks separated by new lines.
     * <p>Block are converted to strings by overridden method {@link Block#toString()}.</p>
     *
     * @return a string representation of Blockchain
     */
    @Override
    public synchronized String toString() {
        Iterator<Block> it = blockList.iterator();
        if (!it.hasNext())
            return "";
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            Block block = it.next();
            sb.append(block);
            if (!it.hasNext())
                return sb.toString();
            sb.append('\n');
        }
    }
}
