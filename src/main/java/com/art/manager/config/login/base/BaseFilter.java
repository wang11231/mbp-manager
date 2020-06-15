package com.art.manager.config.login.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 14:20
 * @Description: todo
 */
public class BaseFilter {

    @Getter
    @AllArgsConstructor
    public enum LoginErrorCode{
        lOGIN_IS_NULL("999999", "登陆号不能为空"),
        PW_IS_NULL("999999", "登陆密码不能为空"),
        ENCODE_IS_NULL("999999", "因子编号不能为空");

        private String errorCode;
        private String errorMsg;

    }


}
