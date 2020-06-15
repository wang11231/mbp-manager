package com.art.manager.service.auction;

import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.H5AuctionGoodsReq;
import com.art.manager.vo.AuctionGoodsVo;
import com.art.manager.vo.H5AuctionGoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuctionGoodsService {

    boolean insert(AuctionGoods auctionGoods);

    boolean deleteByIds(List<Long> ids);

    boolean updateById(AuctionGoods auctionGoods);

    AuctionGoodsVo selectById(AuctionGoodsReq auctionGoodsReq, boolean isH5);

    AuctionGoodsVo getAuctionGoods(Long goodsId);

    Map<String, Object> getList(AuctionGoodsReq auctionGoodsReq);

    void updateGoodsStatusToActionSuccess(Long goodsId, String remark);

    /**
     * 定时任务
     */
    void updateGoodsStatusToInAction();
    /**
     * 定时任务
     */
    void updateGoodsStatusToActionSuccess();
    /**
     * 定时任务
     */
    void updateProxyPrice(Long goodsId, List<AuctionProxyLimit> auctionProxyLimits);

    Map<String, Object> getAuctionGoodList(Map<String, Object> params);

    Map<String, Object> getH5Goods(H5AuctionGoodsReq req);

    Map<String, Object> getH5GoodsForToday(H5AuctionGoodsReq req);

    Map<String, Object> getH5GoodsCount();

    Map<String, Object> getBalanceAmount(Map<String, Object> params);

    void backBaseAmount(Long goodsId, String username);
}
