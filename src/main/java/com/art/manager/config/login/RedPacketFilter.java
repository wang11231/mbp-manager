package com.art.manager.config.login;

import com.art.manager.config.login.base.ExceptionMsg;
import com.art.manager.config.login.base.PwLoginDTO;
import com.art.manager.config.login.base.PwLoginFilter;
import com.art.manager.config.login.base.Result;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 16:57
 * @Description: 红包过滤器
 */
@Component
@Scope("prototype")
public class RedPacketFilter implements PwLoginFilter {
    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        System.out.println("RedPacketFilter--doFilter--");
        chain.doFilter(loginReqDTO, result);
    }

}
