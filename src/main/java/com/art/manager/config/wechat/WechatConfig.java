package com.art.manager.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "wechat", ignoreUnknownFields = false)
@PropertySource("classpath:config/wechat.properties")
@Data
public class WechatConfig {
    /*
     * 自定义token, 用作生成签名,从而验证安全性
     * */
    private String token;

    private String appid;

    private String secret;

    private String callBack;

    private String mchId;

    private String deviceInfo;

    private String merchantKey;

    private String notifyUrl;

    private String JSPAPIUrl;

    private String queryPayUrl;

    private String closeorder;

    private String refund;

    private String refundquery;

    private String refundNotifyUrl;  // 退款通知接口

    private String transfers; //企业付款到零钱
}
