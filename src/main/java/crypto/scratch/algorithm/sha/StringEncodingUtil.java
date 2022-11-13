package crypto.scratch.algorithm.sha;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringEncodingUtil {

	/* Applies StringUtil to a string and returns a blockHash. */
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			/* Applies sha256 to our input */
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return getString(hash);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getString(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (byte elem : hash) {
			String hex = Integer.toHexString(0xff & elem);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
