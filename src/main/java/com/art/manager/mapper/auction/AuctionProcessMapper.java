package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.request.AuctionProcessReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AuctionProcessMapper {

    int insert(AuctionProcess auctionProcess);

    int batchInsert(@Param("list")List<AuctionProcess> list);

    List<AuctionProcess> getList(AuctionProcessReq auctionProcessReq);

    int updateStatusByGoodsId(AuctionProcess auctionProcess);

    List<Long> selectProcessByIds(@Param("ids") List ids);

    List<AuctionProcess> selectByIds(@Param("ids") List ids);

    List<AuctionProcess> selectByUsername(@Param("username") String username);

    List<AuctionProcess> selectProxyLimitByUser(@Param("username") String username);

    AuctionProcess  getCurrentPrice(@Param("goodsId") Long goodsId);

    int batchUpdateProxyStatus(@Param("goodsId") Long goodsId);

    List<AuctionProcess> getAlreadyWentUnsold(@Param("mobile") String mobile);

    AuctionProcess selectUserGoodsPrice(AuctionProcess auctionProcess);
}
