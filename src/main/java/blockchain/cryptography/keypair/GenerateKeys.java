package blockchain.cryptography.keypair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keyLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keyLength);
    }

    public void createKeys() {
        this.pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile()
                .mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public static void generateKeysForUser() {
        String author = Thread.currentThread()
                .getName();
        File publicKey = new File("MyKeys/" + author + "/publicKey");
        File privateKey = new File("MyKeys/" + author + "/privateKey");
        if (publicKey.exists() && privateKey.exists())
            return;
        GenerateKeys gk;
        try {
            gk = new GenerateKeys(1024);
            gk.createKeys();
            gk.writeToFile("MyKeys/" + author + "/publicKey", gk.getPublicKey()
                    .getEncoded());
            gk.writeToFile("MyKeys/" + author + "/privateKey", gk.getPrivateKey()
                    .getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}