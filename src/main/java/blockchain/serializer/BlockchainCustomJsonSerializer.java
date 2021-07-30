package blockchain.serializer;

import blockchain.Blockchain;
import blockchain.block.Block;
import blockchain.block.BlockBuilder;
import blockchain.serializer.exception.DeserializationValidationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

public class BlockchainCustomJsonSerializer extends StdDeserializer<Blockchain> {

    public BlockchainCustomJsonSerializer() {
        this(null);
    }

    public BlockchainCustomJsonSerializer(Class<Blockchain> t) {
        super(t);
    }

    @Override
    public Blockchain deserialize(JsonParser parser, DeserializationContext deserializer)
            throws IOException {
        Blockchain blockchain = Blockchain.getInstance();
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        String blockListJson = node.get("blockList")
                .toString();
        LinkedList<BlockBuilder> dtoLinkedList = new ObjectMapper().readValue(blockListJson,
                new TypeReference<>() {
                });
        for (var dto : dtoLinkedList) {
            Optional<Block> lastBlock = blockchain.getLastBlock();
            boolean isAdded = lastBlock.map(block -> blockchain.tryAddNewBlock(dto.build(), block))
                    .orElseGet(() -> blockchain.tryAddNewBlock(dto.build()));
            if (!isAdded)
                throw new DeserializationValidationException("Block in blockchain that you provided in JSON file, is " +
                        "not completely valid. Block that is not valid - " + dto.build());

        }
        return blockchain;
    }

}
