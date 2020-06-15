package com.art.manager.config.login;
import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 11:31
 * @Description: 参数校验过滤器
 */
@Component
@Scope("prototype")
public class ParamsValidationFilter extends BaseFilter implements PwLoginFilter {
    @Override
    public void doFilter(PwLoginDTO loginReqDTO, Result result, PwLoginFilterChain chain) throws Exception, ExceptionMsg {
        String productNo = loginReqDTO.getProductNo();
        if(StringUtils.isBlank(productNo)){
            throw new AccountServiceException(LoginErrorCode.lOGIN_IS_NULL.getErrorCode(), LoginErrorCode.lOGIN_IS_NULL.getErrorMsg());
        }
        String loginPwd = loginReqDTO.getLoginPwd();
        if(StringUtils.isBlank(loginPwd)){
            throw new AccountServiceException(LoginErrorCode.PW_IS_NULL.getErrorCode(), LoginErrorCode.PW_IS_NULL.getErrorMsg());
        }
        String enCode = loginReqDTO.getEnCode();
        if(StringUtils.isBlank(enCode)){
            throw new AccountServiceException(LoginErrorCode.ENCODE_IS_NULL.getErrorCode(), LoginErrorCode.ENCODE_IS_NULL.getErrorMsg());
        }
        System.out.println("ParamsValidationFilter--doFilter--");
        chain.doFilter(loginReqDTO, result);
    }
}
