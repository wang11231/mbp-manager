package com.art.manager.controller;

import com.art.manager.config.login.base.*;
import com.art.manager.config.login.chain.PwLoginFilterChain;
import com.art.manager.util.SpringUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/4 15:17
 * @Description: todo
 */
@RestController
@Slf4j
@RequestMapping("filter")
public class FilterController {

    @RequestMapping("authCenterPwLogin")
    public Response<LoginResDTO> authCenterPwLogin(@RequestBody PwLoginDTO loginReqDTO){
        log.info("LoginImpl started, loginName:{}, request params:{}", loginReqDTO.getProductNo(), loginReqDTO);
        try {
            Response response = authLogin(loginReqDTO);
            log.info("LoginImpl finished, loginName:{}, response:{}", loginReqDTO.getProductNo(), response);
            return response;
        } catch (AccountServiceException e) {
            log.error("LoginImpl meet error, loginName:{}, response:{}", loginReqDTO.getProductNo(), Throwables.getStackTraceAsString(e));
            return new Response(e.getCode(), e.getMessage());
        } catch (ExceptionMsg e) {
            log.error("LoginImpl meet error, loginName:{}, response:{}", loginReqDTO.getProductNo(), Throwables.getStackTraceAsString(e));
            return new Response(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("LoginImpl meet error, loginName:{}, response:{}", loginReqDTO.getProductNo(), Throwables.getStackTraceAsString(e));
            return new Response(e.getMessage(),e.getMessage());
        }
    }

    private Response authLogin(PwLoginDTO loginReqDTO) throws Exception, ExceptionMsg {
        Result result = new Result();
        PwLoginFilterChain pwLoginFilterChain = SpringUtils.getBean(PwLoginFilterChain.class);
        System.out.println("pwLoginFilterChain" + pwLoginFilterChain);
        pwLoginFilterChain.add(get(LoginFilterEnum.PARAMS_VALIDATION))
                .add(get(LoginFilterEnum.HALL_VERSION))
                .add(get(LoginFilterEnum.TYB_WHITELIST))
                .add(get(LoginFilterEnum.RISK_CHECK))
                .add(get(LoginFilterEnum.RED_PACKET))
                .add(get(LoginFilterEnum.AUTH_LOGIN));
        pwLoginFilterChain.doFilter(loginReqDTO, result);
        return result.getResponse();
    }

    private PwLoginFilter get(LoginFilterEnum loginFilterEnum){
        if(loginFilterEnum == null){
            return null;
        }
        String code = loginFilterEnum.getCode();
        return LoginFilterFactory.getInstance(code);
    }

}
