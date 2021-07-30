package blockchain.serializer.exception;

public class DeserializationValidationException extends RuntimeException {

    public DeserializationValidationException(String message) {
        super(message);
    }
}
