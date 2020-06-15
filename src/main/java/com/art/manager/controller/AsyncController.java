package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @PostMapping("/getIn")
    @ResponseBody
    public Msg getIn() {
        DfsFileDealUtils instance = DfsFileDealUtils.getInstance();
        System.out.println(instance);
        return new Msg(Msg.SUCCESS_CODE, "ok");
    }

    @PostMapping("/get")
    public Msg getList() {
        try {
            String async = asyncService.Async();
            return new Msg(Msg.SUCCESS_CODE, async);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @PostMapping("/pool")
    public Msg pool() {
        try {
            String async = asyncService.pool();
            return new Msg(Msg.SUCCESS_CODE, async);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
