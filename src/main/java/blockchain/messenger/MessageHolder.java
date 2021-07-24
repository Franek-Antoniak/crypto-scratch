package blockchain.messenger;

public class MessageHolder {
    private final String author;
    private final String message;

    public MessageHolder(String message) {
        this.message = message;
        this.author = Thread.currentThread()
                .getName();
    }

    @Override
    public String toString() {
        return String.format("%s: %s\n", author, message);
    }
}
