package com.art.manager.exception;

/**
 * 参数异常类
 */
public class ParamsException extends RuntimeException {

    private String code;

    public ParamsException() {
        super();
    }
    public ParamsException(String message) {
        super(message);
    }

    public ParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ParamsException(Throwable cause) {
        super(cause);
    }

    public ParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
