package com.art.manager.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommodityVo {

    private Long id;
    private String specialTitle; // 专场标题
    private BigDecimal markePrice;  // 市场价
    private String coreSpecification; // 画芯规格
    private String showPicture; // 展示图(单张)
    private String[] picturesWorkss; // 作品图(多张)
    private BigDecimal price;// 价格  折扣价不为空=折扣价 反之=原价

}
