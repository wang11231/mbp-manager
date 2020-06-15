package com.art.manager.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommonCommodityVo {

    private Long id;
    private String commdityName; // 商品名
    private Integer typeCode; // 类别
    private Integer styleCode; // 风格
    private String typeName;
    private String styleName;
    private BigDecimal markePrice;  // 市场价
    private BigDecimal discountPrice; // 折扣价
    private Long specialId;
    private BigDecimal price;// 价格  折扣价不为空=折扣价 反之=原价
}
