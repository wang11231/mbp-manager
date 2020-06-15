package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hao.chang
 */
@Data
public class SpecialAndCommodityDto {
    private Long id;
    /**
     * 专场类型ID
     */
    private Long typeId;
    /**
     *    专场封面URL
     */
    private String special;

    /**
     * 专场标题
     */
    private String specialTitle;

    /**
     * 市场价
     */
    private BigDecimal markePrice;
    /**
     * 折扣价
     */
    private BigDecimal discountPrice;
    /**
     * 尺寸
     */
    private String coreSpecification;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}