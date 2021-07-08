package blockchain;

import blockchain.miner.CryptoMiner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        long magicNumber = getMagicNumber();
        Blockchain blockchain = new Blockchain();
        CryptoMiner cryptoMiner = new CryptoMiner(blockchain, magicNumber);
        cryptoMiner.mineBlocks(5);
        System.out.println(blockchain);
    }

    private static long getMagicNumber() {
        System.out.print("Enter how many zeros the hash must start with: ");
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLong();
        }
    }
}
