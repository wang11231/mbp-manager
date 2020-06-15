package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderReq extends BaseReq implements Serializable {

    private String orderNo; // 订单号
    private String accountNumber; // 下单人账号
    private Date orderTime; // 订单时间
    private Integer status; // 状态
    private Integer typeCode; // 种类
    private String typeName;
    private String styleName;
    private String mobile;


}
