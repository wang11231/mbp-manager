package com.art.manager.pojo.wechat;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付结果实体类
 */
@Data
public class PaymentResult implements Serializable {

    private static final long serialVersionUID = 8714858579411928319L;

    private Long id;
    private String mobile;
    private String returnCode; //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String returnMsg; // 返回信息
    private String mchId; //微信支付分配的商户号
    private String resultCode; // 业务结果
    private String errCode; //错误代码
    private String errCodeDes; // 错误代码描述
    private String prepayId; // 预支付交易会话标识
    private Date createTime;
    private Date updateTime;
    private String orderNo;
    private String outTradeNo; // 商户订单号
    private BigDecimal totalFee; // 订单金额(退款时则代表应结退款金额	)
    private String transactionId; //微信支付订单号

    // 退款
    private String outRefundNo; //商户退款单号
    private String refundId; //微信退款单号
    private Integer refundStatus;// 退款状态 1:退款中  2：退款成功   3：退款异常  4：退款关闭

    // 提现
    private String paymentNo;  //微信付款单号

    private Integer paymentType; // 11:微信支付订单 12：余额支付订单 13：微信支付保证金 14：余额支付保证金 21：充值 22：保证金退回 31：提现
}
