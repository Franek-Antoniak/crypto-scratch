package crypto.scratch.block.components.transaction.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.commons.Hashable;
import crypto.scratch.commons.Signed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionInfo(
		@JsonProperty("sender_address") @NotEmpty String senderAddress,
		@JsonProperty("receiver_address") @NotEmpty String receiverAddress,
		@JsonProperty("amount") @NotNull BigDecimal amount,
		@JsonProperty("signature") @NotEmpty String signature,
		@JsonProperty("nonce") @NotEmpty String nonce
) implements Hashable, Signed {

	@Override
	public String toString() {
		return String.format("id: %s %s send %s PVC to %s Signature: %s\n", nonce, senderAddress, amount,
		                     receiverAddress, signature
		                    );
	}

	@Override
	public String hashArguments() {
		return String.format("%s%s%s%s%s", nonce, senderAddress, amount, receiverAddress, signature);
	}

	@Override
	public String message() {
		return String.format("%s%s%s%s", nonce, senderAddress, amount, receiverAddress);
	}
}
