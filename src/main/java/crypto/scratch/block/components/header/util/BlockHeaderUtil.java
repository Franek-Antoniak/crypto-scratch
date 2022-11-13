package crypto.scratch.block.components.header.util;

import crypto.scratch.block.Block;

public class BlockHeaderUtil {
	public static int getProperDifficulty(Block lastBlock, long nexTimeStamp) {
		int difficulty = lastBlock.header()
		                          .difficulty();
		long timeStampDifference = nexTimeStamp - lastBlock.header()
		                                                   .timestamp();
		timeStampDifference /= 1000 * 60;
		if (timeStampDifference < 1) {
			difficulty++;
		} else if (timeStampDifference > 3) {
			difficulty--;
		}
		return Math.max(difficulty, 0);
	}
}
