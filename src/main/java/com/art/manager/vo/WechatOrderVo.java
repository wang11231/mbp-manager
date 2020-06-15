package com.art.manager.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WechatOrderVo implements Serializable {

    private static final long serialVersionUID = -6391596557093814113L;
    private Long id;
    private String orderNo; // 订单号
    private String commodityName; // 商品名
    private String artist; // 艺术家
    private BigDecimal dealPrice; // 成交价
    /**
     *  展示图(单张)
     */
    private String[] showPictures;
    /**
     *   展示图(单张)
     */
    private String showPicture;
    private String coreSpecification; // 画芯规格
    /**
     * 创作年代
     */
    private String creationYear;
    private Integer status; // 状态   0:代付款 1:代发货 2：已发货 3：待收货
    private BigDecimal markePrice;  // 市场价
    private String transportAddr; // 物流地址

    private Long commodityId;
    //物流公司
    private String logisticsCompany;
    //物流单号
    private String logisticsNo;
}
