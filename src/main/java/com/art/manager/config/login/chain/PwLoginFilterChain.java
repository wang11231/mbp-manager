package com.art.manager.config.login.chain;

import com.art.manager.config.login.base.ExceptionMsg;
import com.art.manager.config.login.base.PwLoginDTO;
import com.art.manager.config.login.base.PwLoginFilter;
import com.art.manager.config.login.base.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 11:25
 * @Description: 密码登录过滤器链
 */
@Component
@Scope("prototype")
public class PwLoginFilterChain {

    private List<PwLoginFilter> filters = new ArrayList<>();
    private int currentPosition=0;

    public PwLoginFilterChain add(PwLoginFilter filter){
        if(filter == null){
            return this;
        }
        filters.add(filter);
        return this;
    }

    public void doFilter(PwLoginDTO loginReqDTO, Result result) throws Exception, ExceptionMsg {
        if(!result.getIsContinue() || currentPosition++ == filters.size()){
            return;
        }
        PwLoginFilter filter = filters.get(currentPosition - 1);
        filter.doFilter(loginReqDTO, result,this);
    }



}
