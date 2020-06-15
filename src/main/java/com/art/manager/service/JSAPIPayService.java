package com.art.manager.service;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.request.PaymentResultReq;
import com.art.manager.request.WechatUserReq;

import java.util.Map;

public interface JSAPIPayService {

    Msg pay(WechatUserReq wechatUserReq) throws Exception;

    Msg refund(WechatUserReq req) throws Exception;

    int savePayment(Map<String, String> resultMap);

    Map<String, Object> getTransactionDetails(PaymentResultReq req);

    Msg balancePay(WechatUserReq req);

    Msg recharge(WechatUserReq wechatUserReq) throws Exception;

    Msg bondPay(WechatUserReq wechatUserReq) throws Exception;

    Msg balancePayBond(WechatUserReq req);

    void insertBaseAmount(AuctionBaseAmount auctionBaseAmount);

    Msg transfers(WechatUserReq req) throws Exception;

}
