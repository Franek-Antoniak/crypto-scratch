package blockchain.messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Messenger {
    private final List<MessageHolder> newMessagesHolderList = new ArrayList<>();
    private List<MessageHolder> finalList = new ArrayList<>();
    private List<Person> listOfUsers;

    private Messenger() {

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

    public synchronized void addMessage(MessageHolder message) {
        newMessagesHolderList.add(message);
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
