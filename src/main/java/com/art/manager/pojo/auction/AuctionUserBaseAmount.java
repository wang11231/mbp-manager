package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户拍品保证金实体类
 */
@Getter
@Setter
@NoArgsConstructor
public class AuctionUserBaseAmount implements Serializable {
    private static final long serialVersionUID = -3420862337385489668L;
    private Long goodsId;
    private String goodsName;
    private String username;
    private BigDecimal bond;
    private String showPic;
    /**
     * 保证金
     */
    private BigDecimal baseAmount;
    private Integer baseAmountStatus;
    private Date createTime;
    private List<String> goodsStatuses;
    /**
     * 出价金额
     */
    private BigDecimal price;
    private String addPriceType;
}
