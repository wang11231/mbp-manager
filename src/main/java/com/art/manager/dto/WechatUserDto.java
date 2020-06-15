package com.art.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WechatUserDto {
    private Long id;
    private String nickname; // 用户昵称
    private String mobile;
    private BigDecimal bond; // 保证金
    private BigDecimal balance; // 余额
    private Integer buyNum; // 购买数量
    private Integer unpaidNum; // 未付款数量
    private Date createTime;
    private Date lastLoginTime; // 最后登录时间
    private String headimgurl;
}
