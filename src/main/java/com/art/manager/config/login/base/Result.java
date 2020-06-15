package com.art.manager.config.login.base;

import lombok.Data;

/**
 * @Author: xieyongshan
 * @Date: 2019/9/3 16:48
 * @Description: todo
 */
@Data
public class Result {

    private LoginResDTO loginResDTO;

    //private CustomerAndAcctResultDto customerAndAcctResultDto;

    private Response<LoginResDTO> response;

    private Boolean isContinue = true;
}
