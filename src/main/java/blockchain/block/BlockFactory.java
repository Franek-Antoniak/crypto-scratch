package blockchain.block;

/**
 * Factory method pattern with overloading methods to encapsulate creating a Block.
 */
public class BlockFactory {

    private final BlockBuilder blockBuilder;

    public BlockFactory() {
        blockBuilder = new BlockBuilder();
    }


    /**
     * Public method to create next Block in Blockchain by mining it.
     * Calls methods to mine based on whether the specified object has a reference or is null.
     *
     * @param previousBlock Previous block in Blockchain
     * @return new Block
     */
    public Block createNewBlock(Block previousBlock) {
        if (previousBlock == null) return mineNewBlock();
        return mineNewBlock(previousBlock);
    }


    /**
     * Private method to mine new Block in BlockChain based on previous Block
     *
     * @param previousBlock Previous block in Blockchain
     * @return new Block
     */
    private Block mineNewBlock(Block previousBlock) {
        Block block = blockBuilder.setPreviousHash(previousBlock.getHash())
                .createNewIndex(previousBlock.getIndex())
                .generateTimeStamp()
                .generateHash()
                .build();
        blockBuilder.reset();
        return block;
    }

    /**
     * Method to mine first Block in Blockchain.
     *
     * @return new Block
     */
    private Block mineNewBlock() {
        Block block = blockBuilder.setPreviousHash("0")
                .createNewIndex(0)
                .generateTimeStamp()
                .generateHash()
                .build();
        blockBuilder.reset();
        return block;
    }
}
