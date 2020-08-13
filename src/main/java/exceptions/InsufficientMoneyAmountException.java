package exceptions;

public class InsufficientMoneyAmountException extends RuntimeException {
    public InsufficientMoneyAmountException(String message) {
        super(message);
    }
}
