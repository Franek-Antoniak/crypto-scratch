package blockchain.miner;

import blockchain.Blockchain;
import blockchain.block.Block;
import blockchain.block.BlockBuilder;
import blockchain.block.BlockBuilderFactory;
import blockchain.messenger.Messenger;
import blockchain.mine.CryptoMine;
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
    private final CryptoMine cryptoMine;
    private final Messenger messenger;
    private volatile boolean active = true;

    public CryptoMiner() {
        cryptoMinerTools = new CryptoMinerTools();
        blockBuilderFactory = new BlockBuilderFactory();
        messenger = Messenger.getInstance();
        cryptoMine = CryptoMine.getInstance();
    }

    public void turnOffMiner() {
        active = false;
        cryptoMinerTools.turnOff();
    }

    public void awaitAndShutdownMainer(int awaitTime) {
        active = false;
        cryptoMinerTools.turnOff();
        try {
            join(awaitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void turnOffMining() {
        cryptoMinerTools.turnOff();
    }

    @Override
    public void run() {
        blockchain = Blockchain.getInstance();
        Optional<Block> lastBlock = blockchain.getLastBlock();
        BlockBuilder nextBlockBuilder = lastBlock.isPresent() ?
                blockBuilderFactory.getBlockBuilder(lastBlock.get())
                        .setListOfMessages(messenger.getFinalMessages()) :
                blockBuilderFactory.getBlockBuilder()
                        .setListOfMessages(messenger.getFinalMessages());
        while (active) {
            cryptoMinerTools.turnOn();
            Block nextBlock = cryptoMinerTools.mineBlock(nextBlockBuilder);
            boolean isAdded = blockchain.tryAddNewBlock(nextBlock, lastBlock);
            if (isAdded) {
                cryptoMine.stopMining();
                messenger.safeCurrentMessages();
            }
            if (lastBlock != blockchain.getLastBlock() && blockchain.getLastBlock()
                    .isPresent()) {
                lastBlock = blockchain.getLastBlock();
                nextBlockBuilder =
                        blockBuilderFactory.getBlockBuilder(lastBlock.get())
                                .setListOfMessages(messenger.getFinalMessages());
            }
        }
    }


}
