package com.art.manager.config.login;

import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 11:29
 * @Description: SessionKey过滤器
 */
@Component
@Scope("prototype")
public class SessionKeyFilter extends BaseFilter implements PwLoginFilter {
    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        System.out.println("SessionKeyFilter--doFilter--");
        chain.doFilter(loginReqDTO, result);
    }
}
