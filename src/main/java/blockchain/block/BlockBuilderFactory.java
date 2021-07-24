package blockchain.block;

import lombok.Data;

import java.util.Optional;

/**
 * Factory method pattern with overloading methods to encapsulate creating a Block.
 */
@Data
public class BlockBuilderFactory {
    /**
     * Private method to mine new Block in BlockChain based on previous Block
     *
     * @param previousBlock Previous block in Blockchain
     * @return new BlockBuilder
     */
    public BlockBuilder getBlockBuilder(Block previousBlock) {
        return new BlockBuilder()
                .setPreviousHash(previousBlock.getHash())
                .createNewIndex(previousBlock.getIndex())
                .changeAmountOfZeros(previousBlock.getAmountOfZeros(), previousBlock.getGeneratingTime())
                .generateTimeStamp();
    }

    /**
     * Method to mine first Block in Blockchain.
     *
     * @return new BlockBuilder
     */
    public BlockBuilder getBlockBuilder() {
        return new BlockBuilder()
                .setPreviousHash("0")
                .createNewIndex(0)
                .changeAmountOfZeros(0, 0)
                .generateTimeStamp();
    }
}
