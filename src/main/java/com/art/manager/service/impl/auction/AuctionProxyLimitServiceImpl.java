package com.art.manager.service.impl.auction;

import com.art.manager.enums.StatusEnum;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.auction.AuctionProxyLimitMapper;
import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.service.auction.AuctionProxyLimitService;
import com.art.manager.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AuctionProxyLimitServiceImpl extends BaseService implements AuctionProxyLimitService {

    @Autowired
    private AuctionProxyLimitMapper auctionProxyLimitMapper;

    @Override
    public boolean insertProxyLimit(AuctionOrderReq auctionOrderReq) {
        AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
        auctionProxyLimit.setGoodsId(auctionOrderReq.getGoodsId());
        auctionProxyLimit.setUsername(auctionOrderReq.getUsername());
        auctionProxyLimit.setPriceLimit(auctionOrderReq.getCurrentPrice());
        auctionProxyLimit.setAddrId(auctionOrderReq.getAddrId());
        AuctionProxyLimit result = auctionProxyLimitMapper.selectOneByUserAndGoodId(auctionProxyLimit);
        if(result != null){
            //之前设置过代理出价要更新
            auctionProxyLimit.setCurrentTime(new Date());
            int innerCount = auctionProxyLimitMapper.updateProxyLimitByUserAndGoodId(auctionProxyLimit);
            return innerCount > 0 ? true :false;
        }
        int count = auctionProxyLimitMapper.insert(auctionProxyLimit);
        return count > 0 ? true :false;
    }

    @Override
    public boolean insert(HttpServletRequest request, AuctionProxyLimit auctionProxyLimit) {
        String username = getUsername(request, false);
        //String username = "18662566803";
        auctionProxyLimit.setUsername(username);
        if(auctionProxyLimit.getGoodsId() == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        if(auctionProxyLimit.getPriceLimit() == null){
            throw new AuctionGoodsException("代理出价上限为空！");
        }
        if(auctionProxyLimit.getAddrId() == null){
            throw new AuctionGoodsException("收货地址id为空！");
        }
        AuctionProxyLimit result = auctionProxyLimitMapper.selectOneByUserAndGoodId(auctionProxyLimit);
        if(result != null){
            //设置代理出价要高于之前
            if(result.getPriceLimit().compareTo(auctionProxyLimit.getPriceLimit()) >= 0){
                throw new AuctionGoodsException("请设置高于:" + result.getPriceLimit() + " 代理出价上限!");
            }
            //代理出价批处理已执行
            if(StatusEnum.INVALID.getCode().equals(result.getProxyStatus())){
                throw new AuctionGoodsException("该商品代理出价自动批处理已执行!");
            }

            //之前设置过代理出价要更新
            if(StatusEnum.VALID.getCode().equals(result.getProxyStatus())){
                auctionProxyLimit.setCurrentTime(new Date());
                int innerCount = auctionProxyLimitMapper.updateProxyLimitByUserAndGoodId(auctionProxyLimit);
                return innerCount > 0 ? true :false;
            }
        }
        int count = auctionProxyLimitMapper.insert(auctionProxyLimit);
        return count > 0 ? true :false;
    }

    @Override
    public BigDecimal selectByUserAndGoodId(HttpServletRequest request, AuctionProxyLimit auctionProxyLimit) {
        String username = getUsername(request, false);
        auctionProxyLimit.setUsername(username);
        if(auctionProxyLimit.getGoodsId() == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        AuctionProxyLimit result = auctionProxyLimitMapper.selectOneByUserAndGoodId(auctionProxyLimit);
        return result == null ? new BigDecimal(0) : result.getPriceLimit();
    }

    @Override
    public List<AuctionProxyLimit> selectProxyStatusValid(Long GoodId) {
        AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
        auctionProxyLimit.setCurrentTime(new Date());
        auctionProxyLimit.setGoodsId(GoodId);
        return auctionProxyLimitMapper.selectProxyStatusValid(auctionProxyLimit);
    }
}
