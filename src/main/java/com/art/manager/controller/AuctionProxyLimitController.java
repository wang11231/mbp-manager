package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.service.auction.AuctionProxyLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 *  拍品代理出价控制类
 */
@RestController
@RequestMapping("/auction/proxyLimit")
@Slf4j
public class AuctionProxyLimitController {

    @Autowired
    private AuctionProxyLimitService auctionProxyLimitService;


    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(HttpServletRequest request, @RequestBody AuctionProxyLimit auctionProxyLimit) {
        try{
            boolean result = auctionProxyLimitService.insert(request, auctionProxyLimit);
            return new Msg(Msg.SUCCESS_CODE, result ? "操作成功" : "操作失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectProxyLimit")
    public Msg selectProxyLimit(HttpServletRequest request, @RequestBody AuctionProxyLimit auctionProxyLimit) {
        try {
            BigDecimal result = auctionProxyLimitService.selectByUserAndGoodId(request, auctionProxyLimit);
            return new Msg(Msg.SUCCESS_CODE, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
