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
public class AuctionUserBaseAmountReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 2701191191900899510L;
    private String mobile;
    /**
     * 历史0 冻结1
     */
    private String type;


}
