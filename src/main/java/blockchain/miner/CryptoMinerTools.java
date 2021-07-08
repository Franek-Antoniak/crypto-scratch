package blockchain.miner;

import blockchain.block.BlockBuilder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class CryptoMinerTools {
    private final long numberOfZeros;

    public long findMagicNumber(BlockBuilder blockBuilder) {
        long randomNumber;
        do {
            randomNumber = ThreadLocalRandom.current()
                    .nextLong(Long.MAX_VALUE);
            blockBuilder.setMagicNumber(randomNumber);
            blockBuilder.generateHash();
        } while (!isEnoughZeroInHash(blockBuilder.getHash()));
        return randomNumber;
    }

    public boolean isEnoughZeroInHash(String hash) {
        String regex = String.format("\\b0{%d}.*", numberOfZeros);
        return hash.matches(regex);
    }
}
