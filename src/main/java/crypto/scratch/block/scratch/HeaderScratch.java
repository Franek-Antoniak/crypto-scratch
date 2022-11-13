package crypto.scratch.block.scratch;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.commons.Hashable;
import lombok.Builder;


@Builder
public record HeaderScratch(
		@JsonProperty("parent_hash") String parentHash,
		@JsonProperty("timestamp") long timestamp,
		@JsonProperty("difficulty") int difficulty,
		@JsonProperty("height") long height
) implements Hashable {

	@Override
	public String hashArguments() {
		return String.format("%s%s%s%s", parentHash, timestamp, difficulty, height);
	}
}
