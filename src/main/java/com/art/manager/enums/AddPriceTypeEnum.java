package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 加价形式枚举类
 */
@Getter
@AllArgsConstructor
public enum AddPriceTypeEnum {
    PROXY("1","代理出价"),
    IMMEDIATELY("2","立即出价"),
    ONE("3","一口价");
    private String code;
    private String desc;
}
