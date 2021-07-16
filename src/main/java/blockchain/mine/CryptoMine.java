package blockchain.mine;

import blockchain.miner.CryptoMiner;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CryptoMine {
    private List<CryptoMiner> cryptoMiners;

    private CryptoMine() {
        cryptoMiners = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cryptoMiners.add(new CryptoMiner());
        }
    }

    public void stopMining() {
        for (var cryptoMiner : cryptoMiners) {
            cryptoMiner.turnOffMining();
        }
    }

    private static class CryptoMinerMineSingleton {
        private static final CryptoMine instance = new CryptoMine();
    }

    public static CryptoMine getInstance() {
        return CryptoMinerMineSingleton.instance;
    }

    public void shutdown() {
        for (var cryptoMiner : cryptoMiners) {
            cryptoMiner.turnOffMiner();
        }
    }

    public void startMining() {
        for (CryptoMiner cryptoMiner : cryptoMiners) {
            cryptoMiner.start();
        }
    }
}
