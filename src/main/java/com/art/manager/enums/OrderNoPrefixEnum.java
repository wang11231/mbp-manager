package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单前缀枚举类
 */
@Getter
@AllArgsConstructor
public enum OrderNoPrefixEnum {
    PP("PP","拍品商品"),
    PT("PT","普通商品"),
    CZ("CZ","充值"),
    BZJ("BZJ", "保证金"),
    TX("TX", "提现");
    private String code;
    private String desc;
}
