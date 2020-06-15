package com.art.manager.service;

import com.art.manager.pojo.Msg;
import com.art.manager.request.OrderReq;
import com.art.manager.vo.OrderBalanceVo;

import java.util.Map;

public interface OrderService {

    Map<String, Object> getOrderList(OrderReq orderReq);

    Map<String, Object> getOrderInfo(String orderNo);

    int updateLogistics(Map<String, Object> params);

    int updateStatus(Map<String, Object> params);

    int updateStatusByOrderNo(String orderNo, String mobile);


    Msg inserPtOrderNo(Map<String, Object> params);

    OrderBalanceVo getOrderBalance(Map<String, Object> params);

    Map<String, Object> wechatOrderList(OrderReq req);

    int delOrder(OrderReq req);

    int updateOrderStatus(OrderReq req);
}
