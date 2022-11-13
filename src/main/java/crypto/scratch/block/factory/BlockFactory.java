package crypto.scratch.block.factory;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.header.Header;
import crypto.scratch.block.components.reward.RewardInfo;
import crypto.scratch.pattern.factory.Factory;

import java.math.BigDecimal;
import java.util.List;

@Factory
public class BlockFactory {
	private static Block genesisBlock;

	public static Block getGenesisBlock() {
		if (genesisBlock == null) {
			genesisBlock = Block.builder()
			                    .header(Header.builder()
			                                  .height(-1)
			                                  .difficulty(-1)
			                                  .parentHash("")
			                                  .hash("")
			                                  .timestamp(System.currentTimeMillis())
			                                  .build())
			                    .transactions(List.of())
			                    .rewardInfo(new RewardInfo("", BigDecimal.ONE, ""))
			                    .nonce(-1)
			                    .build();
		}
		return genesisBlock;
	}
}
