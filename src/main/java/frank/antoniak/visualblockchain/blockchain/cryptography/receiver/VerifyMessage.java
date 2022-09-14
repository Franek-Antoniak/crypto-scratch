package frank.antoniak.visualblockchain.blockchain.cryptography.receiver;

import frank.antoniak.visualblockchain.blockchain.messenger.MessageHolder;
import frank.antoniak.visualblockchain.blockchain.util.StringUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class VerifyMessage {
    String keyFile;
    String author;
    String message;
    String hexSign;
    private final List<byte[]> list = new ArrayList<>();

    //The constructor of VerifyMessage class retrieves the byte arrays from the File
    //and prints the message only if the signature is verified.
    public VerifyMessage(MessageHolder messageHolder) {
        this.author = messageHolder.getAuthor();
        this.message = messageHolder.getMessageData();
        this.hexSign = messageHolder.getSign();
        this.keyFile = "MyKeys/" + author + "/publicKey";
        list.add(message.getBytes(StandardCharsets.UTF_8));
        list.add(StringUtil.hexStringToByteArray(hexSign));
    }


    public boolean isMessageVerified() {
        try {
            return verifySignature(list.get(0), list.get(1), keyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(keyFile));
        sig.update(data);

        return sig.verify(signature);
    }

    //Method to retrieve the Public Key from a file
    public PublicKey getPublic(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}