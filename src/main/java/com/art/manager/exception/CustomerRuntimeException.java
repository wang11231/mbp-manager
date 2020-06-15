package com.art.manager.exception;

/**
 * @Author: xieyongshan
 * @Date: 2019/10/25 14:20
 * @Description: todo
 */
public class CustomerRuntimeException extends RuntimeException{

    public CustomerRuntimeException() {
    }

    public CustomerRuntimeException(String message) {
        super(message);
    }

    public CustomerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerRuntimeException(Throwable cause) {
        super(cause);
    }

    public CustomerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
