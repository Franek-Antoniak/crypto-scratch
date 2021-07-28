package blockchain.messenger;

import blockchain.cryptography.keypair.GenerateKeys;
import blockchain.messenger.controller.MessengerController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Person extends Thread {
    private final static List<String> listOfNames = List.of("Seward", "Rosie", "Benson", "Westley", "Harvey", "Vince",
            "Felicity", "Eleanore", "Jameson", "Lorinda", "Kristy", "Doreen", "Lorene", "Bernadette", "Roly");
    private final MessengerController messengerController = MessengerController.getInstance();
    private final RandomMessagesGenerator messagesGenerator = RandomMessagesGenerator.getInstance();

    public Person(String name) {
        super(name);
    }

    public static String generateName() {
        int randomInt = Math.abs(ThreadLocalRandom.current()
                .nextInt(listOfNames.size()));
        return listOfNames.get(randomInt);
    }

    @Override
    public void run() {
        GenerateKeys.generateKeysForUser();
        String randomMessage = messagesGenerator.getRandomMessage();
        messengerController.sendNewMessage(randomMessage);
    }
}
