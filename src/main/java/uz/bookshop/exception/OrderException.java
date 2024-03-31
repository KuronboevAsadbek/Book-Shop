package uz.bookshop.exception;

import java.io.Serial;

public class OrderException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5804954066030431818L;

    public OrderException(String message) {
        super(message);
    }
}
