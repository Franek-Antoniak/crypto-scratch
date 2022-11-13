package crypto.scratch.node.observer;

import crypto.scratch.node.listener.BlockchainListener;
import crypto.scratch.pattern.observer.Observer;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Observer
@RequiredArgsConstructor
public class BlockchainObserver {
	private final List<BlockchainListener> listeners;

	public void notifyListeners() {
		listeners.forEach(BlockchainListener::onNewBlock);
	}
}
