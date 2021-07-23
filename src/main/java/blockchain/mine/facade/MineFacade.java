package blockchain.mine.facade;

import blockchain.mine.CryptoMine;

public class MineFacade {
    private final CryptoMine cryptoMine = CryptoMine.getInstance();

    private MineFacade() {

    }

    private static class MineFacadeSingleton {
        private static final MineFacade instance = new MineFacade();
    }

    public static MineFacade getInstance() {
        return MineFacadeSingleton.instance;
    }

    public int removeMainers(int howManyToRemove) {
        return cryptoMine.removeMiners(howManyToRemove);
    }

    public int addNewMainers(int howManyToAdd) {
        return cryptoMine.addMiners(howManyToAdd);
    }

    public void removeAllMainers() {
        cryptoMine.shutdown();
    }
}
