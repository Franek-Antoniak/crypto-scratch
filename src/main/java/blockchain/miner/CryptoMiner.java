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
    private Blockchain blockchain;
    private final BlockBuilderFactory blockBuilderFactory;
    private final CryptoMine cryptoMine;
    private final Messenger messenger;
    private volatile boolean isWorking = false;
    private volatile boolean isMining = false;

    public CryptoMiner() {
        blockBuilderFactory = new BlockBuilderFactory();
        messenger = Messenger.getInstance();
        cryptoMine = CryptoMine.getInstance();
    }

    public void turnOffMiner() {
        isWorking = false;
        isMining = false;
    }

    public void awaitAndShutdownMainer(int awaitTime) {
        isWorking = false;
        isMining = false;
        try {
            join(awaitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Block mineBlock(BlockBuilder blockBuilder) {
        long startTime = new Date().getTime();
        long randomNumber;
        boolean isNotEnoughZeros;
        do {
            randomNumber = ThreadLocalRandom.current()
                    .nextLong(Long.MAX_VALUE);
            blockBuilder.setMagicNumber(randomNumber)
                    .setHash(getShaValue(blockBuilder));
            isNotEnoughZeros = !BlockUtil.isEnoughZeroInHash(blockBuilder.getHash(),
                    blockBuilder.getAmountOfZeros());
        } while (isMining && isNotEnoughZeros);
        long secondsOfGenerating = (new Date().getTime() - startTime) / 1000;
        return blockBuilder.setGeneratingTime(secondsOfGenerating)
                .setAuthor(Thread.currentThread().getId())
                .build();
    }

    public String getShaValue(BlockBuilder blockBuilder) {
        StringBuilder inputBuilder = new StringBuilder();
        return StringUtil.applySha256(inputBuilder
                .append(blockBuilder.getIndex())
                .append(blockBuilder.getTimeStamp())
                .append(blockBuilder.getPreviousHash())
                .append(blockBuilder.getMagicNumber())
                .append(blockBuilder.getGeneratingTime())
                .append(blockBuilder.getAuthorId())
                .append(blockBuilder.getAmountOfZeros())
                .append(blockBuilder.getMessages())
                .toString());
    }


    public void turnOffMining() {
        isMining = false;
    }

    @Override
    public void run() {
        blockchain = Blockchain.getInstance();
        isWorking = true;
        Optional<Block> lastBlock = blockchain.getLastBlock();
        BlockBuilder nextBlockBuilder = lastBlock.isPresent() ?
                blockBuilderFactory.getBlockBuilder(lastBlock.get())
                        .setMessages(messenger.getFinalMessages()) :
                blockBuilderFactory.getBlockBuilder()
                        .setMessages(messenger.getFinalMessages());
        while (isWorking) {
            isMining = true;
            Block nextBlock = mineBlock(nextBlockBuilder);
            boolean isAdded = lastBlock.map(block -> blockchain.tryAddNewBlock(nextBlock, block))
                    .orElseGet(() -> blockchain.tryAddNewBlock(nextBlock));
            if (isAdded) {
                cryptoMine.stopMining();
                messenger.safeCurrentMessages();
            }
            if (lastBlock != blockchain.getLastBlock() && blockchain.getLastBlock()
                    .isPresent()) {
                lastBlock = blockchain.getLastBlock();
                nextBlockBuilder =
                        blockBuilderFactory.getBlockBuilder(lastBlock.get())
                                .setMessages(messenger.getFinalMessages());
            }
        }
    }


}
