package crypto.scratch.block.components.reward;

import com.fasterxml.jackson.annotation.JsonProperty;
import crypto.scratch.commons.Hashable;
import crypto.scratch.commons.Signed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RewardInfo(
		@JsonProperty("miner_address") @NotEmpty String minerAddress,
		@JsonProperty("reward") @NotNull BigDecimal reward,
		@JsonProperty("signature") @NotEmpty String signature
) implements Hashable, Signed {

	@Override
	public String toString() {
		return String.format("%s: gained %s PVC Signature: %s\n", minerAddress, reward, signature);
	}

	@Override
	public String hashArguments() {
		return String.format("%s%s%s", minerAddress, reward, signature);
	}

	@Override
	public String message() {
		return String.format("%s%s", minerAddress, reward);
	}
}
