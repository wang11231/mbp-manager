package com.art.manager.controller.base;

import com.art.manager.exception.ParamsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;

@Slf4j
public class BaseController {

    /**
     * 验证错误信息
     * @param errors
     */
    public void validErrors(Errors errors){
        if (errors.hasErrors()) {
            log.error("param other error:{}", errors.getAllErrors().get(0).getDefaultMessage());
            throw new ParamsException(errors.getAllErrors().get(0).getCode(), errors.getAllErrors().get(0).getDefaultMessage());
        }
    }

}
