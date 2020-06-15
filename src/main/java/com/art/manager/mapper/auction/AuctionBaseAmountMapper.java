package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.auction.AuctionUserBaseAmount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AuctionBaseAmountMapper {

    int insert(AuctionBaseAmount auctionBaseAmount);

    List<AuctionBaseAmount> selectByUserAndGoodId(AuctionBaseAmount auctionBaseAmount);

    List<AuctionBaseAmount> selectByUserAndGoodIdWithoutBaseAmountStatus(AuctionBaseAmount auctionBaseAmount);

    List<AuctionBaseAmount>  getAuctionStartPriceList(@Param("mobile") String mobile);

    int updateBoneStatus(AuctionBaseAmount auctionBaseAmount);

    int updateBoneStatusWithoutSuccess(AuctionBaseAmount auctionBaseAmount);

    int updateBaseAmountByUserAndGoodId(AuctionBaseAmount auctionBaseAmount);

    AuctionBaseAmount getBone(AuctionBaseAmount auctionBaseAmount);

    List<AuctionUserBaseAmount> getUserBaseAmountList(AuctionUserBaseAmount auctionUserBaseAmount);

    int updateBaseAmountStatusToDeduct(Map params);

}
