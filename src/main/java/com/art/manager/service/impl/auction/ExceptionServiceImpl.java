package com.art.manager.service.impl.auction;

import com.art.manager.exception.CustomerException;
import com.art.manager.exception.CustomerRuntimeException;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.auction.AuctionOrderMapper;
import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.service.impl.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Author: xieyongshan
 * @Date: 2019/10/25 13:49
 * @Description: todo
 */
@Service
public class ExceptionServiceImpl implements ExceptionService {
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Autowired
    private AuctionOrderMapper auctionOrderMapper;


    @Override
    @Transactional(rollbackFor = CustomerRuntimeException.class)
    public void execute() {
        AuctionGoods auctionGoods = new AuctionGoods();
        auctionGoods.setGoodsName("事务测试商品");
        auctionGoodsMapper.insert(auctionGoods);

        AuctionOrder auctionOrder = new AuctionOrder();
        auctionOrder.setGoodsName("事务测试订单");
        auctionOrderMapper.insert(auctionOrder);
        int num= 100/0;
    }

    public static void main(String[] args) {

        BigDecimal a = new BigDecimal(100.20);
        System.out.println(String.valueOf(a.add(new BigDecimal(21.65)).setScale(0,BigDecimal.ROUND_DOWN)));


    }

}
