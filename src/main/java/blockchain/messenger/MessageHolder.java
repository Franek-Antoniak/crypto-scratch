package blockchain.messenger;

import lombok.Setter;

public class MessageHolder {
    private String author;
    @Setter
    private String message;

    @Override
    public String toString() {
        return String.format("%s: %s\n", author, message);
    }
}
