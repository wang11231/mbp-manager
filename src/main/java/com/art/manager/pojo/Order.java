package com.art.manager.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Order {

    private Long id;
    private String orderNo; // 订单号
    private String commodityName; // 商品名
    private String accountNumber; // 下单人账号
    private String draweeNumber; // 付款人账号
    private String typeName;
    private String styleName;
    private Integer typeCode;
    private Integer styleCode;
    private Date orderTime; // 下单时间
    private Date dealTime; // 处理时间
    private Integer status; // 状态   0:代付款 1:代发货 2：已发货 3：待收货
    private String logisticsNo; // 物流单号
    private String logisticsAddress; // 收货地址
    private BigDecimal dealPrice; // 成交价
    private String artist; // 艺术家
    private String detailedAddress; //具体地址
    private String receiverName;
    private String receiverMobile;
    private String specialField; // 专场
    private String operator;
    private Integer delFlag; // 0:正常 1：已删除
    private String logisticsCompany; // 物流公司
    private String transportAddr; // 物流地址
    private String cnAddress; //省市区中文地址
    private Date createTime;
    private Date updateTime;

}
