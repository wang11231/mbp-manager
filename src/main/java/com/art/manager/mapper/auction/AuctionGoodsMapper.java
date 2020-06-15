package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.pojo.schedule.AuctionGoodsShedule;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.vo.AuctionGoodsVo;
import com.art.manager.vo.H5AuctionGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AuctionGoodsMapper {

    Long insert(AuctionGoods auctionGoods);

    Long insertById(AuctionGoods auctionGoods);

    int deleteByIds(@Param("ids") List<Long> ids);

    int updateById(AuctionGoods auctionGoods);

    AuctionGoodsVo selectById(AuctionGoodsReq auctionGoodsreq);

    AuctionGoodsVo selectOneById(AuctionGoodsReq auctionGoodsreq);

    List<AuctionGoodsVo> getList(AuctionGoodsReq auctionGoodsreq);

    int updateGoodsStatusToInAction(AuctionGoodsShedule auctionGoodsShedule);

    int batchUpdateGoodsStatusToActionSuccess(@Param("params") List<AuctionGoodsShedule> params);

    List<Long> selectGoodsStatusInAction(AuctionGoodsShedule auctionGoodsShedule);

    List<AuctionGoods> getAuctionGoodList(Map<String, Object> params);

    List<H5AuctionGoodsVo> selectTodayAndTomorrowGoods(Map<String, Date> map);

    List<H5AuctionGoodsVo> selectPreGoods(Map<String, Date> map);

    List<H5AuctionGoodsVo> selectHistoryGoods(Map<String, Object> map);

    AuctionGoods getAuctionGoods(@Param("goodsId") Long goodsId);

    AuctionGoods getAuctionGoodsStatus(@Param("goodsId") Long goodsId);

    Integer todayCount(Map<String, Date> map);

    Integer preCount(Map<String, Date> map);

    Integer historyCount(Map<String, Object> map);

    BigDecimal getBaseAmount(@Param("id") String id);

    int updateGoodsStatusToSale(@Param("goodsId") Long goodsId);

    int batchUpdateAutoProxyToInvalid();

    int updateInterestCount(@Param("id") Long id, @Param("interestCount") Long interestCount);
}
