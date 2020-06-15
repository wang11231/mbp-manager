package com.art.manager.exception;

/**
 * 豆沙包业务异常类
 */
public class AuctionGoodsException extends RuntimeException{

    public AuctionGoodsException() {
    }

    public AuctionGoodsException(String message) {
        super(message);
    }

    public AuctionGoodsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuctionGoodsException(Throwable cause) {
        super(cause);
    }

    public AuctionGoodsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
