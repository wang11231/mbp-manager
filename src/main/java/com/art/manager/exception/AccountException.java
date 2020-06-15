package com.art.manager.exception;

/**
 * 账户服务异常类
 */
public class AccountException extends ManagerException {

    public AccountException() {
        super();
    }
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountException(Throwable cause) {
        super(cause);
    }

    public AccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
