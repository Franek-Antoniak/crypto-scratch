package blockchain;

import blockchain.miner.CryptoMiner;

public class Main {

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        CryptoMiner cryptoMiner = new CryptoMiner(blockchain);
        cryptoMiner.mineBlocks(5);
        System.out.println(blockchain);
    }
}
