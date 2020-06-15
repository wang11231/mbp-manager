package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式枚举类
 */
@Getter
@AllArgsConstructor
public enum PayTypeEnum {
    WX("wx","微信"),
    BALANCE("balance","余额");
    private String code;
    private String desc;
}
