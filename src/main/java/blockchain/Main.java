package blockchain;

import blockchain.mine.CryptoMine;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Blockchain blockchain = Blockchain.getInstance();
        CryptoMine cryptoMine = CryptoMine.getInstance();
        cryptoMine.startMining();
        TimeUnit.SECONDS.sleep(4);
        cryptoMine.shutdown();
        System.out.println(blockchain);
    }
}
