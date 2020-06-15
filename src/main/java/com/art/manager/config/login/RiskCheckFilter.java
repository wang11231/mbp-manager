package com.art.manager.config.login;

import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 13:46
 * @Description: 风控校验过滤器
 */
@Component
@Scope("prototype")
@Slf4j
public class RiskCheckFilter extends BaseFilter implements PwLoginFilter {
    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        System.out.println("RiskCheckFilter--doFilter--");
        chain.doFilter(loginReqDTO, result);
    }
}
