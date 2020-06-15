package com.art.manager.service.auction;

import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.request.AuctionOrderReq;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public interface AuctionOrderService {

    /**
     * 延期订单2小时
     * @param req
     * @return
     */
    boolean delayExpireTime(AuctionOrderReq req);

    /**
     * 分页查询订单
     * @param auctionOrderReq
     * @return
     */
    Map<String, Object> getList(AuctionOrderReq auctionOrderReq);

    /**
     * 根据订单号查询订单信息
     * @param auctionOrderReq
     * @return
     */
    AuctionOrder selectByOrderNo(AuctionOrderReq auctionOrderReq);

    /**
     * 根据订单号修改物流信息
     * @param auctionOrder
     * @return
     */
    boolean updateCompanyInfo(AuctionOrder auctionOrder);

    /**
     * 一口价生成订单
     * @param request
     * @param auctionOrderReq
     * @return
     */
    boolean onePrice(HttpServletRequest request, AuctionOrderReq auctionOrderReq);

    /**
     * 立即出价生成订单
     * @param request
     * @param auctionOrderReq
     * @return
     */
    boolean immediatelyPrice(HttpServletRequest request, AuctionOrderReq auctionOrderReq);

    /**
     * 根据支付返回的订单号更改状态
     * @param orderNo
     * @param goodsId
     * @param username
     * @return
     */
    int updateStatusByOrderNo(String orderNo, Long goodsId, String username);

    Map<String, Object> getAuctionOrderList(Map<String, Object> params) throws ParseException;

    Map<String, Object> getAuctionOrderListForEntry(Map<String, Object> params) throws ParseException;

    int updateAuctionOrderStatus(Map<String, Object> params);
    /**
     * 定时任务：处理过期订单
     */
    void dealExpiredOrder();

    AuctionOrder selectByGoodsId(AuctionOrderReq req);
}
