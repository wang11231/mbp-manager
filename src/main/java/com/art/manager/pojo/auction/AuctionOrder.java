package com.art.manager.pojo.auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 拍卖订单实体类
 * @author lenovo
 */
@Getter
@Setter
@NoArgsConstructor
public class AuctionOrder implements Serializable {

    private static final long serialVersionUID = 3284599008497373809L;
    private Long id;
    private String orderNo;
    private Long goodsId;
    private String goodsName;
    private Long buyerId;
    private String buyerName;
    private Long payerId;
    private String payerName;
    private BigDecimal dealPrice;
    private Date buyerDate;
    private Date expireDate;
    private Date dealDate;
    private Date payDate;
    private Long styleCode;
    private String categoryName;
    private String orderStatus;
    private String transportCompany;
    private String transportNo;
    private String transportAddr;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Long typeCode;
    private String parentName;
    private String operator;
    private String artistName;
    private String showPic;
    private Date currentTime;
    private String username;
    private String receiveAddr;
    private String receiveContact;
    private String receivePhone;
    private Long addrId;
    private String auctionGoodsId;
    private BigDecimal transportamount;

}