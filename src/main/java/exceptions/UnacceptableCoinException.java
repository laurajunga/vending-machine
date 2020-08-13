package exceptions;

public class UnacceptableCoinException extends RuntimeException {
    public UnacceptableCoinException(String message) {
        super(message);
    }
}
