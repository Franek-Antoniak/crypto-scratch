package blockchain.block;

import blockchain.miner.CryptoMinerTools;

/**
 * Factory method pattern with overloading methods to encapsulate creating a Block.
 */
public class BlockFactory {

    private final BlockBuilder blockBuilder;
    private final CryptoMinerTools cryptoMinerTools;

    public BlockFactory(CryptoMinerTools cryptoMinerTools) {
        blockBuilder = new BlockBuilder();
        this.cryptoMinerTools = cryptoMinerTools;
    }


    /**
     * Public method to create next Block in Blockchain by mining it.
     * Calls methods to mine based on whether the specified object has a reference or is null.
     *
     * @param previousBlock Previous block in Blockchain
     * @return new Block
     */
    public Block createNewBlock(Block previousBlock) {
        if (previousBlock == null) return getNextBlock();
        return getNextBlock(previousBlock);
    }


    /**
     * Private method to mine new Block in BlockChain based on previous Block
     *
     * @param previousBlock Previous block in Blockchain
     * @return new Block
     */
    private Block getNextBlock(Block previousBlock) {
        Block block = blockBuilder.setPreviousHash(previousBlock.getHash())
                .createNewIndex(previousBlock.getIndex())
                .generateTimeStamp()
                .setMagicNumber(cryptoMinerTools.findMagicNumber(blockBuilder))
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
    private Block getNextBlock() {
        Block block = blockBuilder.setPreviousHash("0")
                .createNewIndex(0)
                .generateTimeStamp()
                .setMagicNumber(cryptoMinerTools.findMagicNumber(blockBuilder))
                .generateHash()
                .build();
        blockBuilder.reset();
        return block;
    }
}
