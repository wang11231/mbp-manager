package com.art.manager.config.login.base;

import com.art.manager.config.login.*;
import com.art.manager.util.SpringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/4 14:13
 * @Description: 登录过滤工厂
 */
public class LoginFilterFactory {
    public static PwLoginFilter getInstance(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        switch (code){
            case "params_validation":
                ParamsValidationFilter bean1 = SpringUtils.getBean(ParamsValidationFilter.class);
                System.out.println("ParamsValidationFilter:"+ bean1);
                return bean1;
            case "hall_version":
                HallVersionFilter bean2 = SpringUtils.getBean(HallVersionFilter.class);
                System.out.println("HallVersionFilter:"+ bean2);
                return bean2;
            case "tyb_whitelist":
                TybWhiteListFilter bean3 = SpringUtils.getBean(TybWhiteListFilter.class);
                System.out.println("HallVersionFilter:"+ bean3);
                return bean3;
            case "session_key":
                SessionKeyFilter bean4 = SpringUtils.getBean(SessionKeyFilter.class);
                System.out.println("HallVersionFilter:"+ bean4);
                return bean4;
            case "risk_check":
                RiskCheckFilter bean5 = SpringUtils.getBean(RiskCheckFilter.class);
                System.out.println("HallVersionFilter:"+ bean5);
                return bean5;
            case "red_packet":
                RedPacketFilter bean6 = SpringUtils.getBean(RedPacketFilter.class);
                System.out.println("HallVersionFilter:"+ bean6);
                return bean6;
            case "auth_login":
                AuthLoginFilter bean7 = SpringUtils.getBean(AuthLoginFilter.class);
                System.out.println("HallVersionFilter:"+ bean7);
                return bean7;
            default:
                return null;
        }
    }

}
