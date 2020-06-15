package com.art.manager.config.login;

import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 17:11
 * @Description: 鉴权登录过滤器
 */
@Component
@Scope("prototype")
public class AuthLoginFilter extends BaseFilter implements PwLoginFilter {
    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        System.out.println("AuthLoginFilter--doFilter--");
        chain.doFilter(loginReqDTO, result);
    }
}
