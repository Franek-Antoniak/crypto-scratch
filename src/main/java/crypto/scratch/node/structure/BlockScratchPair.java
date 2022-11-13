package crypto.scratch.node.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.block.scratch.BlockScratch;

public record BlockScratchPair(
		@JsonProperty("scratch") BlockScratch blockScratch, @JsonProperty("to_string") String scratchString
) {
}
