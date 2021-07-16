package blockchain.miner;

import blockchain.block.Block;
import blockchain.block.BlockBuilder;
import blockchain.block.util.BlockUtil;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class CryptoMinerTools {
    private volatile boolean active = true;

    public Block mineBlock(BlockBuilder blockBuilder) {
        long startTime = new Date().getTime();
        long randomNumber;
        do {
            randomNumber = ThreadLocalRandom.current()
                    .nextLong(Long.MAX_VALUE);
            blockBuilder.setMagicNumber(randomNumber)
                    .generateHash();
        } while (!BlockUtil.isEnoughZeroInHash(blockBuilder.getHash(), blockBuilder.getAmountOfZeros()) && active);
        long secondsOfGenerating = (new Date().getTime() - startTime) / 1000;
        blockBuilder.setGeneratingTime(secondsOfGenerating)
                .setAuthor(Thread.currentThread()
                        .getId());
        return blockBuilder.build();
    }

    public void turnOff() {
        active = false;
    }

    public void turnOn() {
        active = true;
    }
}
