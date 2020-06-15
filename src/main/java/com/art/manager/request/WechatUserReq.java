package com.art.manager.request;

import com.art.manager.request.base.BaseReq;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WechatUserReq  extends BaseReq implements Serializable{

    private static final long serialVersionUID = -3361800832866282317L;
    private String mobile;
    private String orderNo;
    private BigDecimal amount; // 订单总金额 单位分
    private Integer refundAmount; // 退款金额 单位分
    private Integer type; // 支付类型  1：微信支付  2：充值
    private String specialName;
    private String openid;
    private Long id; //商品自增id
}
