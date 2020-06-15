package com.art.manager.mapper.auction;

import com.art.manager.pojo.auction.AuctionProxyLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface AuctionProxyLimitMapper {

    int insert(AuctionProxyLimit auctionProxyLimit);

    AuctionProxyLimit selectOneByUserAndGoodId(AuctionProxyLimit auctionProxyLimit);

    List<AuctionProxyLimit> selectProxyStatusValid(AuctionProxyLimit auctionProxyLimit);

    int batchUpdateProxyStatus(@Param("list") List<AuctionProxyLimit> list);

    int batchUpdateProxyStatusToValid(AuctionProxyLimit auctionProxyLimit);

    int delayEndDateByGoodsId(AuctionProxyLimit auctionProxyLimit);

    int updateProxyLimitByUserAndGoodId(AuctionProxyLimit auctionProxyLimit);

    AuctionProxyLimit selectOneProxylimitByGoondId(AuctionProxyLimit auctionProxyLimit);

    int updateProxyLimitStartAndEndDate(AuctionProxyLimit auctionProxyLimit);

}
