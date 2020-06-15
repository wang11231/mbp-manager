package com.art.manager.service.impl;

import com.art.manager.enums.GoodsEnum;
import com.art.manager.enums.OrderNoPrefixEnum;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.OrderMapper;
import com.art.manager.mapper.SpecialMapper;
import com.art.manager.mapper.SysUserMapper;
import com.art.manager.mapper.artist.ArtistInfoMapper;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.auction.AuctionOrderMapper;
import com.art.manager.mapper.config.CategoryConfigMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.*;
import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.request.OrderReq;
import com.art.manager.service.OrderService;
import com.art.manager.util.IdUtils;
import com.art.manager.vo.AuctionGoodsVo;
import com.art.manager.vo.OrderBalanceVo;
import com.art.manager.vo.OrderVo;
import com.art.manager.vo.WechatOrderVo;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CommonCommodityMapper commonCommodityMapper;
    @Autowired
    private ArtistInfoMapper artistInfoMapper;
    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private SpecialMapper specialMapper;
    @Autowired
    private CategoryConfigMapper configMapper;
    @Autowired
    private AuctionOrderMapper auctionOrderMapper;
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Override
    public Map<String, Object> getOrderList(OrderReq orderReq) {
        PageHelper.startPage(orderReq.getPageNum(), orderReq.getPageSize());
        List<OrderVo> orders = orderMapper.selectOrderList(orderReq);
        List<OrderVo> orderList = new ArrayList<>();
        if(orders != null){
            for(OrderVo orderVo : orders){
                if(orderVo.getDelFlag() == 1 && orderVo.getStatus() == 0){
                }else {
                    orderList.add(orderVo);
                }
            }
        }
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orders);
        int pages = Page.getPages(pageInfo.getTotal(), orderReq.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", orderList);
        return result;
    }

    @Override
    public Map<String, Object> getOrderInfo(String orderNo) {
        Map<String, Object> basicInformation = new HashMap<>();
        Map<String, Object> goodInformation = new HashMap<>();
        Map<String, Object> logisticsInformation = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        if(orderNo.substring(0,2).equals(OrderNoPrefixEnum.PT.getCode())){ // 普通订单
            Order order = orderMapper.selectOrderInfo(orderNo);
            basicInformation.put("orderNo", order.getOrderNo());
            basicInformation.put("dealPrice", order.getDealPrice());
            basicInformation.put("orderTime", order.getOrderTime());
            basicInformation.put("accountNumber", order.getAccountNumber());
            basicInformation.put("draweeNumber", order.getDraweeNumber());
            basicInformation.put("status", order.getStatus());
            CommonCommodity commodity = commonCommodityMapper.getCommodityByName(order.getCommodityName());
            goodInformation.put("commodityName", order.getCommodityName());
            goodInformation.put("artist", order.getArtist());
            goodInformation.put("typeName", configMapper.getTypaName(Long.valueOf(commodity.getTypeCode())));
            goodInformation.put("styleName", configMapper.getStyleName(Long.valueOf(commodity.getStyleCode())));
            goodInformation.put("coreSpecification", commodity.getCoreSpecification());
            goodInformation.put("creationYear", commodity.getCreationYear());
            goodInformation.put("discountPrice", commodity.getDiscountPrice() == null ? new BigDecimal(0) : commodity.getDiscountPrice());
            goodInformation.put("markePrice", commodity.getMarkePrice());
            goodInformation.put("freight", commodity.getFreight());
            Special special = specialMapper.getSpecialById(Long.valueOf(order.getSpecialField()));
            goodInformation.put("specialField", special.getName());
            String[] showPictures = {commodity.getShowPicture()};
            goodInformation.put("showPictures", showPictures);
            logisticsInformation.put("transportAddr", order.getTransportAddr());
            logisticsInformation.put("logisticsNo", order.getLogisticsNo());
            logisticsInformation.put("logisticsCompany", order.getLogisticsCompany());
            logisticsInformation.put("cnAddress", order.getCnAddress());
            logisticsInformation.put("detailedAddress", order.getDetailedAddress());
            logisticsInformation.put("receiverName", order.getReceiverName());
            logisticsInformation.put("receiverMobile", order.getReceiverMobile());
        }else if(orderNo.substring(0,2).equals(OrderNoPrefixEnum.PP.getCode())){
            AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
            auctionOrderReq.setOrderNo(orderNo);
            AuctionOrder auctionOrder = auctionOrderMapper.selectByOrderNo(auctionOrderReq);
            basicInformation.put("orderNo", auctionOrder.getOrderNo());
            basicInformation.put("dealPrice", auctionOrder.getDealPrice());
            basicInformation.put("orderTime", auctionOrder.getBuyerDate());
            basicInformation.put("accountNumber", auctionOrder.getBuyerName());
            basicInformation.put("draweeNumber", auctionOrder.getPayerName());
            basicInformation.put("status", auctionOrder.getOrderStatus());
            AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
            auctionGoodsreq.setId(auctionOrder.getGoodsId());
            AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectById(auctionGoodsreq);
            goodInformation.put("commodityName", auctionGoodsVo.getGoodsName());
            goodInformation.put("artist", auctionGoodsVo.getArtistName());
            goodInformation.put("typeName", auctionGoodsVo.getParentName());
            goodInformation.put("styleName", auctionGoodsVo.getCategoryName());
            goodInformation.put("coreSpecification", auctionGoodsVo.getSpecification());
            goodInformation.put("creationYear", auctionGoodsVo.getCreateYear());
            goodInformation.put("discountPrice", new BigDecimal(0));
            BigDecimal transportAmount = auctionGoodsVo.getTransportAmount() == null ? new BigDecimal(0) :auctionGoodsVo.getTransportAmount();
            goodInformation.put("markePrice", auctionOrder.getDealPrice().subtract(transportAmount));
            goodInformation.put("freight", transportAmount);
            goodInformation.put("specialField", null);
            String[] showPictures = {auctionGoodsVo.getShowPic()};
            goodInformation.put("showPictures", showPictures);
            logisticsInformation.put("transportAddr", auctionOrder.getTransportAddr());
            logisticsInformation.put("logisticsNo", auctionOrder.getTransportNo());
            logisticsInformation.put("logisticsCompany", auctionOrder.getTransportCompany());
            logisticsInformation.put("cnAddress", auctionOrder.getReceiveAddr());
            logisticsInformation.put("detailedAddress", auctionOrder.getReceiveAddr());
            logisticsInformation.put("receiverName", auctionOrder.getReceiveContact());
            logisticsInformation.put("receiverMobile", auctionOrder.getReceivePhone());
        }else{
            throw new RuntimeException("订单号不合法！");
        }
        result.put("basicInformation", basicInformation);
        result.put("goodInformation", goodInformation);
        result.put("logisticsInformation", logisticsInformation);
        return result;
    }

    @Transactional
    @Override
    public int updateLogistics(Map<String, Object> params) {
        //String username = sysUserMapper.getUsernameById((Long) params.get("userId"));
        params.put("operator", params.get("username"));
        return orderMapper.updateLogisticsById(params);
    }

    @Transactional
    @Override
    public int updateStatus(Map<String, Object> params) {
        //String username = sysUserMapper.getUsernameById((Long) params.get("userId"));
        params.put("operator", params.get("username"));
        return orderMapper.updateStatusById(params);
    }

    @Override
    public int updateStatusByOrderNo(String orderNo, String mobile) {
        Order order = new Order();
        order.setStatus(1);
        order.setUpdateTime(new Date());
        order.setDealTime(new Date());
        order.setOrderNo(orderNo);
        order.setDraweeNumber(mobile);
        return orderMapper.updateStatusByOrderNo(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg inserPtOrderNo(Map<String, Object> params) {
        //校验请求参数是否为空
        getCheckOrderRequest(params);
        HashMap<String, Object> map = new HashMap<>();
        Order order = new Order();
        //下单人手机号
        String mobile = (String) params.get("mobile");
        order.setAccountNumber(mobile);
        String id = (String) params.get("id");
        CommonCommodity commonCommodity = commonCommodityMapper.selectCommonCommodity(id);
        Order orderNo1 = orderMapper.getOrderNo(commonCommodity.getCommdityName(), mobile);
        if(null != orderNo1){
            map.put("orderNo",orderNo1.getOrderNo());
            return new Msg(Msg.FAILURE_CODE, "您已下单，请勿重复下单",map);
        }
        //折扣价格
        String discountPrice = (String) params.get("discountPrice");
        //运费
        BigDecimal freight = commonCommodity.getFreight();
        //折扣价格
        BigDecimal discountPiceDeal = new BigDecimal(discountPrice);
        //成交价格
        BigDecimal dealPic = discountPiceDeal.add(freight).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.info("dealPic====>" + dealPic);
        String dealPicString = dealPic.toString();
        //String dealPrice = (String) params.get("dealPrice");
        BigDecimal dealPrice = new BigDecimal(String.valueOf(params.get("dealPrice"))).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.info("dealPrice==>" + dealPrice);
        //校验成交价格是否一样
        if(dealPic.equals(dealPrice)){
            order.setDealPrice(dealPic);
        }else {
            return new Msg(Msg.FAILURE_CODE, "下单失败,订单价格有误，请重新购买");
        }
        //生成订单
        String orderNo = IdUtils.generate(GoodsEnum.COMMON, OrderNoPrefixEnum.PT);
        //订单号
        order.setOrderNo(orderNo);
        //商品名称
        order.setCommodityName((String) params.get("commdityName"));
        //艺术家名称
        order.setArtist((String) params.get("artistName"));

        order.setTypeName(commonCommodity.getTypeName());
        //路名门牌地址
        order.setDetailedAddress((String) params.get("detailedAddress"));
        //收货人手机号
        order.setReceiverMobile((String) params.get("receiverMobile"));
        //收货人姓名
        order.setReceiverName((String) params.get("receiverName"));
        order.setStyleName(commonCommodity.getStyleName());
        //物流地址
        order.setLogisticsAddress((String) params.get("logisticsAddress"));
        //专场ID
        order.setSpecialField((String) params.get("specialField"));
        order.setCnAddress((String) params.get("cnAddress")); // 省市区中文地址
        //订单状态（默认未付款）
        order.setStatus(0);
        order.setOrderTime(new Date());
        order.setDealTime(new Date());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        int i = orderMapper.inserPtOrderNo(order);
        if(i > 0 ){
            map.put("orderNo",orderNo);
            return new Msg(Msg.SUCCESS_CODE, map);
        }
        return new Msg(Msg.FAILURE_CODE, "下单失败");
    }

    @Override
    public OrderBalanceVo getOrderBalance(Map<String, Object> params) {
        Object orderNoReq = params.get("orderNo");
        if(orderNoReq == null){
            throw new RuntimeException("订单号为空");
        }
        Object mobileReq = params.get("mobile");
        if(mobileReq == null){
            throw new RuntimeException("手机号为空");
        }
        String orderNo = String.valueOf(orderNoReq);
        String mobile = String.valueOf(mobileReq);
        OrderBalanceVo orderBalanceVo = new OrderBalanceVo();
        /**
         * 查询用户余额
         */
        String balance = weChatUserMapper.getBalance(mobile);
        BigDecimal bigDecimal = new BigDecimal(balance);
        orderBalanceVo.setBalance(bigDecimal);
        if(orderNo.startsWith(OrderNoPrefixEnum.PT.getCode())){
            Order order = orderMapper.getOrderDealPrice(orderNo);
            Special special = specialMapper.getSpecialById(Long.valueOf(order.getSpecialField()));
            BigDecimal dealPrice = order.getDealPrice();
            Order buyOrder = orderMapper.getBuyOrder(orderNo, mobile);
            if(buyOrder == null){
                orderBalanceVo.setSuccess(false);
            } else {
                orderBalanceVo.setSuccess(true);
            }
            orderBalanceVo.setSpecialName(special.getSpecialName());
            orderBalanceVo.setDealPrice(dealPrice);
        }else if(orderNo.startsWith(OrderNoPrefixEnum.PP.getCode())){
            AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
            auctionOrderReq.setOrderNo(orderNo);
            AuctionOrder auctionOrder = auctionOrderMapper.selectByOrderNo(auctionOrderReq);
            orderBalanceVo.setSuccess(false);
            if(auctionOrder != null){
                orderBalanceVo.setDealPrice(auctionOrder.getDealPrice());
                if(mobile.equals(auctionOrder.getPayerName())){
                    orderBalanceVo.setSuccess(true);
                }
            }
        }else{
            throw new RuntimeException("订单号不合法！");
        }
        return orderBalanceVo;
    }

    @Override
    public Map<String, Object> wechatOrderList(OrderReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<WechatOrderVo> orders = orderMapper.wechatOrderList(req);
        for(WechatOrderVo wechatOrderVo : orders){
            String[] pictureUrl = {wechatOrderVo.getShowPicture()};
            wechatOrderVo.setShowPictures(pictureUrl);
        }
        PageInfo<WechatOrderVo> pageInfo = new PageInfo<>(orders);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", orders);
        return result;
    }

    @Transactional
    @Override
    public int delOrder(OrderReq req) {
        return orderMapper.delOrder(req.getOrderNo());
    }

    @Override
    public int updateOrderStatus(OrderReq req) {
        String orderNo = req.getOrderNo();
        if(StringUtils.isBlank(orderNo)){
            throw new RuntimeException("订单号为空");
        }
        if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PT.getCode())){ // 普通订单
            return orderMapper.updateOrderStatus(orderNo);
        }else if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PP.getCode())){
            return auctionOrderMapper.updateOrderToConfirmReceive(orderNo);
        }
        return 0;
    }

    private Msg getCheckOrderRequest(Map<String, Object> params){
        String mobile = (String) params.get("mobile");
        String discountPrice = (String) params.get("discountPrice");
        String dealPrice = (String) params.get("dealPrice");
        String commdityName= (String) params.get("commdityName");
        String artistName= (String) params.get("artistName");
        String detailedAddress= (String) params.get("detailedAddress");
        String receiverMobile= (String) params.get("receiverMobile");
        String receiverName= (String) params.get("receiverName");
        String logisticsAddress= (String) params.get("logisticsAddress");
        String specialField= (String) params.get("specialField");
        if(StringUtils.isBlank(mobile)){
            return new Msg(Msg.FAILURE_CODE, "下单人手机号不能为空");
        }
        if(StringUtils.isBlank(discountPrice)){
            return new Msg(Msg.FAILURE_CODE, "商品折后价格不能为空");
        }
        if(StringUtils.isBlank(dealPrice)){
            return new Msg(Msg.FAILURE_CODE, "商品成交价格不能为空");
        }
        if(StringUtils.isBlank(commdityName)){
            return new Msg(Msg.FAILURE_CODE, "商品名称不能为空不能为空");
        }
        if(StringUtils.isBlank(artistName)){
            return new Msg(Msg.FAILURE_CODE, "艺术家名称不能为空");
        }
        if(StringUtils.isBlank(detailedAddress)){
            return new Msg(Msg.FAILURE_CODE, "路名门牌地址不能为空");
        }
        if(StringUtils.isBlank(receiverMobile)){
            return new Msg(Msg.FAILURE_CODE, "收货人手机号不能为空");
        }
        if(StringUtils.isBlank(receiverName)){
            return new Msg(Msg.FAILURE_CODE, "收货人姓名不能为空");
        }
        if(StringUtils.isBlank(logisticsAddress)){
            return new Msg(Msg.FAILURE_CODE, "收货地址不能为空");
        }
        if(StringUtils.isBlank(specialField)){
            return new Msg(Msg.FAILURE_CODE, "专场ID不能为空");
        }
        return null;
    }
}
