package blockchain.block;

import blockchain.messenger.Messenger;
import lombok.Data;

import java.util.Date;

/**
 * Factory method pattern with overloading methods to encapsulate creating a Block.
 */
@Data
public class BlockBuilderFactory {
    private final Messenger messenger = Messenger.getInstance();
    /**
     * Private method to mine new Block in BlockChain based on previous Block
     *
     * @param previousBlock Previous block in Blockchain
     * @return new BlockBuilder
     */
    public BlockBuilder getBuilder(Block previousBlock) {
        long amountOfZeros = previousBlock.getAmountOfZeros();
        long generatingTime = previousBlock.getGeneratingTime();
        if (generatingTime > 60)
            amountOfZeros--;
        else if (generatingTime < 5)
            amountOfZeros++;
        return new BlockBuilder()
                .setPreviousHash(previousBlock.getHash())
                .setIndex(previousBlock.getIndex() + 1)
                .setAmountOfZeros(amountOfZeros)
                .setTimeStamp(new Date().getTime())
                .setMessages(messenger.getFinalMessages());
    }

    /**
     * Method to mine first Block in Blockchain.
     *
     * @return new BlockBuilder
     */
    public BlockBuilder getBuilder() {
        return new BlockBuilder()
                .setPreviousHash("0")
                .setIndex(1)
                .setAmountOfZeros(0)
                .setTimeStamp(new Date().getTime())
                .setMessages(messenger.getFinalMessages());
    }
}
