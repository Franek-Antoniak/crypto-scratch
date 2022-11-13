package crypto.scratch.block.components.reward.util;

import java.math.BigDecimal;
import java.util.stream.LongStream;

public class BlockRewardInfoUtil {
	public static BigDecimal calculateReward(long height) {
		return LongStream.range(0, height / 5000)
		                 .mapToObj(i -> BigDecimal.valueOf(2))
		                 .reduce(BigDecimal.valueOf(100), BigDecimal::divide);
	}
}
