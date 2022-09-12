package com.frank.antoniak.blockchain.cryptomine;

import com.frank.antoniak.blockchain.cryptominer.CryptoMiner;
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
        amountOfMiners = Math.min(amountOfMiners, cryptoMiners.size());
        cryptoMiners.stream()
                .limit(amountOfMiners)
                .forEach(cryptoMiner -> {
                    cryptoMiner.turnOffMiner();
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
