package com.art.manager.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentResultVo implements Serializable {
    private Long id;
    private String mobile;
    private String resultCode; // 业务结果
    private String errCodeDes; // 错误代码描述
    private Date createTime;
    private Date updateTime;
    private String status ; //1 支出  2 提现
    private String outTradeNo; // 商户订单号
    private BigDecimal totalFee; // 订单金额
}
