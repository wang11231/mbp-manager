package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保证金列表明细dto
 *
 */
@Data
public class AuctionStartPriceDto {
    private String imgUrl;
    private String goodsName;
    private BigDecimal baseAmount;
    //一口价
    private BigDecimal onePrice;
    //加价幅度
    private BigDecimal incrementStep;
    //起拍价
    private BigDecimal startPrice;
    //拍品 加价形式：1、代理出价 2、立即出价 3、一口价 '
    private String addPriceType;
    //拍品当前价格
    private BigDecimal currentPrice;
    //当前代理出价
    private BigDecimal agencyPrice;

    //当前排名
    private String processStatus;
    //状态 1成功返回 2 扣除 3 冻结
    private String status;
    private Date updateTime;


}