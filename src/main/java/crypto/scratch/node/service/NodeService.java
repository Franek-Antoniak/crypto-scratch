package crypto.scratch.node.service;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.header.util.BlockHeaderUtil;
import crypto.scratch.block.components.reward.util.BlockRewardInfoUtil;
import crypto.scratch.block.components.transaction.validator.TransactionValidator;
import crypto.scratch.block.scratch.BlockScratch;
import crypto.scratch.block.scratch.HeaderScratch;
import crypto.scratch.node.Node;
import crypto.scratch.node.observer.BlockchainObserver;
import crypto.scratch.node.validator.BlockchainValidator;
import crypto.scratch.transaction.receiver.service.TransactionReceiverService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class NodeService {
	private final Node node;
	private final TransactionReceiverService transactionHolderService;
	private final BlockchainObserver blockchainObserver;

	@Synchronized("node")
	public boolean addBlock(Block block) {
		if (BlockchainValidator.validateWithNewBlock(block, node)) {
			node.addBlock(block);
			blockchainObserver.notifyListeners();
			System.out.println(node.getLastBlock());
			return true;
		}
		return false;
	}

	public BlockScratch getBlockScratch() {
		Block lastBlock = node.getLastBlock();
		HeaderScratch headerScratch = HeaderScratch.builder()
		                                           .difficulty(BlockHeaderUtil.getProperDifficulty(lastBlock,
		                                                                                           System.currentTimeMillis()
		                                                                                          ))
		                                           .height(lastBlock.header()
		                                                            .height() + 1)
		                                           .timestamp(System.currentTimeMillis())
		                                           .parentHash(lastBlock.header()
		                                                                .hash())
		                                           .build();
		return BlockScratch.builder()
		                   .blockHeader(headerScratch)
		                   .transactions(transactionHolderService.getPendingTransactions())
		                   .reward(BlockRewardInfoUtil.calculateReward(lastBlock.header()
		                                                                        .height() + 1))
		                   .build();
	}

	public boolean isLastHashEquals(String hash) {
		Block lastBlock = node.getLastBlock();
		return lastBlock.header()
		                .hash()
		                .equals(hash);
	}

	@Synchronized("node")
	public BigDecimal getBalance(String address) {
		return TransactionValidator.getBalanceForAddress(address, node);
	}
}

