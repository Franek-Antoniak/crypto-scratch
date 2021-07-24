package blockchain.messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Messenger {
    private final List<MessageHolder> newMessagesHolderList = new ArrayList<>();
    private List<MessageHolder> finalList = new ArrayList<>();

    private Messenger() {

    }

    private final static class MessengerSingleton {
        private static final Messenger messenger = new Messenger();
    }

    public static Messenger getInstance() {
        return MessengerSingleton.messenger;
    }

    public synchronized void addPeople(int howManyPeople) {
        Stream.generate(() -> new Person(Person.generateName()))
                .limit(howManyPeople)
                .forEach(Thread::start);
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

}
