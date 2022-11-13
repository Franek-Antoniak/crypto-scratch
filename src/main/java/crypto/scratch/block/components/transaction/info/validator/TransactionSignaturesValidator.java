package crypto.scratch.block.components.transaction.info.validator;

import crypto.scratch.block.components.transaction.info.TransactionInfo;
import crypto.scratch.keys.util.KeysUtil;

public class TransactionSignaturesValidator {
	public static boolean validate(TransactionInfo transactionInfo) {
		try {
			return KeysUtil.verify(
					transactionInfo.senderAddress(), transactionInfo.signature(), transactionInfo.message());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
