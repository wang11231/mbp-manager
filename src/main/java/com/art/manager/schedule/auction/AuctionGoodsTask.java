package com.art.manager.schedule.auction;

import com.art.manager.mapper.OrderMapper;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.auction.AuctionOrderMapper;
import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.service.auction.AuctionOrderService;
import com.art.manager.service.auction.AuctionProxyLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拍品商品定时任务
 */
@Component
@Slf4j
public class AuctionGoodsTask {

    @Autowired
    private AuctionGoodsService auctionGoodsService;

    @Autowired
    private AuctionOrderService auctionOrderService;

    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Autowired
    private AuctionOrderMapper auctionOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 商品状态：1:未开始->2:拍卖中
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void updateGoodsStatusToInAction(){
        //修改商品状态，未开始1->拍卖中2
        auctionGoodsService.updateGoodsStatusToInAction();
        //查询商品对应代理出价且有效（proxyStatus=1）的参与者
        updateProxyPrice();
    }

    /**
     * 商品状态：2:拍卖中->3:竞拍成功或0:流拍
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateGoodsStatusToActionSuccess(){
        auctionGoodsService.updateGoodsStatusToActionSuccess();
    }

    /**
     * 订单状态：1:待支付->0:已失效
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void updateOrderStatusToExpired(){
        auctionOrderService.dealExpiredOrder();
    }


    /**
     * 订单状态：1:已发货->4:已收货
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void updateConfirmReceipt(){
        log.info("schedule updateConfirmReceipt start");
        int autionCount = auctionOrderMapper.updateConfirmReceipt();
        log.info("schedule updateConfirmReceipt autionCount:{}", autionCount);
        int count = orderMapper.updateConfirmReceipt();
        log.info("schedule updateConfirmReceipt count:{}", count);
    }

    @Autowired
    private AuctionProxyLimitService auctionProxyLimitService;

    private void updateProxyPrice(){
        //更新没有设置代理出价的拍品
        auctionGoodsMapper.batchUpdateAutoProxyToInvalid();
        //查询所有代理出价的商品
        List<AuctionProxyLimit> proxyList = auctionProxyLimitService.selectProxyStatusValid(null);
        if(proxyList == null || proxyList.size() == 0){
            return;
        }
        Map<Long, List<AuctionProxyLimit>> proxyMap = proxyList.stream().collect(Collectors.groupingBy(AuctionProxyLimit::getGoodsId));
        Iterator<Long> iter = proxyMap.keySet().iterator();
        while(iter.hasNext()){
            Long goodsId = iter.next();
            auctionGoodsService.updateProxyPrice(goodsId, proxyMap.get(goodsId));
        }
    }

}
