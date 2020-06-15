package com.art.manager.wechatcontroller;

import com.art.manager.pojo.Msg;
import com.art.manager.request.OrderReq;
import com.art.manager.service.CategoryConfigService;
import com.art.manager.service.CommonCommodityService;
import com.art.manager.service.OrderService;
import com.art.manager.service.auction.AuctionGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author hao.chang
 */
@Slf4j
@RestController
@RequestMapping("/wechatOrder")
public class WechatOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryConfigService categoryConfigService;
    @Autowired
    private CommonCommodityService commonCommodityService;
    @Autowired
    private AuctionGoodsService auctionGoodsService;


    /**
     * 普通商品下单
     */
    @RequestMapping(value = "/inserPtOrderNo", method = RequestMethod.POST)
    public Msg inserPtOrderNo( @RequestBody Map<String, Object> params, /*@RequestHeader(value = "token") String token,*/ HttpServletRequest request) {
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            Msg msg = orderService.inserPtOrderNo(params);
            return msg;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 商品风格列表
     */
    @RequestMapping(value = "/getStyleList", method = RequestMethod.GET)
    public Msg getStyleList(/*@RequestHeader(value = "token") String token,*/ HttpServletRequest request) {
        try {
            return new Msg(Msg.SUCCESS_CODE, categoryConfigService.getStyleList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 搜索页
     */
    @RequestMapping(value = "/getSearchList", method = RequestMethod.POST)
    public Msg getSearchList(/*@RequestHeader(value = "token") String token,*/ @RequestBody Map<String, Object> params ,HttpServletRequest request) {
        try {
            if(params.get("type").equals("1")){
                return new Msg(Msg.SUCCESS_CODE,  commonCommodityService.getSearchList(params));
            }else {
                return new Msg(Msg.SUCCESS_CODE,  auctionGoodsService.getAuctionGoodList(params));
            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Msg getList(/*@RequestHeader(value = "token") String token,*/ @RequestBody Map<String, Object> params ,HttpServletRequest request) {
        try {

            return new Msg(Msg.SUCCESS_CODE, commonCommodityService.getSearchList(params));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
    /**
     * 获取余额和成交价格
     */
    @RequestMapping(value = "/getOrderBalance", method = RequestMethod.POST)
    public Msg getOrderBalance(@RequestBody Map<String, Object> params) {
        try {
            return new Msg(Msg.SUCCESS_CODE, orderService.getOrderBalance(params));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 个人中心订单列表
     * @param req
     * @return
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public Msg getOrderList(@RequestBody OrderReq req){
        try {
            return new Msg(Msg.SUCCESS_CODE, orderService.wechatOrderList(req));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 删除订单
     * @param req
     * @return
     */
    @RequestMapping(value = "/delOrder", method = RequestMethod.POST)
    public Msg delOrder(@RequestBody OrderReq req){
        try {
            orderService.delOrder(req);
            return new Msg(Msg.SUCCESS_CODE, "删除成功",new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 确认收货
     * @param req
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Msg updateStatus(@RequestBody OrderReq req){
        try {
            orderService.updateOrderStatus(req);
            return new Msg(Msg.SUCCESS_CODE, "修改成功",new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 获取商品保证金和用户余额
     */
    @RequestMapping(value = "/getBalanceAmount", method = RequestMethod.POST)
    public Msg updateStatus( @RequestBody Map<String, Object> params){
        try {
            Map<String, Object> balanceAmount = auctionGoodsService.getBalanceAmount(params);
            return new Msg(Msg.SUCCESS_CODE, "成功",balanceAmount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}