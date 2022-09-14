package frank.antoniak.visualblockchain.blockchain.validation;

import frank.antoniak.visualblockchain.blockchain.block.Block;
import frank.antoniak.visualblockchain.blockchain.cryptography.receiver.VerifyMessage;
import frank.antoniak.visualblockchain.blockchain.messenger.MessageHolder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageValidation {

    public static boolean isBlockchainMessagesValid(List<Block> blockList) {
        return isIdValid(blockList) && isSignValid(blockList);
    }

    private static boolean test(MessageHolder message) {
        return new VerifyMessage(message).isMessageVerified();
    }

    private static boolean isSignValid(List<Block> blockList) {
        if (blockList.isEmpty())
            return true;
        return blockList.stream()
                .map(Block::getMessages)
                .flatMap(Collection::stream)
                .allMatch(MessageValidation::test);
    }

    private static boolean isIdValid(List<Block> blockList) {
        final AtomicInteger max = new AtomicInteger(0);
        return blockList.stream()
                .map(Block::getMessages)
                .flatMap(Collection::stream)
                .map(MessageHolder::getMessageData)
                .map(message -> message.charAt(4) - '0')
                .allMatch(n -> n >= max.getAndSet(n));
    }
}
