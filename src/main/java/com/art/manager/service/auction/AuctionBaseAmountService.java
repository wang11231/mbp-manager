package com.art.manager.service.auction;

import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.request.AuctionUserBaseAmountReq;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public interface AuctionBaseAmountService {

    boolean insert(HttpServletRequest request, AuctionBaseAmount auctionBaseAmount);

    BigDecimal selectByUserAndGoodId(HttpServletRequest request, AuctionBaseAmount auctionBaseAmount);

    Map<String, Object> getUserBaseAmountList(AuctionUserBaseAmountReq auctionUserBaseAmountReq);

}
