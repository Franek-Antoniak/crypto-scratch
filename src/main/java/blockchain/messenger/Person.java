package blockchain.messenger;

import blockchain.messenger.controller.MessengerController;

import java.util.stream.IntStream;

public class Person extends Thread {
    private final MessengerController messengerController = MessengerController.getInstance();
    private final RandomMessagesGenerator messagesGenerator = RandomMessagesGenerator.getInstance();

    public Person(String name) {
        super(name);
    }

    @Override
    public void run() {
        IntStream.iterate(0, x -> x < 30, x -> x++).forEach((iterator) -> {
            String randomMessage = messagesGenerator.getRandomMessage();
            messengerController.sendNewMessage(randomMessage);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
