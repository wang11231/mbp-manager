package com.art.manager.service.auction;

import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.request.AuctionProcessReq;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AuctionProcessService {

    /**
     * 插入AuctionProcess
     * @param auctionProcess
     * @return
     */
    boolean insert(AuctionProcess auctionProcess);

    /**
     * 查询指定订单的竞拍流程
     * @param auctionProcessReq
     * @return
     */
    Map<String, Object> getList(AuctionProcessReq auctionProcessReq);

    /**
     * 查询指定goodId对应的当前价格
     * @param goodsId
     * @return
     */
    AuctionProcess getAuctionProcess(Long goodsId);

    /**
     * 查询指定订单的竞拍流程
     * @param auctionProcessReq
     * @return
     */
    List<Map<String, Object>> selectByUsername(AuctionProcessReq auctionProcessReq);

}
