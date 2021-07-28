package blockchain.messenger;

import blockchain.cryptography.sender.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Messenger {
    private final List<MessageHolder> newMessagesHolderList = new ArrayList<>();
    private List<MessageHolder> finalList = new ArrayList<>();
    private List<Person> listOfUsers;
    private volatile long counterId = 1;

    private Messenger() {
        /*Blockchain.getInstance().getLastIdOfMessage();*/
    }

    public static Messenger getInstance() {
        return MessengerSingleton.messenger;
    }

    public void addPeople(int howManyPeople) {
        listOfUsers = Stream.generate(() -> new Person(Person.generateName()))
                .limit(howManyPeople)
                .peek(Thread::start)
                .collect(Collectors.toList());
    }

    public synchronized void addMessage(String message) {
        MessageHolder.Decrypted messageDecrypted = new MessageHolder.Decrypted(message, counterId++);
        MessageHolder messageHolder;
        List<byte[]> encryptedMessage;
        try {
            encryptedMessage = Message.getEncryptedData(messageDecrypted.toString());
            messageHolder = new MessageHolder(encryptedMessage, messageDecrypted.id);
        } catch (Exception e) {
            System.err.println("Error while encrypting data for user: " + Thread.currentThread().getName());
            e.printStackTrace();
            return;
        }
        newMessagesHolderList.add(messageHolder);
    }

    public synchronized void safeCurrentMessages() {
        finalList = List.copyOf(newMessagesHolderList);
        newMessagesHolderList.clear();
    }

    public List<MessageHolder> getFinalMessages() {
        return finalList;
    }

    public void joinUsers() {
        listOfUsers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private final static class MessengerSingleton {
        private static final Messenger messenger = new Messenger();
    }

}
