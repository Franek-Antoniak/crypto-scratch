package blockchain.miner;

import blockchain.block.BlockBuilder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class CryptoMinerTools {
    private final long numberOfZeros;

    public long findMagicNumber(BlockBuilder blockBuilder) {
        long startTime = new Date().getTime();
        long randomNumber;
        do {
            randomNumber = ThreadLocalRandom.current()
                    .nextLong(Long.MAX_VALUE);
            blockBuilder.setMagicNumber(randomNumber);
            blockBuilder.generateHash();
        } while (!isEnoughZeroInHash(blockBuilder.getHash()));
        long secondsOfGenerating = (new Date().getTime() - startTime) / 1000;
        blockBuilder.setGeneratingTime(secondsOfGenerating);
        return randomNumber;
    }

    public boolean isEnoughZeroInHash(String hash) {
        String regex = String.format("\\b0{%d}.*", numberOfZeros);
        return hash.matches(regex);
    }
}
