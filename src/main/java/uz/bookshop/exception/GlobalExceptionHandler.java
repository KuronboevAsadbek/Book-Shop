package uz.bookshop.exception;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.bookshop.utils.ResponseCode;

@ControllerAdvice
@Slf4j
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

    @ExceptionHandler(OrderDetailsException.class)
    public ResponseEntity<Object> handleCartException(OrderDetailsException cartException) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse
                        .builder()
                        .message(cartException.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(UserStatisticsResponseException.class)
    public ResponseEntity<Object> handleUserStatisticsException(UserStatisticsResponseException userStatisticsResponse) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse
                        .builder()
                        .message(userStatisticsResponse.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse2> on(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse2 errorResponse = ErrorResponse2.of(ResponseCode.REQUIRED_DATA_MISSING, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
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

