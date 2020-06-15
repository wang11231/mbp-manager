package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CommonCommodityDto {
    private Long id;
    private Long specialId;
    private String commdityName; // 商品名
    private String artist; // 艺术家
    private Long artistId; // 艺术家
    private Integer typeCode; // 类别
    private Integer styleCode; // 风格
    private BigDecimal markePrice;  // 市场价
    private BigDecimal discountPrice; // 折扣价
    private String status; // 商品状态 0：正常  1：已删除 2：下线
    private Date createTime;
    private Date updateTime;
    private String showPicture; // 展示图(单张)
    private String[] showPictures; // 展示图(单张)
    private String specialField; // 专场
    private String[] picturesWorks; // 作品图(多张)
    private String typeName;
    private String styleName;
    private String operator; // 操作者
    /**
     * 是否是推荐商品
     */
    private String isRecommend;

    private String goodId;

    private Integer stock;

    private Integer goodStatus; //1-未售 0-已售
    private Long interestCount;
    private BigDecimal freight;// 运费
}
