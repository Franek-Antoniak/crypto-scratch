package crypto.scratch.block.components.header;


import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.commons.Hashable;
import lombok.Builder;

@Builder
public record Header(
		@JsonProperty("parent_hash") String parentHash,
		@JsonProperty("hash") String hash,
		@JsonProperty("timestamp") long timestamp,
		@JsonProperty("difficulty") int difficulty,
		@JsonProperty("height") long height
) implements Hashable {
	@Override
	public String toString() {
		return String.format("Previous hash: %s\n", parentHash) + String.format("Block hash: %s\n", hash) +
		       String.format("Timestamp: %d\n", timestamp) + String.format("Difficulty: %d\n", difficulty) +
		       String.format("Height: %d\n", height);
	}

	@Override
	public String hashArguments() {
		return String.format("%s%d%d%d", parentHash, timestamp, difficulty, height);
	}
}
