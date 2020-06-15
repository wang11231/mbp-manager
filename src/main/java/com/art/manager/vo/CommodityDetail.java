package com.art.manager.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 公众号商品详情
 */
@Data
public class CommodityDetail {

    private Long id;
    /**
     * 作品图(多张)
     */
    private String[] picturesWorkss;
    private BigDecimal markePrice;
    private BigDecimal discountPrice;
    private Long specialId;
    private String specialName;
    private String introductionWorks;
    private String commdityName;
    private BigDecimal freight;
    /**
     * 展示图(单张)
     */
    private String[] showPictures;

    // 艺术家信息
    private Map<String, Object> artistInfo;
    // 基本信息
    private Map<String, Object> baseInfo;
    private Integer isBuy; // 购买状态
    private String buyName; // 购买人
    private BigDecimal price;// 价格  折扣价不为空=折扣价 反之=原价
    private String goodId; // 商品id
    /**
     * 库存
     */
    private Integer stock;
    private String dealTime; // 处理时间
    private Long interestCount;

}
