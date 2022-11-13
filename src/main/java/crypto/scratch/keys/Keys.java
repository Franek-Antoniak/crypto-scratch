package crypto.scratch.keys;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Keys(
		@JsonProperty("public_key") String publicKey, @JsonProperty("private_key") String privateKey
) {
}