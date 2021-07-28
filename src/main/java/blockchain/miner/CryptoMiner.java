package blockchain.miner;

import blockchain.Blockchain;
import blockchain.block.Block;
import blockchain.block.BlockBuilder;
import blockchain.block.BlockBuilderFactory;
import blockchain.block.util.BlockUtil;
import blockchain.messenger.Messenger;
import blockchain.mine.CryptoMine;
import blockchain.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a CryptoMiner.
 * CryptoMiner can mine blocks and add to an already existing BlockChain.
 * Constructor accept Blockchain that we currently working on.
 * Class uses blockFactory to make new Blocks.
 */
@Getter
@Setter
public class CryptoMiner extends Thread {
    private final BlockBuilderFactory blockBuilderFactory = new BlockBuilderFactory();
    private final CryptoMine cryptoMine = CryptoMine.getInstance();
    private final Messenger messenger = Messenger.getInstance();
    private Blockchain blockchain;
    private volatile boolean isWorking = false;
    private volatile boolean isMining = false;

    public CryptoMiner() {
    }

    public void turnOffMiner() {
        isWorking = false;
        isMining = false;
    }

    private Block mineBlock(BlockBuilder blockBuilder) {
        isMining = true;
        long startTime = new Date().getTime();
        long randomNumber;
        boolean isNotEnoughZeros;
        do {
            randomNumber = ThreadLocalRandom.current()
                    .nextLong(Long.MAX_VALUE);
            blockBuilder.setMagicNumber(randomNumber)
                    .setHash(getShaValue(blockBuilder));
            isNotEnoughZeros = !BlockUtil.isEnoughZeroInHash(blockBuilder.getHash(), blockBuilder.getAmountOfZeros());
        } while (isMining && isNotEnoughZeros);
        long secondsOfGenerating = (new Date().getTime() - startTime) / 1000;
        return blockBuilder.setGeneratingTime(secondsOfGenerating)
                .setAuthor(Thread.currentThread().getId())
                .build();
    }

    private String getShaValue(BlockBuilder blockBuilder) {
        StringBuilder inputBuilder = new StringBuilder();
        return StringUtil.applySha256(inputBuilder
                .append(blockBuilder.getIndex())
                .append(blockBuilder.getTimeStamp())
                .append(blockBuilder.getPreviousHash())
                .append(blockBuilder.getGeneratingTime())
                .append(blockBuilder.getAuthorId())
                .append(blockBuilder.getAmountOfZeros())
                .append(blockBuilder.getMessages())
                .append(blockBuilder.getMagicNumber())
                .toString());
    }


    public void turnOffMining() {
        isMining = false;
    }

    private void initializeMiner() {
        blockchain = Blockchain.getInstance();
        isWorking = true;
    }

    @Override
    public void run() {
        initializeMiner();
        while (isWorking) {
            Optional<Block> lastBlock = blockchain.getLastBlock();
            BlockBuilder nextBlockBuilder = lastBlock.map(blockBuilderFactory::getBuilder)
                    .orElseGet(blockBuilderFactory::getBuilder);
            Block nextBlock = mineBlock(nextBlockBuilder);
            /* If isMining equals to true then we know that someone found new block and turned off this miner,
            that's why we don't need to check if we should add this block to blockchain */
            if (isMining) {
                boolean isAdded = lastBlock.map(block -> blockchain.tryAddNewBlock(nextBlock, block))
                        .orElseGet(() -> blockchain.tryAddNewBlock(nextBlock));
                if (isAdded) {
                    cryptoMine.stopMining();
                    messenger.safeCurrentMessages();
                }
            }
        }
    }


}
