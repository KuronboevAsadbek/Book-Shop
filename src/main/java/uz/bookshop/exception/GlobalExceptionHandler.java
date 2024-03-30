package uz.bookshop.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<Object> handleUserException(UserException userException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(userException.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(BookException.class)
    @ResponseBody
    public ResponseEntity<Object> handleBookException(BookException bookException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(bookException.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(CommentException.class)
    @ResponseBody
    public ResponseEntity<Object> handleCommentException(CommentException commentException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(commentException.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<Object> handleCartException(CartException cartException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(cartException.getMessage())
                        .build()
                );
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorResponse {
        private String message;
    }
}

