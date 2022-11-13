package crypto.scratch.block.validator;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.reward.validator.BlockRewardInfoValidator;
import crypto.scratch.block.components.transaction.validator.TransactionValidator;

public class BlockValidator {
	public static boolean validateBlock(Block newBlock) {
		boolean isHashValid = isHashValid(newBlock);
		boolean isEnoughZerosInHash = isEnoughZerosInHash(newBlock);
		boolean isTransactionsValid = newBlock.transactions()
		                                      .stream()
		                                      .allMatch(TransactionValidator::validateTransaction);
		boolean isRewardInfoValid = BlockRewardInfoValidator.validate(newBlock);
		boolean isTimestampValid = isTimestampValid(newBlock);
		return isTransactionsValid && isHashValid && isEnoughZerosInHash && isRewardInfoValid && isTimestampValid;
	}

	private static boolean isHashValid(Block newBlock) {
		return newBlock.header()
		               .hash()
		               .equals(newBlock.toSHA256());
	}

	private static boolean isEnoughZerosInHash(Block newBlock) {
		return newBlock.header()
		               .hash()
		               .startsWith("0".repeat(newBlock.header()
		                                              .difficulty()));
	}

	private static boolean isTimestampValid(Block newBlock) {
		return newBlock.header()
		               .timestamp() <= System.currentTimeMillis();
	}
}
