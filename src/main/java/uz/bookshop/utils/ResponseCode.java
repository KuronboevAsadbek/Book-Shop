package uz.bookshop.utils;

import lombok.Getter;


@Getter
public enum ResponseCode {

    REQUIRED_DATA_MISSING(4001),
    ;

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

}
