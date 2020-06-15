package com.art.manager.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class H5AuctionGoodsVo implements Serializable {
    private static final long serialVersionUID = 6851083900503555521L;
    private Long id;
    private String  goodsName;
    private String  showPic;
    private BigDecimal  startPrice;
    private Date startDate;
    private Date  endDate;
    private String  goodsStatus;
    private String  username;
    private BigDecimal  currentPrice;
    private BigDecimal  baseAmount;
    /**
     * 关注人数
     */
    private Long interestCount;
    private Date createTime;
    private Date updateTime;
    private String payDate;
    private String buyerName;
    /**
     * 2：结束，1 未结束
     */
    private Integer finished;

}
