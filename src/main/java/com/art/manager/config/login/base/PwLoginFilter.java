package com.art.manager.config.login.base;


import com.art.manager.config.login.chain.PwLoginFilterChain;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 11:21
 * @Description: 密码登录过滤器
 */
public interface PwLoginFilter {

    void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg;

}
