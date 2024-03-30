package uz.bookshop.exception;

import java.io.Serial;

public class CartException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 5804954066030431818L;

    public CartException(String message){
        super(message);
    }
}
