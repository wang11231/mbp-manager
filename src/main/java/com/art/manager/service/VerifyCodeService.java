package com.art.manager.service;

import com.art.manager.pojo.Msg;

public interface VerifyCodeService {

    /**
     * 验证ip
     * @param ip
     * @param phoneNo
     * @return
     */
    boolean validateIp(String ip, String phoneNo);
    /**
     * 获取验证码
     * @param phoneno
     * @return
     */
    boolean getVerifyCode(String phoneno);

    /**
     * 验证验证码
     * @param phoneno
     * @param verifyCode
     * @return
     */
    Msg validateVerifyCode(String phoneno, String verifyCode);


    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    Msg sendVerifyCode(String phone);

}
