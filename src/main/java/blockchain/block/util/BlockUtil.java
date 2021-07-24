package blockchain.block.util;

final public class BlockUtil {

    private BlockUtil() {
    }

    public static boolean isEnoughZeroInHash(String hash, long amountOfZeros) {
        String regex = String.format("\\b0{%d}.*", amountOfZeros);
        return hash.matches(regex);
    }
}
