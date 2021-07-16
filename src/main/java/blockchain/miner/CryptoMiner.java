package blockchain.miner;

import blockchain.Blockchain;
import blockchain.block.Block;
import blockchain.block.BlockBuilder;
import blockchain.block.BlockBuilderFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Represents a CryptoMiner.
 * CryptoMiner can mine blocks and add to an already existing BlockChain.
 * Constructor accept Blockchain that we currently working on.
 * Class uses blockFactory to make new Blocks.
 */
@Getter
@Setter
public class CryptoMiner extends Thread {
    private Blockchain blockchain;
    private final CryptoMinerTools cryptoMinerTools;
    private final BlockBuilderFactory blockBuilderFactory;
    private volatile boolean active = true;

    public CryptoMiner() {
        cryptoMinerTools = new CryptoMinerTools();
        blockBuilderFactory = new BlockBuilderFactory();
    }

    public void turnOffMiner() {
        active = false;
        cryptoMinerTools.turnOff();
    }

    public void turnOffMining() {
        cryptoMinerTools.turnOff();
    }

    @Override
    public void run() {
        blockchain = Blockchain.getInstance();
        Optional<Block> lastBlock = blockchain.getLastBlock();
        BlockBuilder nextBlockBuilder = blockBuilderFactory.getBlockBuilder(lastBlock);
        while (active) {
            cryptoMinerTools.turnOn();
            if (lastBlock != blockchain.getLastBlock()) {
                lastBlock = blockchain.getLastBlock();
                nextBlockBuilder = blockBuilderFactory.getBlockBuilder(lastBlock);
            }
            Block nextBlock = cryptoMinerTools.mineBlock(nextBlockBuilder);
            blockchain.tryAddNewBlock(nextBlock, lastBlock);
        }
    }


}
