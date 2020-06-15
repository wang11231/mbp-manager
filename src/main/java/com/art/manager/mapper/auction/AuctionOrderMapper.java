package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.request.AuctionOrderReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AuctionOrderMapper {

    int insert(AuctionOrder auctionOrder);

    int insertByProcess(AuctionOrderReq auctionOrderReq);

    int insertByProxy(AuctionOrder AuctionOrder);

    int insertOnePrice(AuctionOrderReq auctionOrderReq);

    int insertImmediatelyPrice(AuctionOrderReq auctionOrderReq);

    int updateByOrderNo(AuctionOrder auctionOrder);

    List<AuctionOrder> getList(AuctionOrderReq auctionOrderReq);

    AuctionOrder selectByOrderNo(AuctionOrderReq auctionOrderReq);

    List<AuctionOrder> selectByGoodsId(AuctionOrderReq auctionOrderReq);

    int updateOrderStatusByOrderNo(AuctionOrderReq auctionOrderReq);

    int delayExpireTime(AuctionOrderReq req);

    int buyCount(String buyerName);

    int unpaidCount(String buyerName);

    List<String> orderNoList(String buyerName);

    int updateStatusByOrderNo(AuctionOrder order);

    int updateStatusToSendGoods(AuctionOrderReq order);

    List<AuctionOrder> getAuctionOrderList(String buyerName);

    int updateReceiveInfo(@Param("id") Long id);

    int updateReceiveInfoByProcess(@Param("id") Long id);

    AuctionOrder getOrderStatus(@Param("goodsId") Long goodsId);

    AuctionOrder getOrderMobile(@Param("goodsId") Long goodsId, @Param("mobile") String mobile);

    List<AuctionOrder> getAuctionOrderMobileList(@Param("mobile") String mobile,@Param("type") String type);

    int updateAuctionOrderStatus(@Param("orderNo") String orderNo, @Param("goodsId") String goodsId);
    List<AuctionOrder> selectAllExpiredOrder();

    int batchUpdateExpiredOrder(@Param("ids") List ids);

    int updateOrderToConfirmReceive(@Param("orderNo") String orderNo);

    int updateConfirmReceipt();

}
