package com.art.manager.service.impl.auction;

import com.art.manager.enums.GoodsStatusEnum;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.auction.AuctionBaseAmountMapper;
import com.art.manager.mapper.auction.AuctionProcessMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.pojo.auction.AuctionUserBaseAmount;
import com.art.manager.request.AuctionUserBaseAmountReq;
import com.art.manager.service.auction.AuctionBaseAmountService;
import com.art.manager.service.base.BaseService;
import com.art.manager.vo.WeChatUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AuctionBaseAmountServiceImpl extends BaseService implements AuctionBaseAmountService {

    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    @Autowired
    private AuctionProcessMapper auctionProcessMapper;
    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Override
    public boolean insert(HttpServletRequest request, AuctionBaseAmount auctionBaseAmount) {
        String username = getUsername(request, false);
        auctionBaseAmount.setUsername(username);
        if(auctionBaseAmount.getGoodsId() == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        if(auctionBaseAmount.getBaseAmount() == null){
            throw new AuctionGoodsException("保证金为空！");
        }
        int count = auctionBaseAmountMapper.insert(auctionBaseAmount);
        return count > 0 ? true :false;
    }

    @Override
    public BigDecimal selectByUserAndGoodId(HttpServletRequest request, AuctionBaseAmount auctionBaseAmount) {
        String username = getUsername(request, false);
        auctionBaseAmount.setUsername(username);
        if(auctionBaseAmount.getGoodsId() == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        List<AuctionBaseAmount>  list = auctionBaseAmountMapper.selectByUserAndGoodId(auctionBaseAmount);
        return (list==null||list.size()==0) ? new BigDecimal(0) : list.get(0).getBaseAmount();
    }

    @Override
    public Map<String, Object> getUserBaseAmountList(AuctionUserBaseAmountReq auctionUserBaseAmountReq) {
        if(auctionUserBaseAmountReq == null || StringUtils.isBlank(auctionUserBaseAmountReq.getMobile())){
            throw new AuctionGoodsException("用户手机号为空");
        }
        if(StringUtils.isBlank(auctionUserBaseAmountReq.getType())){
            throw new AuctionGoodsException("冻结或历史类型为空");
        }
        Page page = new Page();
        setPrePageInfo(page, auctionUserBaseAmountReq);
        AuctionUserBaseAmount auctionUserBaseAmount = new AuctionUserBaseAmount();
        auctionUserBaseAmount.setUsername(auctionUserBaseAmountReq.getMobile());
        if(auctionUserBaseAmountReq.getType().equals("0")){
            //历史
            auctionUserBaseAmount.setGoodsStatuses(Arrays.asList(GoodsStatusEnum.AUCTION_FAIL.getCode(),GoodsStatusEnum.AUCTION_SALE.getCode()));
        }else{
            //冻结
            auctionUserBaseAmount.setBaseAmountStatus(1);
            auctionUserBaseAmount.setGoodsStatuses(Arrays.asList(GoodsStatusEnum.AUCTION_pre.getCode(),GoodsStatusEnum.AUCTION_IN.getCode(),GoodsStatusEnum.AUCTION_SUCCESS.getCode()));
        }
        List<AuctionUserBaseAmount> userBaseAmountList = auctionBaseAmountMapper.getUserBaseAmountList(auctionUserBaseAmount);
        if(userBaseAmountList != null && userBaseAmountList.size() > 0 && auctionUserBaseAmountReq.getType().equals("0")){
            //历史
            List<AuctionProcess> processes = auctionProcessMapper.selectProxyLimitByUser(auctionUserBaseAmountReq.getMobile());
            if(processes != null && processes.size() > 0){
                for(AuctionUserBaseAmount amount:userBaseAmountList){
                    for(AuctionProcess process:processes){
                        if(amount.getGoodsId().equals(process.getGoodsId())){
                            amount.setPrice(process.getCurrentPrice());
                            amount.setAddPriceType(process.getAddPriceType());
                            break;
                        }
                    }
                }
            }
        }
        Map<String, Object> result = setAfterPageInfo(userBaseAmountList, page);
        WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(auctionUserBaseAmountReq.getMobile(), null);
        result.put("bond",weChatUserVo.getBond()==null?0:weChatUserVo.getBond());
        return result;
    }
}
