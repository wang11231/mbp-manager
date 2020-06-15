package com.art.manager.service.base;

import com.art.manager.enums.GoodsEnum;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.base.BaseReq;
import com.art.manager.util.RegexUtil;
import com.art.manager.util.SpringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 基础服务类
 */
public class BaseService {
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;
    @Autowired
    private CommonCommodityMapper commonCommodityMapper;

    /**
     * 设置分页信息
     *
     * @param page
     * @param req
     */
    public void setPrePageInfo(Page page, BaseReq req) {
        page.setPageSize(req.getPageSize());
        page.setPageNum(req.getPageNum());
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
    }

    /**
     * 封装分页信息
     *
     * @param list
     * @param page
     * @param <T>
     * @return
     */
    public <T> Map<String, Object> setAfterPageInfo(List<T> list, Page page) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        int pages = Page.getPages(pageInfo.getTotal(), page.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("currentTime", new Date());
        result.put("list", list);
        return result;
    }

    /**
     * 根据token获取用户用户名（手机号）
     *
     * @param request
     * @return
     */
    public String getUsername(HttpServletRequest request, boolean isManagerUser) {
        Object username = request.getAttribute("username");
        if (username == null) {
            throw new AuctionGoodsException("用户名不存在！");
        }
        String phone = (String) username;
        if(isManagerUser){
            SysUser sysUser = (SysUser)username;
            return sysUser.getUsername();
        }
        if (!RegexUtil.isTel(phone)) {
            throw new AuctionGoodsException("手机号不合法！");
        }
        return phone;
    }

    public void setInterestCount(GoodsEnum goodsEnum, Long goodId, String mobile){
        if(goodsEnum == null || goodId == null || StringUtils.isBlank(mobile)){
            return;
        }
        RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String keyPre = null;
        if(GoodsEnum.COMMON.getCode().equals(goodsEnum.getCode())){
            keyPre="ART_COMMOM_GOODS_";
        }else if(GoodsEnum.AUCTION.getCode().equals(goodsEnum.getCode())){
            keyPre="ART_AUCTION_GOODS_";
        }
        if(StringUtils.isBlank(keyPre)){
            return;
        }
        Boolean member = setOperations.isMember(keyPre + goodId, mobile);
        if(!member){
            int count= 0;
            if(GoodsEnum.COMMON.getCode().equals(goodsEnum.getCode())){
                //修改普通商品 关注人数+3
                count = commonCommodityMapper.updateInterestCount(goodId, /*(long)new Random().nextInt(90) +10*/ 3L);
            }else if(GoodsEnum.AUCTION.getCode().equals(goodsEnum.getCode())){
                //修改拍卖商品 关注人数+3
                count = auctionGoodsMapper.updateInterestCount(goodId, /*(long)new Random().nextInt(90) +10*/3L);
            }
            if(count > 0){
                setOperations.add(keyPre + goodId, mobile);
            }
        }
    }


}
