package blockchain.messenger;

import blockchain.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class MessageHolder {
    private final String author;
    private final String messageData;
    private final String sign;


    public MessageHolder(List<byte[]> encryptedData) {
        this.messageData = new String(encryptedData.get(0));
        this.sign = StringUtil.bytesToHex(encryptedData.get(1));
        this.author = Thread.currentThread()
                .getName();
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
