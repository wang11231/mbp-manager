package com.art.manager.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVo {

    private Long id;
    private String orderNo; // 订单号
    private String commodityName; // 商品名
    private String accountNumber; // 下单人账号
    private String draweeNumber; // 付款人账号
    private String typeName;
    private String styleName;
    private Date orderTime; // 下单时间
    private Date dealTime; // 处理时间
    private Integer status; // 状态   0:代付款 1:代发货 2：已发货 3：待收货
    private BigDecimal dealPrice; // 成交价
    private String operator;
    /**
     *   运费
     */
    private BigDecimal freight;
    private String receiverName; // 收货人姓名
    private String receiverMobile; // 收货人电话
    private String logisticsAddress;
    private Integer delFlag; // 0:正常 1：已删除
    private String goodId; // 商品id

}
