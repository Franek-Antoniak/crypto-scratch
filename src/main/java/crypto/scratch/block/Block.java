package crypto.scratch.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.algorithm.sha.StringEncodingUtil;
import crypto.scratch.block.components.header.Header;
import crypto.scratch.block.components.reward.RewardInfo;
import crypto.scratch.block.components.transaction.Transaction;
import crypto.scratch.commons.Hashable;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record Block(
		@JsonProperty("header") @NotNull Header header,
		@JsonProperty("transactions") @NotNull List<Transaction> transactions,
		@JsonProperty("reward_info") @NotNull RewardInfo rewardInfo,
		@JsonProperty("nonce") int nonce
) implements Hashable {

	public String toSHA256() {
		return StringEncodingUtil.applySha256(hashArguments());
	}

	@Override
	public String hashArguments() {
		StringBuilder builder = new StringBuilder();
		builder.append(header.hashArguments());
		transactions.forEach(transaction -> builder.append(transaction.hashArguments()));
		builder.append(rewardInfo.hashArguments());
		builder.append(String.format("%d", nonce));
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(header);
		builder.append("Transactions:\n");
		transactions.forEach(builder::append);
		builder.append("Reward Info:\n");
		builder.append(rewardInfo);
		builder.append(String.format("Nonce: %s\n", nonce));
		return builder.toString();
	}
}