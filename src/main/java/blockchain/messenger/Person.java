package blockchain.messenger;

import blockchain.messenger.controller.MessengerController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Person extends Thread {
    private final MessengerController messengerController = MessengerController.getInstance();
    private final RandomMessagesGenerator messagesGenerator = RandomMessagesGenerator.getInstance();
    private final static List<String> listOfNames = List.of("Seward", "Rosie", "Benson", "Westley", "Harvey", "Vince",
            "Felicity", "Eleanore", "Jameson", "Lorinda", "Kristy", "Doreen", "Lorene", "Bernadette", "Roly");

    public Person(String name) {
        super(name);
    }

    @Override
    public void run() {
        IntStream.iterate(0, x -> x < 10, x -> ++x)
                .forEach((iterator) -> {
                    String randomMessage = messagesGenerator.getRandomMessage();
                    messengerController.sendNewMessage(randomMessage);
                    try {
                        sleep(Math.abs(ThreadLocalRandom.current()
                                .nextInt(1000)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static String generateName() {
        int randomInt = Math.abs(ThreadLocalRandom.current()
                .nextInt(listOfNames.size()));
        return listOfNames.get(randomInt);
    }
}
