package uz.bookshop.exception;

import java.io.Serial;

public class UserStatisticsResponseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7573094468298549179L;

    public UserStatisticsResponseException(String message) {
        super(message);
    }
}
