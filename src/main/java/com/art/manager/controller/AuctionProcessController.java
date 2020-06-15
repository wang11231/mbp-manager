package com.art.manager.controller;

import com.art.manager.controller.base.BaseController;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.request.AuctionProcessReq;
import com.art.manager.service.auction.AuctionProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  拍品订单流程控制类
 */
@RestController
@RequestMapping("/auction/process")
@Slf4j
public class AuctionProcessController extends BaseController {

    @Autowired
    private AuctionProcessService auctionProcessService;

    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(@RequestBody @Valid AuctionProcess auctionProcess, Errors errors) {
        try {
            validErrors(errors);
            boolean result = auctionProcessService.insert(auctionProcess);
            return new Msg(Msg.SUCCESS_CODE, result ? "新增成功" : "新增失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getList")
    public Msg getList(@RequestBody AuctionProcessReq auctionProcessReq) {
        try {
            Map<String, Object> list = auctionProcessService.getList(auctionProcessReq);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectByUsername")
    public Msg selectByUsername(@RequestBody AuctionProcessReq auctionProcessReq) {
        try {
            List<Map<String, Object>> result = auctionProcessService.selectByUsername(auctionProcessReq);
            return new Msg(Msg.SUCCESS_CODE, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    


}
