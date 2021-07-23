package blockchain.messenger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMessagesGenerator {
    private final List<String> randomMessagesList;

    private RandomMessagesGenerator() {
        this.randomMessagesList = new ArrayList<>();
        try (FileReader fileReader = new FileReader("random/text/random_messages.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            randomMessagesList.add(bufferedReader.readLine());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static class RandomMessagesGeneratorSingleton {
        private static final RandomMessagesGenerator instance = new RandomMessagesGenerator();
    }

    public static RandomMessagesGenerator getInstance() {
        return RandomMessagesGeneratorSingleton.instance;
    }

    public String getRandomMessage() {
        int randomInt = ThreadLocalRandom.current()
                .nextInt(randomMessagesList.size());
        return randomMessagesList.get(randomInt);
    }

}
