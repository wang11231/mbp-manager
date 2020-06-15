package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommodityDto {

    private Long id;
    private String commdityName;
    private String markePrice;
    private BigDecimal discountPrice;
    private String coreSpecification;
    // 展示图(单张)
    private String showPicture;
    private BigDecimal price;// 价格  折扣价不为空=折扣价 反之=原价
    /**
     * 库存
     */
    private Integer stock;

}
