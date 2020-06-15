package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.OrderReq;
import com.art.manager.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 普通订单列表
     * @param orderReq
     * @return
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public Msg getOrderList(@RequestBody OrderReq orderReq){
        try {
            Map<String, Object> orderList = orderService.getOrderList(orderReq);
            return new Msg(Msg.SUCCESS_CODE, orderList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 查看订单信息
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/getOrderInfo")
    public Msg getOrderInfo(@RequestParam("orderNo") String orderNo){
        if(orderNo == null){
            return new Msg(Msg.FAILURE_CODE, "订单号为空");
        }
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            Map<String, Object> orderInfo = orderService.getOrderInfo(orderNo);
            return new Msg(Msg.SUCCESS_CODE, orderInfo);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 更新物流信息
     * @param params
     * @return
     */
    @RequestMapping(value = "/updateLogistics", method = RequestMethod.POST)
    public Msg updateLogistics(@RequestBody Map<String, Object> params){
        try {
            orderService.updateLogistics(params);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 更新订单状态
     * @param params
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Msg updateStatus(@RequestBody Map<String, Object> params){
        try {
            orderService.updateStatus(params);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
