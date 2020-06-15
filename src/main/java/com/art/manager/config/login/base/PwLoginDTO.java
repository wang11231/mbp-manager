package com.art.manager.config.login.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jumping
 * @version 1.0.0
 * @time 2015/6/29
 */
@Getter
@Setter
@ToString(callSuper = true, exclude = {"loginPwd", "verifyCode", "imgVerifyKey"})
public class PwLoginDTO extends BaseDTO {

    /**
     * 登陆号
     * 邮箱、昵称、手机
     */
    private String productNo;

    /**
     * 登陆密码
     */
    private String loginPwd;

    /**
     * 加密因子
     */
    private String enCode;

    /**
     * 图片验证码KEY
     */
    private String imgVerifyKey;

    /**
     * 图形验证码
     */
    private String verifyCode;

    /**
     * session会话
     */
    private String sessionKey;

    /**
     * 手机ip
     */
    private String requestIp;

    /**
     * 临时token
     */
    private String temToken;

    /**
     * 滑动登录标志
     */
    private boolean slideLoginFlag;

    /**
     * 算法模式: 老密码控件为0，新密码控件为2，默认为0
     */
    private String algorithmMode;
}
