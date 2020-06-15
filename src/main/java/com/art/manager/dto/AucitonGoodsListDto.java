package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AucitonGoodsListDto {
    //商品Id
    private Long goodsId;
    //商品名称
    private String goodsName;

    //商品价格
    private BigDecimal goodsPrice;

    //商品图片
    private String showPic;

    //用户出价方式
    private  String addPriceType;
    //用户出价
    private BigDecimal userPrice;

    //商品状态
    private String goodsStatus;

    //拍卖开始时间
    private Date startDate;

    //拍卖结束时间
    private Date endDate;

    //拍卖中:用户是否领先
    private String processStatus;

    //竞拍成功: 是否超时
    private Boolean isOverTime;

    //订单号
    private String orderNo;

    //订单状态
    private String orderStatus;

    //支付结束时间
    private Date orderTime;

    //运费
    private BigDecimal transportAmount;
    //竞拍价格
    private BigDecimal currentPrice;

    //当前时间
    private String currentTime;

    //插入时间
    private Date createTime;
    //到期时间
    private Date expireDate;

    //一口价
    private BigDecimal onePrice;

    //代理价格
    private BigDecimal proxyPrice;

    //购买人
    private String buyerName;



}