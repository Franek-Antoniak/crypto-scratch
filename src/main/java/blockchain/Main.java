package blockchain;

import blockchain.miner.CryptoMiner;
import blockchain.serializer.BlockchainJsonSerializer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BlockchainJsonSerializer serializer = new BlockchainJsonSerializer();
        long magicNumber = getMagicNumber();
        Blockchain blockchain = new Blockchain();
        CryptoMiner cryptoMiner = new CryptoMiner(blockchain, magicNumber);
        cryptoMiner.mineBlocks(5);
        System.out.println(blockchain);
        serializer.serialize(blockchain);
    }

    private static long getMagicNumber() {
        System.out.print("Enter how many zeros the hash must start with: ");
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLong();
        }
    }
}
