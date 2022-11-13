package crypto.scratch.block.components.transaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.block.components.transaction.info.TransactionInfo;
import crypto.scratch.commons.Hashable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record Transaction(
		@JsonProperty("sender_address") @NotEmpty String senderAddress,
		@JsonProperty("transaction_info") @NotNull List<TransactionInfo> transactionInfo
) implements Hashable {

	@Override
	public String hashArguments() {
		StringBuilder builder = new StringBuilder();
		transactionInfo.stream()
		               .map(TransactionInfo::message)
		               .forEach(builder::append);
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		transactionInfo.forEach(builder::append);
		return builder.toString();
	}
}
