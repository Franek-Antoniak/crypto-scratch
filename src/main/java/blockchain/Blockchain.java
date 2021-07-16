package blockchain;

import blockchain.block.Block;
import blockchain.block.util.BlockUtil;
import blockchain.mine.CryptoMine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Represents a Blockchain.
 */
@Setter
public class Blockchain {
    /* List that contains Block with info */
    private final LinkedList<Block> blockList = new LinkedList<>();
    @JsonIgnore
    private final CryptoMine cryptoMinerMine = CryptoMine.getInstance();

    private Blockchain() {
    }

    private static class BlockChainSingleton {
        private static final Blockchain instance = new Blockchain();
    }

    public static Blockchain getInstance() {
        return BlockChainSingleton.instance;
    }

    public synchronized void tryAddNewBlock(Block newBlock, Optional<Block> lastBlock) {
        if (lastBlock.equals(getLastBlock()) && BlockUtil.isEnoughZeroInHash(newBlock.getHash(), newBlock.getAmountOfZeros())) {
            blockList.add(newBlock);
            cryptoMinerMine.stopMining();
        }
    }

    @JsonIgnore
    public Optional<Block> getLastBlock() {
        try {
            return Optional.of(blockList.getLast());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public int sizeOfBlockchain() {
        return blockList.size();
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
