package blockchain.messenger;

import blockchain.cryptography.sender.Message;
import blockchain.messenger.person.PersonFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

final public class Messenger {
    private final List<MessageHolder> newMessagesHolderList = new ArrayList<>();
    private List<MessageHolder> finalList = new ArrayList<>();
    private volatile long counterId = 1;

    private Messenger() {}

    private final static class MessengerSingleton {
        private static final Messenger messenger = new Messenger();
    }

    public static Messenger getInstance() {
        return MessengerSingleton.messenger;
    }

    public synchronized void addMessage(String message) {
        MessageHolder.Decrypted messageDecrypted = new MessageHolder.Decrypted(message, counterId++);
        MessageHolder messageHolder;
        List<byte[]> encryptedMessage;
        try {
            encryptedMessage = Message.getEncryptedData(messageDecrypted.toString());

            messageHolder = new MessageHolder(encryptedMessage);
        } catch (Exception e) {
            System.err.println("Error while encrypting data for user: " + Thread.currentThread()
                    .getName());
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

    public void addNewUser() throws InterruptedException, InvocationTargetException {
        Runnable initFrame = PersonFrame::new;
        SwingUtilities.invokeAndWait(initFrame);
    }

}
