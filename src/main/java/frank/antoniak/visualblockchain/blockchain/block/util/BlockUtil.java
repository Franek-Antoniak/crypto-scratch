package frank.antoniak.visualblockchain.blockchain.block.util;

import frank.antoniak.visualblockchain.blockchain.block.Block;
import frank.antoniak.visualblockchain.blockchain.block.TimeState;

final public class BlockUtil {

    private BlockUtil() {
    }

    public static boolean isEnoughZeroInHash(String hash, long amountOfZeros) {
        String regex = String.format("\\b0{%d}.*", amountOfZeros);
        return hash.matches(regex);
    }

    public static long howManyZerosInNext(Block lastBlock) {
        TimeState timeState = howLongGenerated(lastBlock.getGeneratingTime());
        long zerosInNext = lastBlock.getAmountOfZeros();
        switch (timeState) {
            case SHORT:
                zerosInNext++;
                break;
            case LONG:
                zerosInNext--;
                break;
        }
        return zerosInNext;
    }

    public static TimeState howLongGenerated(long generatingTime) {
        if (generatingTime < 5)
            return TimeState.SHORT;
        else if (generatingTime > 60)
            return TimeState.LONG;
        else
            return TimeState.PROPERLY;
    }
}
