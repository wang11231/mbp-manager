package com.art.manager.thread;

import com.art.manager.enums.PayTypeEnum;
import com.art.manager.mapper.auction.AuctionBaseAmountMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.service.JSAPIPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@Async
public class SyncNotifyService {
    @Autowired
    private JSAPIPayService jSAPIPayService;

    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    public void updateBone(String mobile, String total_fee, Long id){
        try {
            BigDecimal bond = new BigDecimal(weChatUserMapper.getBond(mobile)).add(new BigDecimal(total_fee).divide(new BigDecimal(100)));
            weChatUserMapper.updateBondByMobile(mobile, bond);
            AuctionBaseAmount auctionBaseAmount = new AuctionBaseAmount();
            auctionBaseAmount.setBaseAmount(new BigDecimal(total_fee).divide(new BigDecimal(100)));
            auctionBaseAmount.setGoodsId(id);
            auctionBaseAmount.setUsername(mobile);
            auctionBaseAmount.setBaseAmountStatus(1);
            auctionBaseAmount.setPayType(PayTypeEnum.WX.getCode());
            jSAPIPayService.insertBaseAmount(auctionBaseAmount);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public void updateBoneStatus(Long id, String mobile){
        try {
            AuctionBaseAmount auctionBaseAmount = new AuctionBaseAmount();
            auctionBaseAmount.setGoodsId(id);
            auctionBaseAmount.setBaseAmountStatus(0);
            auctionBaseAmount.setUpdateTime(new Date());
            auctionBaseAmount.setUsername(mobile);
            auctionBaseAmountMapper.updateBoneStatus(auctionBaseAmount);
            AuctionBaseAmount bone = auctionBaseAmountMapper.getBone(auctionBaseAmount);
            BigDecimal baseAmount = bone.getBaseAmount();
            BigDecimal balance = new BigDecimal(weChatUserMapper.getBalance(mobile)).add(baseAmount);
            weChatUserMapper.updateBoneAndBalanceByMobile(mobile,baseAmount, balance);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
