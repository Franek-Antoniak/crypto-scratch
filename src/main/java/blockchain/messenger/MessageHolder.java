package blockchain.messenger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MessageHolder {
    private final String author;
    private final String messageData;
    private final String sign;
    private final long id;

    public MessageHolder(List<byte[]> encryptedData, long id) {
        this.messageData = new String(encryptedData.get(0));
        this.sign = new String(encryptedData.get(1));
        this.author = Thread.currentThread()
                .getName();
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("> %s:\nMessageData:\n%s\nSign: %s\n", author, messageData, sign);
    }

    @AllArgsConstructor
    public static class Decrypted {
        String decryptedMessage;
        long id;

        @Override
        public String toString() {
            return String.format("Id: %d %s", id, decryptedMessage);
        }
    }
}
