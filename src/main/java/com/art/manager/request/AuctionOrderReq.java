package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
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
public class AuctionOrderReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = -2169026354319768553L;
    private Long id;
    private Long goodsId;
    private String orderNo;
    private String goodsName;
    private String buyerName;
    private String orderStatus;
    private Date dealDate;
    private Date buyerDate;
    private String username;
    private String operator;
    private String addPriceType;
    private BigDecimal currentPrice;
    private Long addrId;

}
