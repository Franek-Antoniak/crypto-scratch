package blockchain;

import blockchain.block.Block;
import blockchain.block.BlockFactory;
import lombok.Getter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Represents a Blockchain.
 */
@Getter
public class Blockchain {
    /* List that contains Block with info */
    private final LinkedList<Block> blockList = new LinkedList<>();

    public void addNewBlock(Block newBlock) {
        blockList.add(newBlock);
    }

    // FIXME: 06.07.2021 Don't know yet if it's good idea.
    public Block getLastBlock() {
        try {
            return blockList.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Returns a string representation of this Blockchain. The string
     * representation consists of a list of Blocks separated by new lines.
     * <p>Block are converted to strings by overridden method {@link Block#toString()}.</p>
     *
     * @return a string representation of Blockchain
     */
    @Override
    public String toString() {
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
