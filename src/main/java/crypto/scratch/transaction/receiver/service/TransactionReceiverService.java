package crypto.scratch.transaction.receiver.service;

import crypto.scratch.block.components.transaction.Transaction;
import crypto.scratch.block.components.transaction.validator.TransactionValidator;
import crypto.scratch.node.Node;
import crypto.scratch.node.listener.BlockchainListener;
import crypto.scratch.pattern.listener.Listener;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Listener
@RequiredArgsConstructor
public class TransactionReceiverService implements BlockchainListener {
	private final Node node;
	private final List<Transaction> currentTransactions = new ArrayList<>();
	private final List<Transaction> pendingTransactions = new ArrayList<>();
	private long securityIndex = 1;

	@Synchronized("node")
	public synchronized boolean addTransaction(Transaction transaction) {
		boolean isTransactionValid = TransactionValidator.validateTransaction(transaction);
		boolean isValidWithBlockchain = TransactionValidator.validateWithBlockchain(List.of(transaction), node);
		boolean isNonceValuesValid = TransactionValidator.validateNonceValuesForNewTransaction(
				transaction, currentTransactions, securityIndex);
		if (isValidWithBlockchain && isTransactionValid && isNonceValuesValid) {
			currentTransactions.add(transaction);
			return true;
		}
		return false;
	}

	public synchronized String getRandomIndex() {
		return String.format("%d%d", securityIndex, System.currentTimeMillis());
	}

	@Synchronized("pendingTransactions")
	public List<Transaction> getPendingTransactions() {
		return pendingTransactions;
	}

	@Override
	public synchronized void onNewBlock() {
		securityIndex++;
		pendingTransactions.clear();
		pendingTransactions.addAll(currentTransactions);
		currentTransactions.clear();
	}
}
