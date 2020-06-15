package com.art.manager.exception;

/**
 * 单点登录异常
 * Create by yuan on 2018/9/27
 */
public class PointLoginException extends RuntimeException {
    public PointLoginException() {
    }

    public PointLoginException(String message) {
        super(message);
    }

    public PointLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointLoginException(Throwable cause) {
        super(cause);
    }

    public PointLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
