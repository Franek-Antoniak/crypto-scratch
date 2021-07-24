package blockchain.messenger;

import lombok.Setter;

public class MessageHolder {
    private String author;
    @Setter
    private String message;

    public MessageHolder(String message) {
        this.message = message;
        this.author = Thread.currentThread().getName();
    }

    @Override
    public String toString() {
        return String.format("%s: %s\n", author, message);
    }
}
