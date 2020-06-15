package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.service.auction.AuctionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  拍品订单控制类
 */
@RestController
@RequestMapping("/auction/order")
@Slf4j
public class AuctionOrderController {

    @Autowired
    private AuctionOrderService auctionOrderService;

    @ResponseBody
    @PostMapping("/updateCompanyInfo")
    public Msg updateCompanyInfo(@RequestBody AuctionOrder auctionOrder) {
        try {
            boolean result = auctionOrderService.updateCompanyInfo(auctionOrder);
            return new Msg(Msg.SUCCESS_CODE, result ? "修改成功" : "修改失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectByOrderNo")
    public Msg selectByOrderNo(@RequestBody AuctionOrderReq req) {
        try {
            AuctionOrder auctionOrder = auctionOrderService.selectByOrderNo(req);
            return new Msg(Msg.SUCCESS_CODE, auctionOrder);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectByGoodsId")
    public Msg selectByGoodsId(@RequestBody AuctionOrderReq req) {
        try {
            AuctionOrder auctionOrder = auctionOrderService.selectByGoodsId(req);
            return new Msg(Msg.SUCCESS_CODE, auctionOrder);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getListByPage")
    public Msg getListByPage(@RequestBody AuctionOrderReq req) {
        try {
            Map<String, Object> list = auctionOrderService.getList(req);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/delayExpireTime")
    public Msg delayExpireTime(@RequestBody AuctionOrderReq req) {
        try {
            boolean result = auctionOrderService.delayExpireTime(req);
            return new Msg(Msg.SUCCESS_CODE, result ? "修改成功" : "修改失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/onePrice")
    public Msg onePrice(HttpServletRequest request, @RequestBody AuctionOrderReq req) {
        try {
            boolean result = auctionOrderService.onePrice(request, req);
            return new Msg( result ? Msg.SUCCESS_CODE : Msg.OTHER, result ? "操作成功" : "操作失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/immediatelyPrice")
    public Msg immediatelyPrice(HttpServletRequest request, @RequestBody AuctionOrderReq req) {
        try {
            boolean result = auctionOrderService.immediatelyPrice(request, req);
            return new Msg(Msg.SUCCESS_CODE, result ? "操作成功" : "操作失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
    //拍卖订单列表
    @RequestMapping(value = "/getAuctionOrderList", method = RequestMethod.POST)
    public Msg getAuctionOrderList(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> auctionOrderList = auctionOrderService.getAuctionOrderList(params);
            return new Msg(Msg.SUCCESS_CODE, "成功",auctionOrderList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    //拍卖订单列表
    @RequestMapping(value = "/getAuctionOrderListForEntry", method = RequestMethod.POST)
    public Msg getAuctionOrderListForEntry(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> auctionOrderList = auctionOrderService.getAuctionOrderListForEntry(params);
            return new Msg(Msg.SUCCESS_CODE, "成功",auctionOrderList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    //确认收货接口
    @RequestMapping(value = "/updateAuctionOrderStatus", method = RequestMethod.POST)
    public Msg updateAuctionOrderStatus(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        try {
            int i = auctionOrderService.updateAuctionOrderStatus(params);
            if(i > 0){
                return new Msg(Msg.SUCCESS_CODE, "成功");
            }
            return new Msg(Msg.FAILURE_CODE, "修改失败");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}
