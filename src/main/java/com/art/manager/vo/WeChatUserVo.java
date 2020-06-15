package com.art.manager.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WeChatUserVo implements Serializable {

    private Long id;
    private String redisToken;  // redis key
    private String openid; // 用户的唯一标识
    private String nickname; // 用户昵称
    private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String headimgurl; // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效
    private String mobile;
    private BigDecimal bond; // 保证金
    private BigDecimal balance; // 余额
    private BigDecimal deduct; // 扣除金额
}
