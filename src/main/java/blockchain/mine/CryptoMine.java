package blockchain.mine;

import blockchain.miner.CryptoMiner;
import lombok.Data;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

@Data
public class CryptoMine {
    private Set<CryptoMiner> cryptoMiners;

    private CryptoMine() {
        cryptoMiners = new ConcurrentSkipListSet<>(Comparator.comparing(CryptoMiner::getId));
    }

    public static CryptoMine getInstance() {
        return CryptoMinerMineSingleton.instance;
    }

    public void addMiners(int amountOfMiners) {
        int currentAmountOfMainers = cryptoMiners.size();
        amountOfMiners = Math.min(amountOfMiners, Runtime.getRuntime()
                .availableProcessors() - currentAmountOfMainers);
        Stream.generate(CryptoMiner::new)
                .limit(amountOfMiners)
                .forEach(cryptoMiner -> {
                    cryptoMiners.add(cryptoMiner);
                    cryptoMiner.start();
                });
    }

    public synchronized void removeMiners(int amountOfMiners) {
        int currentAmountOfMainers = cryptoMiners.size();
        cryptoMiners.forEach(cryptoMiner -> {
            cryptoMiner.turnOffMiner();
            cryptoMiner.awaitAndShutdownMainer(1000);
            cryptoMiners.remove(cryptoMiner);
        });
    }

    public void stopMining() {
        cryptoMiners.forEach(CryptoMiner::turnOffMining);
    }

    public void shutdown() {
        cryptoMiners.forEach(CryptoMiner::turnOffMiner);
        cryptoMiners.forEach(cryptoMiners::remove);
    }

    private static class CryptoMinerMineSingleton {
        private static final CryptoMine instance = new CryptoMine();
    }
}
