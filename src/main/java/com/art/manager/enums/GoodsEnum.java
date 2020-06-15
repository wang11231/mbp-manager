package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举类
 */
@Getter
@AllArgsConstructor
public enum GoodsEnum {
    COMMON("common","普通商品"),
    AUCTION("auction","拍卖商品"),
    TRANSFERS("transfers", "提现");
    private String code;
    private String desc;
}
