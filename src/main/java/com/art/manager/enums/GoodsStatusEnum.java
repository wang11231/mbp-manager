package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态枚举类
 */
@Getter
@AllArgsConstructor
public enum GoodsStatusEnum {
    AUCTION_FAIL("0","流拍"),
    AUCTION_pre("1","未开始"),
    AUCTION_IN("2","拍卖中"),
    AUCTION_SUCCESS("3","竞拍成功"),
    AUCTION_SALE("4","已出售");
    private String code;
    private String desc;
}
