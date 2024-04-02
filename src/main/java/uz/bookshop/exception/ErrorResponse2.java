package uz.bookshop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.bookshop.utils.ResponseCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse2 {

    private int code;
    private String message;

    public static ErrorResponse2 of(ResponseCode responseCode, String message) {
        return new ErrorResponse2(responseCode.getCode(), message);
    }
}
