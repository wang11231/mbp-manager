package com.art.manager.config.login.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/4 13:57
 * @Description: 过滤器枚举类
 */
@Getter
@AllArgsConstructor
public enum LoginFilterEnum {

    PARAMS_VALIDATION("params_validation","参数验证"),
    HALL_VERSION("hall_version","大厅版本"),
    TYB_WHITELIST("tyb_whitelist","添益宝白名单"),
    SESSION_KEY("session_key","session校验"),
    RISK_CHECK("risk_check","风控校验"),
    RED_PACKET("red_packet","红包"),
    AUTH_LOGIN("auth_login","鉴权登录");

    private String code;
    private String desc;


}
