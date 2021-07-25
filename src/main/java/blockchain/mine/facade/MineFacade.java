package blockchain.mine.facade;

import blockchain.mine.CryptoMine;

public class MineFacade {
    private final CryptoMine cryptoMine = CryptoMine.getInstance();

    private MineFacade() {

    }

    public static MineFacade getInstance() {
        return MineFacadeSingleton.instance;
    }

    public void removeMainers(int howManyToRemove) {
        cryptoMine.removeMiners(howManyToRemove);
    }

    public void addNewMainers(int howManyToAdd) {
        cryptoMine.addMiners(howManyToAdd);
    }

    public void removeAllMainers() {
        cryptoMine.shutdown();
    }

    private static class MineFacadeSingleton {
        private static final MineFacade instance = new MineFacade();
    }
}
