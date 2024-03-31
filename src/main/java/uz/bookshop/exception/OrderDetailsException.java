package uz.bookshop.exception;


import java.io.Serial;

public class OrderDetailsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6775881588764668777L;

    public OrderDetailsException(String message) {
        super(message);
    }
}
