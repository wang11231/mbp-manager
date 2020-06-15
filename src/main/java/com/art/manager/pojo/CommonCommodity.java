package com.art.manager.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data

public class CommonCommodity {
    private Long id;
    @NotNull(message = "专场不能为空")
    private Long specialId;
    private String commdityName; // 商品名
    private BigDecimal markePrice;  // 市场价
    private BigDecimal discountPrice; // 折扣价
    @NotNull(message = "商品类别不能为空")
    private Integer typeCode; // 类别
    @NotNull(message = "商品风格不能为空")
    private Integer styleCode; // 风格
    private String status; // 商品状态 0：正常  1：已删除 2：下线 默认2
    private Date createTime;
    private Date updateTime;
    private String coreSpecification; // 画芯规格
    /**
     * 创作年代
     */
    private String creationYear;
    /**
     *   题材
     */
    private String theme;
    /**
     *   展示图(单张)
     */
    private String showPicture;
    /**
     *   运费
     */
    private BigDecimal freight;
    /**
     *  作者
     */
    private String author;
    /**
     *  使用场景
     */
    private String useScene;
    /**
     *  作品介绍
     */
    private String introductionWorks;
    @NotNull(message = "艺术家不能为空")
    private Long artistId;
    /**
     *  展示图(单张)
     */
    private String[] showPictures;
    /**
     *  作品图(多张)
     */
    private String[] picturesWorkss;
    /**
     *  操作者
     */
    private String operator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;
    /**
     *  艺术家
     */
    private String artist;
    private String typeName;
    private String styleName;
    /**
     * 专场
     */
    private String specialField;
    /**
     * 是否是精品推荐
     */
    private String isRecommend;

    /**
     * 库存
     */
    private Integer stock;
    private String goodId; // 商品id
    private BigDecimal price;// 价格  折扣价不为空=折扣价 反之=原价
    /**
     * 备用字段
     */
    private String backup1;
    private String backup2;
    private String backup3;
    private Long interestCount;

    public static void main(String[] args) {

        System.out.println(new BigDecimal("9.9"));

    }

}
