package com.art.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务枚举类
 */
@Getter
@AllArgsConstructor
public enum ScheduleEnum {
    AUCTION_GOODS("auction_goods","拍卖商品"),
    AUCTION_ORDER("auction_order","拍卖订单");
    private String code;
    private String desc;

}
