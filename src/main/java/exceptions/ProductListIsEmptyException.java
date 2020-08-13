package exceptions;

public class ProductListIsEmptyException extends RuntimeException {
    public ProductListIsEmptyException(String message) {
        super(message);
    }

}
