package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.request.AuctionUserBaseAmountReq;
import com.art.manager.service.auction.AuctionBaseAmountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 *  拍品保证金控制类
 */
@RestController
@RequestMapping("/auction/baseAmount")
@Slf4j
public class AuctionBaseAmountController {

    @Autowired
    private AuctionBaseAmountService auctionBaseAmountService;


    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(HttpServletRequest request, @RequestBody AuctionBaseAmount auctionBaseAmount) {
        try{
            boolean result = auctionBaseAmountService.insert(request, auctionBaseAmount);
            return new Msg(Msg.SUCCESS_CODE, result ? "新增成功" : "新增失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectBaseAmount")
    public Msg selectBaseAmount(HttpServletRequest request, @RequestBody AuctionBaseAmount auctionBaseAmount) {
        try {
            BigDecimal result = auctionBaseAmountService.selectByUserAndGoodId(request, auctionBaseAmount);
            return new Msg(Msg.SUCCESS_CODE, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getUserBaseAmountList")
    public Msg getUserBaseAmountList(@RequestBody AuctionUserBaseAmountReq auctionUserBaseAmountReq) {
        try {
            Map<String, Object> userBaseAmountList = auctionBaseAmountService.getUserBaseAmountList(auctionUserBaseAmountReq);
            return new Msg(Msg.SUCCESS_CODE, userBaseAmountList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }



}
