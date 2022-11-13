package crypto.scratch.block.scratch;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.block.components.transaction.Transaction;
import crypto.scratch.commons.Hashable;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BlockScratch(
		@JsonProperty("transactions") List<Transaction> transactions,
		@JsonProperty("header") HeaderScratch blockHeader,
		@JsonProperty("reward") BigDecimal reward
) implements Hashable {
	@Override
	public String hashArguments() {
		StringBuilder builder = new StringBuilder();
		builder.append(blockHeader.hashArguments());
		transactions.forEach(transaction -> builder.append(transaction.hashArguments()));
		return builder.toString();
	}
}
