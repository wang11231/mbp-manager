package com.art.manager.config.login.base;

import lombok.Getter;

/**
 * AnteaterAccException.
 *
 * @author CWZ
 * @date 2015/6/29
 * 业务异常处理
 */
public class AccountServiceException extends RuntimeException {
   
	private static final long serialVersionUID = 1165876351848409310L;

	@Getter
    private String code;

    @Getter
    private String resMessage;

    public AccountServiceException(String code) {
        this.code = code;
    }

    public AccountServiceException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public AccountServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AccountServiceException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

}
