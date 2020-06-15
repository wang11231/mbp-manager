package com.art.manager.mapper;

import com.art.manager.pojo.Order;
import com.art.manager.request.OrderReq;
import com.art.manager.request.WechatUserReq;
import com.art.manager.vo.OrderVo;
import com.art.manager.vo.WechatOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface OrderMapper {

    List<OrderVo> selectOrderList(OrderReq req);

    Order selectOrderInfo(String orderNo);

    int updateLogisticsById(Map<String, Object> params);

    int updateStatusById(Map<String, Object> params);

    int buyCount(String accountNumber);

    int unpaidCount(String accountNumber);

    List<String> orderNoList(String accountNumber);

    int updateStatusByOrderNo(Order order);

    int inserPtOrderNo(Order order);

    List<Order> getOrderNum(@Param("mobile") String mobile);

    Order getOrder(String commodityName);

    Order getOrderNo(@Param("commodityName") String commodityName,@Param("mobile") String mobile);

    Order getOrderDealPrice(String orderNo);

    List<WechatOrderVo> wechatOrderList(OrderReq req);

    int delOrder(String orderNo);

    int updateOrderStatus(String orderNo);

    Order getBuyOrder(@Param("orderNo") String orderNo, @Param("mobile") String mobile);

    int updateConfirmReceipt();
}
