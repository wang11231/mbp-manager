package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 拍品保证金实体类
 */
@Getter
@Setter
@NoArgsConstructor
public class AuctionBaseAmount implements Serializable {
    private static final long serialVersionUID = -3003358930565741552L;
    private Long id;
    private Long userId;
    private String username;
    private Long goodsId;
    private BigDecimal baseAmount;
    private Integer baseAmountStatus;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String payType;

}
