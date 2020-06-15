package com.art.manager.service.auction;

import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.request.AuctionOrderReq;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface AuctionProxyLimitService {

    public boolean insertProxyLimit(AuctionOrderReq auctionOrderReq);

    boolean insert(HttpServletRequest request, AuctionProxyLimit auctionProxyLimit);

    BigDecimal selectByUserAndGoodId(HttpServletRequest request, AuctionProxyLimit auctionProxyLimit);

    List<AuctionProxyLimit> selectProxyStatusValid(Long GoodId);
}
