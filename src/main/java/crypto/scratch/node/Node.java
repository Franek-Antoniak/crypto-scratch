package crypto.scratch.node;

import crypto.scratch.block.Block;
import crypto.scratch.block.factory.BlockFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ToString
@RequiredArgsConstructor
public class Node {

	@Getter(onMethod_ = {@Synchronized})
	private final HashMap<String, Block> blockchainContainer = new HashMap<>();
	private Block lastBlock = BlockFactory.getGenesisBlock();

	@Synchronized("lastBlock")
	public Block getLastBlock() {
		return lastBlock;
	}

	public void addBlock(Block block) {
		blockchainContainer.put(block.header()
		                             .hash(), block);
		lastBlock = block;
	}
}
