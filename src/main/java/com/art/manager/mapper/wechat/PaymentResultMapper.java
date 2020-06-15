package com.art.manager.mapper.wechat;

import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.wechat.PaymentResult;
import com.art.manager.request.PaymentResultReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PaymentResultMapper {

    int savePaymentResult(PaymentResult paymentResult);

    int updatePaymentResult(Map<String, String> params);

    List<PaymentResult> selectPayResult(PaymentResultReq req);

    int insertByBaseAmount(AuctionBaseAmount auctionBaseAmount);

    PaymentResult selectPayResultByOrderNo(String orderNo);

    PaymentResult selectPayResultByTransactionId(String transactionId);

    int updatePaymentResultByTransactionId(@Param("refundStatus") Integer refundStatus, @Param("transactionId") String transactionId);
}
