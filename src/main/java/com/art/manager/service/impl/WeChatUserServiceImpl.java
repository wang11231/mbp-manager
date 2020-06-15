package com.art.manager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.art.manager.dto.AuctionStartPriceDto;
import com.art.manager.dto.WechatUserDto;
import com.art.manager.mapper.OrderMapper;
import com.art.manager.mapper.auction.AuctionBaseAmountMapper;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.auction.AuctionOrderMapper;
import com.art.manager.mapper.auction.AuctionProcessMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Order;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.request.WechatUserReq;
import com.art.manager.service.WeChatUserService;
import com.art.manager.vo.WeChatUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class WeChatUserServiceImpl implements WeChatUserService {

    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AuctionOrderMapper auctionOrderMapper;
    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;
    @Autowired
    private AuctionProcessMapper auctionProcessMapper;

    @Override
    public int inserWeChatUser(JSONObject wechatJson) {
        WeChatUser weChatUser = new WeChatUser();
        weChatUser.setMobile(wechatJson.getString("mobile"));
        weChatUser.setOpenid(wechatJson.getString("openid"));
        weChatUser.setNickname(wechatJson.getString("nickname"));
        weChatUser.setSex(Integer.valueOf(wechatJson.getString("sex")));
        weChatUser.setLanguage(wechatJson.getString("language"));
        weChatUser.setCity(wechatJson.getString("city"));
        weChatUser.setProvince(wechatJson.getString("province"));
        weChatUser.setHeadimgurl(wechatJson.getString("headimgurl"));
        weChatUser.setPrivileges(wechatJson.getString("privileges"));
        weChatUser.setUnionid(wechatJson.getString("unionid"));
        weChatUser.setCountry(wechatJson.getString("country"));
        weChatUser.setCreateTime(new Date());
        weChatUser.setUpdateTime(new Date());
        weChatUser.setLastLoginTime(new Date());
        return weChatUserMapper.saveWechatUser(weChatUser);
    }

    @Override
    public int updateWeChatUser(JSONObject wechatJson) {
        WeChatUser weChatUser = new WeChatUser();
        weChatUser.setMobile(wechatJson.getString("mobile"));
        weChatUser.setOpenid(wechatJson.getString("openid"));
        weChatUser.setNickname(wechatJson.getString("nickname"));
        weChatUser.setSex(Integer.valueOf(wechatJson.getString("sex")));
        weChatUser.setLanguage(wechatJson.getString("language"));
        weChatUser.setCity(wechatJson.getString("city"));
        weChatUser.setProvince(wechatJson.getString("province"));
        weChatUser.setHeadimgurl(wechatJson.getString("headimgurl"));
        weChatUser.setPrivileges(wechatJson.getString("privileges"));
        weChatUser.setUnionid(wechatJson.getString("unionid"));
        weChatUser.setCountry(wechatJson.getString("country"));
        weChatUser.setUpdateTime(new Date());
        weChatUser.setLastLoginTime(new Date());
        return weChatUserMapper.updateByopenid(weChatUser);
    }

    @Override
    public int updateWeChatUserByMobile(JSONObject wechatJson) {
        WeChatUser weChatUser = new WeChatUser();
        weChatUser.setMobile(wechatJson.getString("mobile"));
        weChatUser.setOpenid(wechatJson.getString("openid"));
        weChatUser.setNickname(wechatJson.getString("nickname"));
        weChatUser.setSex(Integer.valueOf(wechatJson.getString("sex")));
        weChatUser.setLanguage(wechatJson.getString("language"));
        weChatUser.setCity(wechatJson.getString("city"));
        weChatUser.setProvince(wechatJson.getString("province"));
        weChatUser.setHeadimgurl(wechatJson.getString("headimgurl"));
        weChatUser.setPrivileges(wechatJson.getString("privileges"));
        weChatUser.setUnionid(wechatJson.getString("unionid"));
        weChatUser.setCountry(wechatJson.getString("country"));
        weChatUser.setUpdateTime(new Date());
        weChatUser.setLastLoginTime(new Date());
        return weChatUserMapper.updateByMobile(weChatUser);
    }

    @Transactional
    @Override
    public int saveByMobile(WeChatUser weChatUser) {
        weChatUser.setUpdateTime(new Date());
        weChatUser.setCreateTime(new Date());
        weChatUser.setLastLoginTime(new Date());
        weChatUser.setStatus("1"); // 1:直接登录
        return weChatUserMapper.saveWechatUser(weChatUser);
    }

    @Override
    public WeChatUserVo isRegister(String mobile, String openid) {
        return weChatUserMapper.selectWechatUser(mobile, openid);
    }

    @Override
    public int updateOpenid(WeChatUser weChatUser) {
        weChatUser.setUpdateTime(new Date());
        return weChatUserMapper.updateOpenid(weChatUser);
    }

    @Override
    public int updateMobile(WeChatUser weChatUser) {
        weChatUser.setUpdateTime(new Date());
        return weChatUserMapper.updateMobile(weChatUser);
    }

    @Override
    public Msg wechatUserList(WechatUserReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<WechatUserDto> wechatUserList = weChatUserMapper.getWechatUserList(req);

        if (wechatUserList != null && wechatUserList.size() > 0) {
            for (WechatUserDto wechatUserDto : wechatUserList) {
                /*int buyCount1 = auctionOrderMapper.buyCount(wechatUserDto.getMobile());
                int unpaidCount1 = auctionOrderMapper.unpaidCount(wechatUserDto.getMobile());*/
                int buyCount = orderMapper.buyCount(wechatUserDto.getMobile());
                int unpaidCount = orderMapper.unpaidCount(wechatUserDto.getMobile());
                wechatUserDto.setBuyNum(buyCount);
                wechatUserDto.setUnpaidNum(unpaidCount);
                if (StringUtils.isBlank(wechatUserDto.getNickname())) {
                    wechatUserDto.setNickname(wechatUserDto.getMobile());
                }
            }
        }
        PageInfo<WechatUserDto> pageInfo = new PageInfo<>(wechatUserList);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", wechatUserList);
        return new Msg(Msg.SUCCESS_CODE, result);
    }

    @Override
    public Msg orderNoList(WechatUserReq req) {
        List<String> list = orderMapper.orderNoList(req.getMobile());
        List<String> orderNoList = auctionOrderMapper.orderNoList(req.getMobile());
        list.addAll(orderNoList);
        return new Msg(Msg.SUCCESS_CODE, list);
    }

    @Override
    public Msg orderList(WechatUserReq req) {
        // 普通商品 0:代付款 1:代发货 2：已发货 3：待收货'
        int ptUnpaidNum = 0;  // 普通商品代付款
        int ptUnshippeNum = 0; // 普通商品代发货
        int ptShippedNum = 0; //普通商品已发货
        int ptUnreceivedNum = 0; // 普通商品待收货

       /* //拍品状态 1:待支付，2:待发货，3:待收货，4:已收货
        int auctionUnpaidNum = 0;  // 拍品商品代付款
        int auctionUnshippeNum = 0; // 拍品商品代发货
        int auctionShippedNum = 0; //拍品商品已发货
        int auctionUnreceivedNum = 0; // 拍品商品待收货*/
        List<Order> orderList = orderMapper.getOrderNum(req.getMobile());
        if (orderList != null && orderList.size() > 0) {
            for (Order order : orderList) {
                if (order.getStatus() == 0) {
                    ptUnpaidNum++;
                } else if (order.getStatus() == 1) {
                    ptUnshippeNum++;
                } else if (order.getStatus() == 2) {
                    ptShippedNum++;
                } else if (order.getStatus() == 3) {
                    ptUnreceivedNum++;
                }
            }
        }
       /* //拍品状态 1:待支付，2:待发货，3:待收货，4:已收货
        List<AuctionOrder> auctionOrderList = auctionOrderMapper.getAuctionOrderList(req.getMobile());
        if(auctionOrderList != null && auctionOrderList.size() > 0){
            for (AuctionOrder auctionOrder : auctionOrderList) {
                if(auctionOrder.getOrderStatus().equals("1")){
                    auctionUnpaidNum++;
                } else if(auctionOrder.getOrderStatus().equals("2")){
                    auctionUnshippeNum++;
                } else if(auctionOrder.getOrderStatus().equals("3")){
                    auctionShippedNum++;
                } else if(auctionOrder.getOrderStatus().equals("4")){
                    auctionUnreceivedNum++;
                }
            }
        }*/

        Map<String, Object> map = new HashMap<>();
        map.put("unpaidNum", ptUnpaidNum);
        map.put("unshippeNum", ptUnshippeNum);
        map.put("shippedNum", ptShippedNum);
        map.put("unreceivedNum", ptUnreceivedNum);
        map.put("allNum", orderList.size());
        //map.put("ppOrderNum", auctionOrderList.size());
        return new Msg(Msg.SUCCESS_CODE, map);
    }

    @Override
    public Msg getUserBalance(Map<String, Object> params) {
        Msg msg = new Msg();
        String openid = (String) params.get("openid");
        if (StringUtils.isNotBlank(openid)) {
            String balance = weChatUserMapper.selectBalance(openid);
            msg.setData(balance);
        } else {
            return new Msg(Msg.FAILURE_CODE, "opendId不能为空");
        }

        return msg;
    }

    @Override
    public Map<String, Object> getAuctionStartPriceList(Map<String, Object> params) {
        String mobile = (String) params.get("mobile");
        Object pageSize1 = params.get("pageSize");
        int pageSize = Integer.parseInt(pageSize1.toString());
        List<AuctionBaseAmount> list = auctionBaseAmountMapper.getAuctionStartPriceList(mobile);
        ArrayList<AuctionStartPriceDto> auctionStartPriceDtos = new ArrayList<>();
        if (list.size() > 0) {
            for (AuctionBaseAmount auctionBaseAmount : list) {
                AuctionStartPriceDto auctionStartPriceDto = new AuctionStartPriceDto();
                AuctionGoods auctionGoods = auctionGoodsMapper.getAuctionGoodsStatus(auctionBaseAmount.getGoodsId());
                AuctionOrder orderStatus = auctionOrderMapper.getOrderStatus(auctionBaseAmount.getGoodsId());
             //   AuctionProcess auctionProcess = auctionProcessMapper.getCurrentPrice(auctionBaseAmount.getGoodsId());
                if (auctionGoods.getGoodsStatus().equals("4") && orderStatus.getBuyerId().equals(mobile)) {
                    auctionStartPriceDto.setStatus("1");

                } else if (auctionGoods.getGoodsStatus().equals("0")) {
                    auctionStartPriceDto.setStatus("1");
                }
           /* if(auctionGoods.getGoodsStatus().equals("3") && orderStatus.equals("1")){
                Date expireDate = orderStatus.getExpireDate();
                boolean after = expireDate.after(new Date());
                if(!after){
                    //状态 1成功返回 2 扣除 3 冻结
                    auctionStartPriceDto.setStatus("2");
                }
            }else {
                auctionStartPriceDto.setStatus("1");
            }*/
                auctionStartPriceDtos.add(auctionStartPriceDto);
            }
        }

        PageInfo<AuctionStartPriceDto> pageInfo = new PageInfo<>(auctionStartPriceDtos);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", auctionStartPriceDtos);
        return result;
    }

    @Override
    public Map<String, Object> getUserPaseAmountList(Map<String, Object> params) {
        String mobile = (String) params.get("mobile");
        Object pageSize1 = params.get("pageSize");
        int pageSize = Integer.parseInt(pageSize1.toString());
        List<AuctionBaseAmount> list = auctionBaseAmountMapper.getAuctionStartPriceList(mobile);
        ArrayList<AuctionStartPriceDto> auctionStartPriceDtos = new ArrayList<>();
        Integer integer = 0;
        if (list.size() > 0) {
            for (AuctionBaseAmount auctionBaseAmount : list) {
                AuctionStartPriceDto auctionStartPriceDto = new AuctionStartPriceDto();
                //当前用户，当前拍品数据
                AuctionGoods auctionGoods = auctionGoodsMapper.getAuctionGoods(auctionBaseAmount.getGoodsId());
                //当前用户，当前订单数据
                AuctionOrder orderStatus = auctionOrderMapper.getOrderStatus(auctionBaseAmount.getGoodsId());
                //当前用户，当前竞拍流程数据
                // AuctionProcess auctionProcess = auctionProcessMapper.getCurrentPrice(auctionBaseAmount.getGoodsId());
                //1.用户支付过保证金，没有竞拍成功  拍品状态 4: 已出售 0：流拍
                //状态 1成功返回 2 扣除 3 冻结
                if (auctionGoods.getGoodsStatus().equals("3") && orderStatus.equals("1")) {
                    Date expireDate = orderStatus.getExpireDate();
                    boolean after = expireDate.after(new Date());
                    if (after) {
                        auctionStartPriceDto.setStatus("3");

                    } else {
                        //用户支付过保证金，竞拍成功 支付成功，成功返回状态
                        auctionStartPriceDto.setStatus("1");

                    }
                    //用户支付过保证金，拍品未开始，拍卖中  冻结状态
                } else if (auctionGoods.getGoodsStatus().equals("1")) {
                    auctionStartPriceDto.setStatus("3");

                } else if (auctionGoods.getGoodsStatus().equals("2")) {
                    auctionStartPriceDto.setStatus("3");

                }
                auctionStartPriceDtos.add(auctionStartPriceDto);
            }
        }
        PageInfo<AuctionStartPriceDto> pageInfo = new PageInfo<>(auctionStartPriceDtos);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        BigDecimal bigDecimal = new BigDecimal(integer);
        result.put("baseAmountSum", bigDecimal);
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", auctionStartPriceDtos);
        return result;
    }
}
