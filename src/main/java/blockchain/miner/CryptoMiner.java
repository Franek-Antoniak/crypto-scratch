package blockchain.miner;

import blockchain.Blockchain;
import blockchain.block.Block;
import blockchain.block.BlockFactory;
import lombok.RequiredArgsConstructor;

/**
 * Represents a CryptoMiner.
 * CryptoMiner can mine blocks and add to an already existing BlockChain.
 * Constructor accept Blockchain that we currently working on.
 * Class uses blockFactory to make new Blocks.
 */
@RequiredArgsConstructor
public class CryptoMiner {
    private final Blockchain blockchain;
    private final CryptoMinerTools cryptoMinerTools;
    private final BlockFactory blockFactory;

    public CryptoMiner(Blockchain blockchain, long magicNumber) {
        this.blockchain = blockchain;
        cryptoMinerTools = new CryptoMinerTools(magicNumber);
        blockFactory = new BlockFactory(cryptoMinerTools);
    }

    public void mineBlocks(long amountToMine) {
        for (int i = 0; i < amountToMine; i++) {
            mineBlock();
        }
    }

    private void mineBlock() {
        Block lastBlock = blockchain.getLastBlock();
        Block newBlock = blockFactory.createNewBlock(lastBlock);
        blockchain.addNewBlock(newBlock);
    }

}
