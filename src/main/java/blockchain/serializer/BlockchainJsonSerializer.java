package blockchain.serializer;

import blockchain.Blockchain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;

public class BlockchainJsonSerializer {
    private final String basePath = "src/main/resources/serialized/blockchain/Blockchain";
    private final ObjectMapper objectMapper;
    private long serialId = 0;

    public BlockchainJsonSerializer() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Blockchain.class, new BlockchainCustomJsonSerializer());
        objectMapper.registerModule(module);
    }

    public void serialize(Blockchain blockchain) {

        try {
            File file = new File(basePath + serialId + ".json");
            objectMapper.writeValue(file, blockchain);
            serialId++;
        } catch (IOException e) {
            System.out.println("IOException caught, cannot save the object");
        }
    }

    public Blockchain deserialize(long id) {
        try {
            File file = new File(basePath + id + ".json");
            return objectMapper.readValue(file, Blockchain.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Blockchain.getInstance();
    }


}
