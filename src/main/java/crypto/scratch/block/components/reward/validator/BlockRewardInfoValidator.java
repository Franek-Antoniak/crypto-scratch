package crypto.scratch.block.components.reward.validator;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.reward.RewardInfo;
import crypto.scratch.block.components.reward.util.BlockRewardInfoUtil;
import crypto.scratch.keys.util.KeysUtil;

import java.math.BigDecimal;

public class BlockRewardInfoValidator {
	public static boolean validate(Block block) {
		RewardInfo blockRewardInfo = block.rewardInfo();
		boolean validateSignature = validateSignature(blockRewardInfo);
		boolean validateRewardAmount = validateRewardAmount(
				blockRewardInfo, block.header()
				                      .height());
		return validateSignature && validateRewardAmount;
	}

	private static boolean validateSignature(RewardInfo blockRewardInfo) {
		try {
			return KeysUtil.verify(
					blockRewardInfo.minerAddress(), blockRewardInfo.signature(), blockRewardInfo.message());
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean validateRewardAmount(RewardInfo blockRewardInfo, long height) {
		BigDecimal rewardAmount = blockRewardInfo.reward();
		BigDecimal expectedRewardAmount = BlockRewardInfoUtil.calculateReward(height);
		return rewardAmount.compareTo(expectedRewardAmount) == 0;
	}
}
