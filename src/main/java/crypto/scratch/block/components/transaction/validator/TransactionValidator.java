package crypto.scratch.block.components.transaction.validator;

import crypto.scratch.block.Block;
import crypto.scratch.block.components.transaction.Transaction;
import crypto.scratch.block.components.transaction.info.TransactionInfo;
import crypto.scratch.block.components.transaction.info.validator.TransactionSignaturesValidator;
import crypto.scratch.node.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
	public static boolean validateTransaction(Transaction transaction) {
		boolean isSignatureValid = validateSignatures(transaction);
		boolean isAmountValid = transaction.transactionInfo()
		                                   .stream()
		                                   .map(TransactionInfo::amount)
		                                   .allMatch(amount -> amount.compareTo(BigDecimal.ZERO) > 0);
		boolean isSenderValid = validateSender(transaction);
		return isSignatureValid && isAmountValid && isSenderValid;
	}

	private static boolean validateSignatures(Transaction transaction) {
		return transaction.transactionInfo()
		                  .stream()
		                  .allMatch(TransactionSignaturesValidator::validate);
	}

	public static boolean validateSender(Transaction transaction) {
		return transaction.transactionInfo()
		                  .stream()
		                  .map(TransactionInfo::senderAddress)
		                  .distinct()
		                  .count() == 1;
	}

	public static boolean validateWithBlockchain(List<Transaction> transactions, Node node) {
		List<String> transactionsInputId = transactions.stream()
		                                               .map(Transaction::senderAddress)
		                                               .toList();
		HashMap<String, BigDecimal> coinsSumPerInputIdInBlockchain = getBalanceForAddresses(transactionsInputId, node);
		// How much they want to spend
		HashMap<String, BigDecimal> coinsSumPerInputIdInNewBlock = new HashMap<>();
		transactionsInputId.forEach(inputId -> coinsSumPerInputIdInNewBlock.put(inputId, BigDecimal.ZERO));
		transactions.stream()
		            .flatMap(transaction -> transaction.transactionInfo()
		                                               .stream())
		            .filter(transactionInfo -> transactionsInputId.contains(transactionInfo.senderAddress()))
		            .forEach(transactionInfo -> coinsSumPerInputIdInNewBlock.put(transactionInfo.senderAddress(),
		                                                                         coinsSumPerInputIdInNewBlock.get(
				                                                                                                     transactionInfo.senderAddress())
		                                                                                                     .add(transactionInfo.amount())
		                                                                        ));
		// Calculate the difference between how much they want to spend and how much they have
		HashMap<String, BigDecimal> result = new HashMap<>();
		coinsSumPerInputIdInBlockchain.forEach(
				(key, value) -> result.put(key, value.subtract(coinsSumPerInputIdInNewBlock.get(key))));
		// If the difference is negative, they don't have enough coins
		return result.values()
		             .stream()
		             .map(BigDecimal::signum)
		             .allMatch(x -> x >= 0);
	}

	private static HashMap<String, BigDecimal> getBalanceForAddresses(List<String> senderAddresses, Node node) {
		// How much they send in total to other addresses
		HashMap<String, BigDecimal> coinsSumPerInputIdInBlockchain = new HashMap<>();
		senderAddresses.forEach(inputId -> coinsSumPerInputIdInBlockchain.put(inputId, BigDecimal.ZERO));
		node.getBlockchainContainer()
		    .entrySet()
		    .stream()
		    .flatMap(entry -> entry.getValue()
		                           .transactions()
		                           .stream())
		    .flatMap(transaction -> transaction.transactionInfo()
		                                       .stream())
		    .filter(transactionInfo -> senderAddresses.contains(transactionInfo.senderAddress()))
		    .forEach(transactionInfo -> coinsSumPerInputIdInBlockchain.put(transactionInfo.senderAddress(),
	                                                                   coinsSumPerInputIdInBlockchain.get(transactionInfo.senderAddress())
		                                                                     .subtract(transactionInfo.amount())
	                                                                  ));
		// How much they got in total from other addresses
		node.getBlockchainContainer()
		    .entrySet()
		    .stream()
		    .flatMap(entry -> entry.getValue()
		                           .transactions()
		                           .stream())
		    .flatMap(transaction -> transaction.transactionInfo()
		                                       .stream())
		    .filter(transactionInfo -> senderAddresses.contains(transactionInfo.receiverAddress()))
		    .forEach(transactionInfo -> coinsSumPerInputIdInBlockchain.put(transactionInfo.receiverAddress(),
	                                                                   coinsSumPerInputIdInBlockchain.get(transactionInfo.receiverAddress())
		                                                                     .add(transactionInfo.amount())
	                                                                  ));
		// How much coins they got from mining the crypto
		node.getBlockchainContainer()
		    .values()
		    .stream()
		    .map(Block::rewardInfo)
		    .filter(blockRewardInfo -> senderAddresses.contains(blockRewardInfo.minerAddress()))
		    .forEach(blockRewardInfo -> coinsSumPerInputIdInBlockchain.put(blockRewardInfo.minerAddress(),
	                                                                   coinsSumPerInputIdInBlockchain.get(blockRewardInfo.minerAddress())
		                                                                     .add(blockRewardInfo.reward())
	                                                                  ));
		return coinsSumPerInputIdInBlockchain;
	}

	public static boolean validateNonceValuesForNewTransaction(
			Transaction transaction, List<Transaction> currentTransactions, long height
	                                                          ) {
		String strHeight = String.valueOf(height);
		boolean isStartOfNonceProper = transaction.transactionInfo()
		                                          .stream()
		                                          .map(TransactionInfo::nonce)
		                                          .allMatch(nonce -> nonce.startsWith(strHeight));
		Set<String> currentTransactionsNonce = transaction.transactionInfo()
		                                                  .stream()
		                                                  .map(TransactionInfo::nonce)
		                                                  .collect(Collectors.toSet());
		currentTransactionsNonce.addAll(currentTransactions.stream()
		                                                   .filter(t -> t.senderAddress()
		                                                                 .equals(transaction.senderAddress()))
		                                                   .flatMap(t -> t.transactionInfo()
		                                                                  .stream())
		                                                   .map(TransactionInfo::nonce)
		                                                   .collect(Collectors.toSet()));
		long transactionsForSender = currentTransactions.stream()
		                                                .filter(t -> t.senderAddress()
		                                                              .equals(transaction.senderAddress()))
		                                                .count() + transaction.transactionInfo()
		                                                                      .size();
		boolean isNonceValuesUnique = currentTransactionsNonce.size() == transactionsForSender;
		return isStartOfNonceProper && isNonceValuesUnique;
	}

	public static boolean validateUniqueNonceValuesInBlock(List<Transaction> transactions, long height) {
		String strHeight = String.valueOf(height);
		boolean isStartOfNonceProper = transactions.stream()
		                                           .flatMap(t -> t.transactionInfo()
		                                                          .stream())
		                                           .map(TransactionInfo::nonce)
		                                           .allMatch(nonce -> nonce.startsWith(strHeight));
		Map<String, Long> transfersPerAddress = transactions.stream()
		                                                    .flatMap(t -> t.transactionInfo()
		                                                                   .stream())
		                                                    .collect(groupingBy(TransactionInfo::senderAddress,
		                                                                        counting()
		                                                                       ));
		Map<String, Set<String>> noncePerAddress = transactions.stream()
		                                                       .flatMap(t -> t.transactionInfo()
		                                                                      .stream())
		                                                       .collect(groupingBy(TransactionInfo::senderAddress,
		                                                                           mapping(TransactionInfo::nonce,
		                                                                                   toSet()
		                                                                                  )
		                                                                          ));
		boolean isNonceValuesUnique = transfersPerAddress.entrySet()
		                                                 .stream()
		                                                 .allMatch(entry -> entry.getValue() == noncePerAddress.get(
				                                                                                                       entry.getKey())
		                                                                                                       .size());
		return isStartOfNonceProper && isNonceValuesUnique;
	}

	public static BigDecimal getBalanceForAddress(String address, Node node) {
		return getBalanceForAddresses(List.of(address), node).get(address);
	}
}
