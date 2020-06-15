package com.art.manager.config.login;

import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 11:28
 * @Description: 添益宝白名单过滤器
 */
@Component
@Scope("prototype")
public class TybWhiteListFilter extends BaseFilter implements PwLoginFilter {

    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        System.out.println("TybWhiteListFilter--doFilter--");
        boolean flag = false;
        if(flag){
            result.setIsContinue(false);
            LoginResDTO retLoginResDTO = new LoginResDTO();
            retLoginResDTO.setCertNo("56565445456");
            retLoginResDTO.setGrade("d");
            result.setResponse(new Response<>(retLoginResDTO));
            return;
        }
        chain.doFilter(loginReqDTO, result);
    }
}
