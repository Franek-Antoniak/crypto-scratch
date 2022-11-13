package crypto.scratch.node.validator;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.header.util.BlockHeaderUtil;
import crypto.scratch.block.components.transaction.validator.TransactionValidator;
import crypto.scratch.block.validator.BlockValidator;
import crypto.scratch.node.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BlockchainValidator {

	public static boolean validateWithNewBlock(Block newBlock, Node node) {
		Block lastBlock = node.getLastBlock();
		boolean isChainValid = validateWithLastBlock(newBlock, lastBlock);
		boolean isHeightValid = isHeightValid(newBlock, lastBlock);
		boolean isBlockValid = BlockValidator.validateBlock(newBlock);
		boolean isTimeStampAfterLastBlock = isTimeStampAfterLastBlock(newBlock, lastBlock);
		boolean isTransactionsValid = newBlock.transactions()
		                                      .stream()
		                                      .allMatch(TransactionValidator::validateTransaction);
		boolean isTransactionsNonceValuesValid = TransactionValidator.validateUniqueNonceValuesInBlock(
				newBlock.transactions(), newBlock.header()
				                                 .height());
		boolean isTransfersValid = TransactionValidator.validateWithBlockchain(newBlock.transactions(), node);
		return isBlockValid && isChainValid && isHeightValid && isTimeStampAfterLastBlock && isTransactionsValid &&
		       isTransactionsNonceValuesValid && isTransfersValid;
	}

	private static boolean validateWithLastBlock(Block newBlock, Block lastBlock) {
		boolean isPreviousHashCorrect = isHashesMatching(newBlock, lastBlock);
		boolean isDifficultyChangeCorrect = isDifficultyChangeCorrect(newBlock, lastBlock);
		return isPreviousHashCorrect && isDifficultyChangeCorrect;
	}

	private static boolean isHeightValid(Block newBlock, Block lastBlock) {
		return newBlock.header()
		               .height() == lastBlock.header()
		                                     .height() + 1;
	}

	private static boolean isTimeStampAfterLastBlock(Block newBlock, Block lastBlock) {
		return newBlock.header()
		               .timestamp() > lastBlock.header()
		                                       .timestamp();
	}

	private static boolean isHashesMatching(Block newBlock, Block lastBlock) {
		return newBlock.header()
		               .parentHash()
		               .equals(lastBlock.header()
		                                .hash());
	}

	private static boolean isDifficultyChangeCorrect(Block newBlock, Block lastBlock) {
		int requiredDifficulty = BlockHeaderUtil.getProperDifficulty(lastBlock, newBlock.header()
		                                                                                .timestamp());
		return newBlock.header()
		               .difficulty() == requiredDifficulty;
	}
}
