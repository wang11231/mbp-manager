package com.art.manager.exception;

/**
 * 发送验证码异常
 */
public class SendVerifyCodeException extends ManagerException {

    public SendVerifyCodeException() {
        super();
    }
    public SendVerifyCodeException(String message) {
        super(message);
    }

    public SendVerifyCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendVerifyCodeException(Throwable cause) {
        super(cause);
    }

    public SendVerifyCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
