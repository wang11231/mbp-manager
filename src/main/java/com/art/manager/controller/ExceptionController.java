package com.art.manager.controller;

import com.art.manager.service.impl.ExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xieyongshan
 * @Date: 2019/10/25 13:58
 * @Description: todo
 */
@RestController
@Slf4j
@RequestMapping("exception")
public class ExceptionController {
    @Autowired
    private ExceptionService exceptionService;

    @RequestMapping("insert")
    public String insert(){
        exceptionService.execute();
        return "ok";
    }

}
