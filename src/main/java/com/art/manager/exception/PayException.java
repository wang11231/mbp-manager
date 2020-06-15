package com.art.manager.exception;

/**
 * 发送验证码异常
 */
public class PayException extends ManagerException {

    public PayException() {
        super();
    }
    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(Throwable cause) {
        super(cause);
    }

    public PayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
