package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 付款类型枚举类
 */
@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    WX_PAY_ORDER(11,"微信支付订单"),
    BALANCE_PAY_ORDER(12,"余额支付订单"),
    WX_PAY_BASE_AMOUNT(13,"微信支付保证金"),
    BALANCE_PAY_BASE_AMOUNT(14,"余额支付保证金"),
    DEPOSIT(21,"充值"),
    BASE_AMOUNT_BACK(22,"保证金退回"),
    TRANSFER(31,"转账"),
    REFUND(41, "退款"),
    TRANSFERS(51, "提现");
    private Integer code;
    private String desc;

}
