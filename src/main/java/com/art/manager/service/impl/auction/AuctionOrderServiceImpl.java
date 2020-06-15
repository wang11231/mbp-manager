package com.art.manager.service.impl.auction;

import com.art.manager.constants.LockConstants;
import com.art.manager.dto.AucitonGoodsListDto;
import com.art.manager.enums.*;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.auction.*;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.AuctionGoods;
import com.art.manager.pojo.auction.AuctionOrder;
import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.pojo.auction.AuctionProxyLimit;
import com.art.manager.pojo.schedule.AuctionGoodsShedule;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.service.auction.AuctionOrderService;
import com.art.manager.service.auction.AuctionProcessService;
import com.art.manager.service.auction.AuctionProxyLimitService;
import com.art.manager.service.base.BaseService;
import com.art.manager.util.DateUtil;
import com.art.manager.util.IdUtils;
import com.art.manager.vo.AuctionGoodsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuctionOrderServiceImpl extends BaseService implements AuctionOrderService {

    @Autowired
    private AuctionOrderMapper auctionOrderMapper;

    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Autowired
    private AuctionProcessService auctionProcessService;

    @Autowired
    private AuctionProxyLimitMapper auctionProxyLimitMapper;

    @Autowired
    private AuctionGoodsService auctionGoodsService;

    @Autowired
    private AuctionProxyLimitService auctionProxyLimitService;

    @Autowired
    private AuctionProcessMapper auctionProcessMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;

    private static final BigDecimal DEFAULT_VALUE_ZERO = new BigDecimal(0);

    @Override
    public boolean delayExpireTime(AuctionOrderReq req) {
        if (req == null || StringUtils.isBlank(req.getOrderNo())) {
            throw new AuctionGoodsException("订单号为空！");
        }
        if (req.getGoodsId() == null) {
            throw new AuctionGoodsException("商品id为空！");
        }
        AuctionGoodsReq auctionGoodsReq = new AuctionGoodsReq();
        auctionGoodsReq.setId(req.getGoodsId());
        //查询商品状态是否为竞拍成功  1:未开始，2:拍卖中，3:竞拍成功，4:已出售，0:流拍
        AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectOneById(auctionGoodsReq);
        if (auctionGoodsVo == null) {
            throw new AuctionGoodsException("商品不存在！");
        }
        if (!"3".equals(auctionGoodsVo.getGoodsStatus())) {
            throw new AuctionGoodsException("商品状态不是竞拍成功！");
        }

        //查询订单状态是否为待支付 1:待支付，2:待发货，3:待收货，4:已收货, 0 已失效
        List<AuctionOrder> orders = auctionOrderMapper.selectByGoodsId(req);
        if (orders == null || orders.size() == 0) {
            throw new AuctionGoodsException("订单不存在！");
        }
        if (orders.size() > 1) {
            throw new AuctionGoodsException("存在多个有效订单！");
        }
        AuctionOrder auctionOrder = orders.get(0);
        if (!"1".equals(auctionOrder.getOrderStatus())) {
            throw new AuctionGoodsException("订单状态不是待支付！");
        }
        req.setDealDate(new Date());
        int count = auctionOrderMapper.delayExpireTime(req);
        return count > 0 ? true : false;
    }

    @Override
    public Map<String, Object> getList(AuctionOrderReq auctionOrderReq) {
        Page page = new Page();
        setPrePageInfo(page, auctionOrderReq);
        List<AuctionOrder> list = auctionOrderMapper.getList(auctionOrderReq);
        return setAfterPageInfo(list, page);
    }

    @Override
    public AuctionOrder selectByOrderNo(AuctionOrderReq auctionOrderReq) {
        if (auctionOrderReq == null || StringUtils.isBlank(auctionOrderReq.getOrderNo())) {
            throw new AuctionGoodsException("商品订单号为空！");
        }
        AuctionOrder auctionOrder = auctionOrderMapper.selectByOrderNo(auctionOrderReq);
        if (auctionOrder == null) {
            throw new AuctionGoodsException("商品订单不存在！");
        }
        AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
        auctionGoodsreq.setId(auctionOrder.getGoodsId());
        AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectById(auctionGoodsreq);
        auctionOrder.setCategoryName(auctionGoodsVo.getCategoryName());
        auctionOrder.setParentName(auctionGoodsVo.getParentName());
        auctionOrder.setArtistName(auctionGoodsVo.getArtistName());
        auctionOrder.setShowPic(auctionGoodsVo.getShowPic());
        auctionOrder.setCurrentTime(new Date());
        return auctionOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCompanyInfo(AuctionOrder auctionOrder) {
        if (auctionOrder == null || StringUtils.isBlank(auctionOrder.getOrderNo())) {
            throw new AuctionGoodsException("商品订单号为空！");
        }
        AuctionOrder order = new AuctionOrder();
        order.setOrderNo(auctionOrder.getOrderNo());
        order.setTransportCompany(auctionOrder.getTransportCompany());//物流公司
        order.setTransportNo(auctionOrder.getTransportNo());//物流单号
        order.setTransportAddr(auctionOrder.getTransportAddr());//物流地址
        int count = auctionOrderMapper.updateByOrderNo(order);
        //修改订单状态：2.代发货->3.已发货
        AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
        auctionOrderReq.setOrderNo(auctionOrder.getOrderNo());
        auctionOrderReq.setDealDate(new Date());
        auctionOrderReq.setUsername(auctionOrder.getUsername());
        auctionOrderMapper.updateStatusToSendGoods(auctionOrderReq);
        return count > 0 ? true : false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean onePrice(HttpServletRequest request, AuctionOrderReq auctionOrderReq) {
        if (auctionOrderReq == null || auctionOrderReq.getGoodsId() == null) {
            throw new AuctionGoodsException("商品id为空！");
        }
        if (auctionOrderReq.getAddrId() == null) {
            throw new AuctionGoodsException("收货地址id为空！");
        }
        Long goodsId = auctionOrderReq.getGoodsId();
        synchronized (LockConstants.getLock(goodsId)) {
            getUsername(request, auctionOrderReq);
            AuctionGoodsVo auctionGoods = auctionGoodsService.getAuctionGoods(auctionOrderReq.getGoodsId());
            if (auctionGoods.getOnePrice() == null) {
                throw new AuctionGoodsException("此商品未设置一口价");
            }
            //验证当前价+增幅是否大于一口价
            AuctionProcess process = auctionProcessService.getAuctionProcess(auctionOrderReq.getGoodsId());
            if(process != null){
                BigDecimal incrementStep = auctionGoods.getIncrementStep();
                incrementStep = (incrementStep == null) ? new BigDecimal(0) :incrementStep;
                if((process.getCurrentPrice().add(incrementStep)).compareTo(auctionGoods.getOnePrice()) >= 0){
                    return false;
                }
            }
            //验证自动代理出价是否执行和是否是最后5分钟
            validProxyPriceDone(auctionGoods);
            validGoods(auctionOrderReq, auctionGoods);
            //插入流程表
            auctionOrderReq.setGoodsName(auctionGoods.getGoodsName());
            auctionOrderReq.setCurrentPrice(auctionGoods.getOnePrice());
            insertAuctionProcess(auctionOrderReq, AddPriceTypeEnum.ONE);
            //插入订单表（一口价）
            auctionOrderReq.setBuyerDate(new Date());
            auctionOrderReq.setOrderNo(IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.PP));
            int count = auctionOrderMapper.insertOnePrice(auctionOrderReq);
            //退其余参拍者保证金
            auctionGoodsService.backBaseAmount(goodsId, auctionOrderReq.getUsername());
            //修改商品状态 拍卖中2->拍卖成功3
            auctionGoodsService.updateGoodsStatusToActionSuccess(goodsId, "onePrice update goods status 2 to 3");
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean immediatelyPrice(HttpServletRequest request, AuctionOrderReq auctionOrderReq) {
        if (auctionOrderReq == null || auctionOrderReq.getGoodsId() == null) {
            throw new AuctionGoodsException("商品id为空！");
        }
        //判断立即出价价格:1、非空；2、要高于当前价格
        if (auctionOrderReq.getCurrentPrice() == null) {
            throw new AuctionGoodsException("出价价格为空！");
        }
        if (auctionOrderReq.getCurrentPrice().compareTo(new BigDecimal(0)) <= 0) {
            throw new AuctionGoodsException("出价价格需大于0！");
        }
        String addPriceType = auctionOrderReq.getAddPriceType();
        if (StringUtils.isBlank(addPriceType)) {
            throw new AuctionGoodsException("加价形式为空！");
        }
        if (!AddPriceTypeEnum.IMMEDIATELY.getCode().equals(addPriceType) && !AddPriceTypeEnum.PROXY.getCode().equals(addPriceType)) {
            throw new AuctionGoodsException("加价形式只能是立即出价和代理出价！");
        }
        if (auctionOrderReq.getAddrId() == null) {
            throw new AuctionGoodsException("收货地址id为空！");
        }
        Long goodsId = auctionOrderReq.getGoodsId();
        synchronized (LockConstants.getLock(goodsId)) {
            getUsername(request, auctionOrderReq);
            //记录流程，判断立即出价价格是否大于等于一口价，若是，生成订单
            AuctionGoodsVo auctionGoods = auctionGoodsService.getAuctionGoods(goodsId);
            //验证出价时间

            AuctionProcess process = auctionProcessService.getAuctionProcess(auctionOrderReq.getGoodsId());
            validPrice(auctionOrderReq, auctionGoods, process);
            //验证自动代理出价是否执行
            validProxyPriceDone(auctionGoods);
            BigDecimal onePrice = auctionGoods.getOnePrice();
            auctionOrderReq.setGoodsName(auctionGoods.getGoodsName());
            AuctionProxyLimit limit = new AuctionProxyLimit();
            limit.setPriceLimit(auctionOrderReq.getCurrentPrice());
            limit.setAddrId(auctionOrderReq.getAddrId());
            limit.setGoodsId(goodsId);
            limit.setUsername(auctionOrderReq.getUsername());
            AuctionProxyLimit auctionProxyLimit = auctionProxyLimitMapper.selectOneProxylimitByGoondId(limit);
            BigDecimal currentPrice = auctionOrderReq.getCurrentPrice();
            BigDecimal incrementStep = auctionGoods.getIncrementStep();
            //onePrice = onePrice == null ? new BigDecimal(Integer.MAX_VALUE) : onePrice;
            onePrice = new BigDecimal(Integer.MAX_VALUE);
            if (AddPriceTypeEnum.IMMEDIATELY.getCode().equals(addPriceType)) {
                //立即出价可以一直出价，只要高于当前价
                /*if(process != null && process.getUsername().equals(auctionOrderReq.getUsername())){
                    throw new AuctionGoodsException("您当前已经处于领先，无需再立即出价！");
                }*/
                //插入流程表
                insertAuctionProcess(auctionOrderReq, AddPriceTypeEnum.IMMEDIATELY);
                if (auctionProxyLimit != null) {
                    BigDecimal priceLimit = auctionProxyLimit.getPriceLimit();
                    BigDecimal addPrice = currentPrice.add(incrementStep);
                    boolean immediatelyFlag = false;
                    if (priceLimit.compareTo(currentPrice) > 0 && addPrice.compareTo(priceLimit) <= 0) {
                        AuctionOrderReq req = new AuctionOrderReq();
                        req.setUsername(auctionProxyLimit.getUsername());
                        req.setGoodsId(auctionProxyLimit.getGoodsId());
                        req.setGoodsName(auctionOrderReq.getGoodsName());
                        req.setAddrId(auctionProxyLimit.getAddrId());
                        req.setCurrentPrice(addPrice);
                        insertAuctionProcess(req, AddPriceTypeEnum.PROXY);
                        currentPrice = addPrice;
                        immediatelyFlag = true;
                    }
                    if (priceLimit.compareTo(currentPrice) == 0 && !immediatelyFlag) {
                        AuctionOrderReq req = new AuctionOrderReq();
                        req.setUsername(auctionProxyLimit.getUsername());
                        req.setGoodsId(auctionProxyLimit.getGoodsId());
                        req.setGoodsName(auctionOrderReq.getGoodsName());
                        req.setAddrId(auctionProxyLimit.getAddrId());
                        req.setCurrentPrice(currentPrice);
                        insertAuctionProcess(req, AddPriceTypeEnum.PROXY);
                    }
                }
                if (onePrice != null && currentPrice.compareTo(onePrice) >= 0) {
                    //插入订单
                    return insertOrder(auctionOrderReq, auctionGoods);
                } else {
                    //没有生成订单，判断是否是最后5分钟
                    iSLast5Min(auctionGoods);
                }
            } else {
                //代理出价-未开始
                if(auctionGoods.getGoodsStatus().equals(GoodsStatusEnum.AUCTION_pre.getCode())){
                    auctionProxyLimitService.insertProxyLimit(auctionOrderReq);
                    //没有生成订单，判断是否是最后5分钟
                    iSLast5Min(auctionGoods);
                    return true;
                }
                //代理出价-拍卖中
                if (process == null) {
                    //第一个代理出价的人 插入代理出价表和流程表  价格=起拍价（起拍价如果是0的话代理出价就是 0 + 加价幅度  起价如果大于0的话代理出价就是直接是起拍价）
                    BigDecimal startPrice = auctionGoods.getStartPrice();
                    if(startPrice.compareTo(DEFAULT_VALUE_ZERO)==0){
                        startPrice = startPrice.add(incrementStep);
                    }
                    if (auctionOrderReq.getCurrentPrice().compareTo(startPrice) < 0) {
                        //throw new AuctionGoodsException("出价价格需大于等于起拍价：" + startPrice);
                        throw new AuctionGoodsException(String.valueOf(startPrice.setScale(0,BigDecimal.ROUND_DOWN)));
                    }
                    auctionOrderReq.setCurrentPrice(startPrice);
                    insertAuctionProcess(auctionOrderReq, AddPriceTypeEnum.PROXY);
                    auctionProxyLimitService.insertProxyLimit(auctionOrderReq);
                    //没有生成订单，判断是否是最后5分钟
                    iSLast5Min(auctionGoods);
                    return true;
                }
                BigDecimal price = process.getCurrentPrice().add(incrementStep);
                if (auctionOrderReq.getCurrentPrice().compareTo(price) < 0) {
                    //throw new AuctionGoodsException("出价价格需大于等于当前价+增幅：" + price);
                    throw new AuctionGoodsException(String.valueOf(price.setScale(0,BigDecimal.ROUND_DOWN)));
                }
                if (process.getUsername().equals(auctionOrderReq.getUsername())) {
                    //当前用户与最高价是同一人，不用pk，需要高于当前加，不一定需要高于上次设置的价格
                    /*if(auctionProxyLimit != null && auctionProxyLimit.getUsername().equals(auctionOrderReq.getUsername()) && auctionOrderReq.getCurrentPrice().compareTo(auctionProxyLimit.getPriceLimit()) <= 0){
                        throw new AuctionGoodsException("出价价格需大于之前设置价格："+auctionProxyLimit.getPriceLimit());
                    }*/
                    //插入或更新代理出价表
                    insertAndUpdateProxyLimit(limit);
                    //没有生成订单，判断是否是最后5分钟
                    iSLast5Min(auctionGoods);
                    return true;
                } else {
                    if (auctionProxyLimit == null) {
                        //goodsId没有对应的代理出价 插入流程和代理出价  价格=当前价+增幅
                        auctionOrderReq.setCurrentPrice(price);
                        insertAuctionProcess(auctionOrderReq, AddPriceTypeEnum.PROXY);
                        auctionProxyLimitMapper.insert(limit);
                        //没有生成订单，判断是否是最后5分钟
                        iSLast5Min(auctionGoods);
                        return true;
                    } else {
                        //当前用户与最高价不是同一人，pk
                        BigDecimal processCurrentPrice = process.getCurrentPrice();
                        BigDecimal priceLimit = auctionProxyLimit.getPriceLimit();
                        List<AuctionProcess> processes = new ArrayList<>();
                        int flag = 0;
                        boolean onPriceFlag = false;
                        while (true) {
                            //流程 先插入请求方的流程
                            processCurrentPrice = processCurrentPrice.add(incrementStep);
                            boolean firstFlag = processCurrentPrice.compareTo(onePrice) >= 0;
                            if (processCurrentPrice.compareTo(currentPrice) > 0) {
                                flag = 1;
                                break;
                            }
                            auctionOrderReq.setCurrentPrice(firstFlag ? onePrice : processCurrentPrice);
                            processes.add(getProcesss(auctionOrderReq, AddPriceTypeEnum.PROXY));
                            if (firstFlag) {
                                //生成订单
                                insertOrder(auctionOrderReq, auctionGoods);
                                flag = 2;
                                onPriceFlag = true;
                                break;
                            }
                            // 流程 再插入原来方的流程
                            processCurrentPrice = processCurrentPrice.add(incrementStep);
                            AuctionOrderReq req = new AuctionOrderReq();
                            req.setUsername(auctionProxyLimit.getUsername());
                            req.setGoodsId(auctionProxyLimit.getGoodsId());
                            req.setGoodsName(auctionOrderReq.getGoodsName());
                            req.setAddrId(auctionProxyLimit.getAddrId());
                            boolean secondFlag = processCurrentPrice.compareTo(onePrice) >= 0;
                            if (processCurrentPrice.compareTo(priceLimit) > 0) {
                                flag = 2;
                                break;
                            }
                            req.setCurrentPrice(secondFlag ? onePrice : processCurrentPrice);
                            processes.add(getProcesss(req, AddPriceTypeEnum.PROXY));
                            if (secondFlag) {
                                flag = 1;
                                //生成订单
                                insertOrder(auctionOrderReq, auctionGoods);
                                onPriceFlag = true;
                                break;
                            }
                        }
                        //批量插入流程表
                        if (processes != null && processes.size() > 0) {
                            int size = processes.size();
                            BigDecimal prePrice = processes.get(size - 1).getCurrentPrice();
                            BigDecimal nextPrice = prePrice.add(incrementStep);
                            if (!onPriceFlag) {
                                //一口价生成订单不需要判断
                                if (priceLimit.compareTo(nextPrice) < 0 && currentPrice.compareTo(nextPrice) < 0) {
                                    if (priceLimit.compareTo(currentPrice) >= 0) {
                                        if (size % 2 != 0) {
                                            //原代理价大于等于请求价，且原代理价和请求加都小于nextPrice，原代理价赢
                                            AuctionOrderReq req = new AuctionOrderReq();
                                            req.setUsername(auctionProxyLimit.getUsername());
                                            req.setGoodsId(auctionProxyLimit.getGoodsId());
                                            req.setGoodsName(auctionOrderReq.getGoodsName());
                                            req.setAddrId(auctionProxyLimit.getAddrId());
                                            req.setCurrentPrice(prePrice);
                                            processes.add(getProcesss(req, AddPriceTypeEnum.PROXY));
                                            flag = 1;
                                        }
                                    } else {
                                        if (size % 2 == 0) {
                                            //原代理价小于等于请求价，且原代理价和请求加都小于nextPrice，请求价赢
                                            auctionOrderReq.setCurrentPrice(prePrice);
                                            processes.add(getProcesss(auctionOrderReq, AddPriceTypeEnum.PROXY));
                                            flag = 2;
                                        }
                                    }
                                } else {
                                    /**
                                     * 多插入一个流程
                                     * 1、流程是偶数；
                                     * 2、流程中最后一个的价格和请求中的价格相等
                                     */
                                    if (size % 2 == 0 && prePrice.equals(currentPrice) && prePrice.compareTo(onePrice) < 0) {
                                        AuctionOrderReq req = new AuctionOrderReq();
                                        req.setUsername(auctionProxyLimit.getUsername());
                                        req.setGoodsId(auctionProxyLimit.getGoodsId());
                                        req.setGoodsName(auctionOrderReq.getGoodsName());
                                        req.setAddrId(auctionProxyLimit.getAddrId());
                                        boolean threeFlag = nextPrice.compareTo(onePrice) >= 0;
                                        if (nextPrice.compareTo(priceLimit) > 0) {
                                            flag = 2;
                                        } else {
                                            req.setCurrentPrice(threeFlag ? onePrice : nextPrice);
                                            processes.add(getProcesss(req, AddPriceTypeEnum.PROXY));
                                            if (threeFlag) {
                                                flag = 1;
                                                //生成订单
                                                insertOrder(auctionOrderReq, auctionGoods);
                                            }
                                        }
                                    }
                                }
                            }
                            //全量插入
                            auctionProcessMapper.batchInsert(processes);
                            //更新id大者为领先
                            auctionProcessMapper.batchUpdateProxyStatus(goodsId);
                        }
                        insertAndUpdateProxyLimit(limit);
                        //原代理出价胜出
                        if (flag == 1) {
                            limit.setUsername(auctionProxyLimit.getUsername());
                        }
                        //根据googsId和username批量更新代理状态
                        auctionProxyLimitMapper.batchUpdateProxyStatusToValid(limit);
                        //没有生成订单，判断是否是最后5分钟
                        iSLast5Min(auctionGoods);
                    }
                }
            }
            return true;
        }
    }

    private void insertAndUpdateProxyLimit(AuctionProxyLimit limit) {
        //请求方代理出价存在更新，不存在插入
        AuctionProxyLimit proxyLimit = auctionProxyLimitMapper.selectOneByUserAndGoodId(limit);
        if (proxyLimit == null) {
            auctionProxyLimitMapper.insert(limit);
        } else {
            auctionProxyLimitMapper.updateProxyLimitByUserAndGoodId(limit);
        }
    }

    private boolean insertOrder(AuctionOrderReq auctionOrderReq, AuctionGoodsVo auctionGoods) {
        //插入订单表（立即出价&代理出价）
        Long goodsId = auctionOrderReq.getGoodsId();
        String addPriceType = auctionOrderReq.getAddPriceType();
        auctionOrderReq.setBuyerDate(new Date());
        auctionOrderReq.setOrderNo(IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.PP));
        BigDecimal transportAmount = auctionGoods.getTransportAmount();
        //代理价格或立即出价若超过一口价，生成订单的价格是按一口价价格
        auctionOrderReq.setCurrentPrice(auctionGoods.getOnePrice().add(transportAmount == null ? new BigDecimal(0) : transportAmount));
        int count = auctionOrderMapper.insertImmediatelyPrice(auctionOrderReq);
        //修改商品状态 拍卖中2->拍卖成功3
        String remarkPre = AddPriceTypeEnum.IMMEDIATELY.getCode().equals(addPriceType) ? "immediatelyPrice" : "proxyPrice";
        auctionGoodsService.updateGoodsStatusToActionSuccess(goodsId, remarkPre + " update goods status 2 to 3");
        //退其余参拍者保证金
        auctionGoodsService.backBaseAmount(goodsId, auctionOrderReq.getUsername());
        return count > 0 ? true : false;
    }

    @Override
    public int updateStatusByOrderNo(String orderNo, Long goodsId, String username) {
        AuctionOrder order = new AuctionOrder();
        order.setOrderNo(orderNo);
        order.setPayerName(username);
        int count = auctionOrderMapper.updateStatusByOrderNo(order);
        if(count > 0){
            auctionGoodsMapper.updateGoodsStatusToSale(goodsId);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealExpiredOrder() {
        //查询所有过期订单
        List<AuctionOrder> orderList = auctionOrderMapper.selectAllExpiredOrder();
        if (orderList != null && orderList.size() > 0) {
            //修改订单状态：1待支付-> 已失效0
            List<Long> ids = orderList.stream().map(order -> order.getGoodsId()).collect(Collectors.toList());
            int count = auctionOrderMapper.batchUpdateExpiredOrder(ids);
            log.info("updateOrderStatusToExpired success count:{}", count);
            //修改保证金状态为扣除
            Map<String, List<AuctionOrder>> list = orderList.stream().collect(Collectors.groupingBy(order -> order.getBuyerName()));
            Iterator<String> iter = list.keySet().iterator();
            while (iter.hasNext()) {
                //修改保证金状态为扣除
                String username = iter.next();
                List<Long> goodsIds = list.get(username).stream().map(vo -> vo.getGoodsId()).collect(Collectors.toList());
                Map<String, Object> params = new HashMap();
                params.put("username", username);
                params.put("goodsIds", goodsIds);
                weChatUserMapper.updateUserBondAndDeduct(params);
                auctionBaseAmountMapper.updateBaseAmountStatusToDeduct(params);
                //修改订单为流拍
                List<AuctionGoodsShedule> param = new ArrayList<>();
                if (goodsIds != null && goodsIds.size() > 0) {
                    Date currentTime = new Date();
                    for (Long id : ids) {
                        AuctionGoodsShedule shedule = new AuctionGoodsShedule();
                        shedule.setId(id);
                        shedule.setCurrentTime(currentTime);
                        //3:竞拍成功
                        shedule.setPreGoodsStatus("3");
                        //0:流拍
                        shedule.setCurrentGoodsStatus("0");
                        shedule.setGoodsStatusRemark("order Expired update goods status 3 to 0");
                        param.add(shedule);
                    }
                    auctionGoodsMapper.batchUpdateGoodsStatusToActionSuccess(param);
                }
            }
            return;
        }
        log.info("updateOrderStatusToExpired success count:{}", 0);
    }

    @Override
    public AuctionOrder selectByGoodsId(AuctionOrderReq req) {
        if (req == null || req.getGoodsId() == null) {
            throw new AuctionGoodsException("商品id为空！");
        }
        List<AuctionOrder> auctionOrders = auctionOrderMapper.selectByGoodsId(req);
        if (auctionOrders == null || auctionOrders.size() == 0) {
            throw new AuctionGoodsException("商品订单不存在！");
        }
        return auctionOrders.get(0);
    }

    private void validPrice(AuctionOrderReq auctionOrderReq, AuctionGoodsVo auctionGoods, AuctionProcess process) {
        //验证立即出价是否大于当前价格+增幅 代理出价可以随便设置
        if (AddPriceTypeEnum.IMMEDIATELY.getCode().equals(auctionOrderReq.getAddPriceType()) && process != null && process.getCurrentPrice() != null) {
            if (auctionOrderReq.getCurrentPrice().compareTo(process.getCurrentPrice().add(auctionGoods.getIncrementStep())) < 0) {
                //throw new AuctionGoodsException("出价价格需大于等于当前价格+增幅：" + process.getCurrentPrice().add(auctionGoods.getIncrementStep()));
                throw new AuctionGoodsException(String.valueOf(process.getCurrentPrice().add(auctionGoods.getIncrementStep()).setScale(0,BigDecimal.ROUND_DOWN)));
            }
        }
        validGoods(auctionOrderReq, auctionGoods);
    }

    private void validGoods(AuctionOrderReq auctionOrderReq, AuctionGoodsVo auctionGoods) {
        //验证当前时间是否大于拍卖开始时间且小于拍卖结束时间
        Date currentTime = new Date();
        //立即出价、一口价需拍卖开始后，代理出价可以在开始拍卖前
        if (!AddPriceTypeEnum.PROXY.getCode().equals(auctionOrderReq.getAddPriceType()) && currentTime.compareTo(auctionGoods.getStartDate()) < 0) {
            throw new AuctionGoodsException("当前时间应大于拍卖开始时间！");
        }
        if (currentTime.compareTo(auctionGoods.getEndDate()) >= 0) {
            throw new AuctionGoodsException("当前时间应小于拍卖结束时间！");
        }
        //立即出价、一口价需拍卖拍卖中，代理出价可以在开始拍卖前
        if (!AddPriceTypeEnum.PROXY.getCode().equals(auctionOrderReq.getAddPriceType()) && !GoodsStatusEnum.AUCTION_IN.getCode().equals(auctionGoods.getGoodsStatus())) {
            throw new AuctionGoodsException("商品状态不在拍卖中！");
        }
        //验证订单是否存在
        List<AuctionOrder> orders = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
        if (orders != null && orders.size() > 0) {
            throw new AuctionGoodsException("订单已存在！");
        }
    }

    private void getUsername(HttpServletRequest request, AuctionOrderReq auctionOrderReq) {
        String username = getUsername(request, false);
        //String username = "18662566803";
        auctionOrderReq.setUsername(username);

        //查询订单是否生成
        List<AuctionOrder> list = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
        if (list != null && list.size() > 0) {
            throw new AuctionGoodsException("商品订单已经生成！");
        }
    }

    private void insertAuctionProcess(AuctionOrderReq auctionOrderReq, AddPriceTypeEnum addPriceTypeEnum) {
        auctionProcessService.insert(getProcesss(auctionOrderReq, addPriceTypeEnum));
    }

    private AuctionProcess getProcesss(AuctionOrderReq auctionOrderReq, AddPriceTypeEnum addPriceTypeEnum) {
        AuctionProcess auctionProcess = new AuctionProcess();
        auctionProcess.setGoodsId(auctionOrderReq.getGoodsId());
        auctionProcess.setGoodsName(auctionOrderReq.getGoodsName());
        auctionProcess.setAddPriceType(addPriceTypeEnum.getCode());
        auctionProcess.setUsername(auctionOrderReq.getUsername());
        auctionProcess.setCurrentPrice(auctionOrderReq.getCurrentPrice());
        auctionProcess.setRemark(AddPriceTypeEnum.PROXY.getCode().equals(addPriceTypeEnum.getCode()) ? "proxy price insert" : AddPriceTypeEnum.IMMEDIATELY.getCode().equals(addPriceTypeEnum.getCode()) ? "immediately price insert" : AddPriceTypeEnum.ONE.getCode().equals(addPriceTypeEnum.getCode()) ? "one price insert" : "");
        auctionProcess.setAddrId(auctionOrderReq.getAddrId());
        return auctionProcess;
    }

    private void validProxyPriceDone(AuctionGoodsVo auctionGoods) {
        //验证自动代理出价是否执行
        if (auctionGoods != null && auctionGoods.getGoodsStatus().equals(GoodsStatusEnum.AUCTION_IN.getCode()) && auctionGoods.getAutoProxy().equals(1)) {
            throw new AuctionGoodsException("商品" + auctionGoods.getGoodsName() + "自动代理出价尚未执行！");
        }
    }

    private void iSLast5Min(AuctionGoodsVo auctionGoodsVo) {
        Date endDate = auctionGoodsVo.getEndDate();
        Date currentDate = new Date();
        Calendar nextCal = Calendar.getInstance();
        nextCal.setTime(currentDate);
        nextCal.add(Calendar.MINUTE, 2);
        if (currentDate.compareTo(endDate) < 0 && nextCal.getTime().compareTo(endDate) >= 0 /*&& StatusEnum.VALID.getCode().equals(auctionGoodsVo.getSettingStatus())*/) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.MINUTE, 5);
            AuctionGoods auctionGoods = new AuctionGoods();
            auctionGoods.setId(auctionGoodsVo.getId());
            Date nextTime = cal.getTime();
            auctionGoods.setNextEndDate(nextTime);
            //设置为0,无效
            auctionGoods.setSettingStatus(StatusEnum.INVALID.getCode());
            //延长拍品的结束时间
            auctionGoodsMapper.updateById(auctionGoods);
            //延长自动代理拍品的结束时间
            AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
            auctionProxyLimit.setNextEndDate(nextTime);
            auctionProxyLimit.setGoodsId(auctionGoodsVo.getId());
            auctionProxyLimitMapper.delayEndDateByGoodsId(auctionProxyLimit);
        }
    }

    /**
     * 拍卖订单列表
     */
    @Override
    public Map<String, Object> getAuctionOrderListForEntry(Map<String, Object> params) throws ParseException {
        //6:已参拍,1:待支付,2:待发货,3:待收货,0:全部
        String mobile = (String) params.get("mobile");
        List<AucitonGoodsListDto> aucitonGoodsListDto = new ArrayList<>();
        if ( StringUtils.isNotBlank(mobile)) {
            List<AuctionProcess> auctionProcessList = auctionProcessMapper.selectProxyLimitByUser(mobile);
            if (auctionProcessList != null && auctionProcessList.size() > 0) {
                for (int i = 0; i < auctionProcessList.size(); i++) {
                    AucitonGoodsListDto auctionDto = new AucitonGoodsListDto();
                    AuctionProcess auctionProcess = auctionProcessList.get(i);
                    AuctionGoodsVo auctionGoodsVo = selectAuctionGoodsVo(auctionProcess.getGoodsId());
                    //商品图片
                    auctionDto.setShowPic(auctionGoodsVo.getShowPic());
                    //商品Id
                    auctionDto.setGoodsId(auctionGoodsVo.getId());
                    //商品名称
                    auctionDto.setGoodsName(auctionGoodsVo.getGoodsName());
                    //加价方式
                    auctionDto.setAddPriceType(auctionProcess.getAddPriceType());
                    //商品状态
                    auctionDto.setGoodsStatus(auctionGoodsVo.getGoodsStatus());
                    //商品运费
                    auctionDto.setTransportAmount(auctionGoodsVo.getTransportAmount());
                    auctionDto.setCreateTime(auctionGoodsVo.getCreateTime());
                    auctionDto.setStartDate(auctionGoodsVo.getStartDate());
                    auctionDto.setEndDate(auctionGoodsVo.getEndDate());
                    AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
                    auctionOrderReq.setGoodsId(auctionProcess.getGoodsId());
                    List<AuctionOrder> auctionOrders = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
                    AuctionOrder auctionOrder = (auctionOrders != null && auctionOrders.size() > 0) ? auctionOrders.get(0) : null;
                    boolean isBuyerOrder = auctionOrder != null && auctionOrder.getBuyerName().equals(mobile) ? true : false;
                    AuctionProcess currentPrice = auctionProcessService.getAuctionProcess(auctionProcess.getGoodsId());
                    auctionDto.setProcessStatus(auctionProcess.getProcessStatus());
                    if (auctionGoodsVo.getGoodsStatus().equals("0")) {
                        auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                        auctionDto.setOrderNo(isBuyerOrder ? auctionOrder.getOrderNo() : null);
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                    }
                    if (auctionGoodsVo.getGoodsStatus().equals("4")) {
                        //如果商品状价格是  出售， 或者流拍的话， 商品价格取成交价格
                        auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                        auctionDto.setOrderNo(isBuyerOrder ? auctionOrder.getOrderNo() : null);
                    }
                    if (auctionGoodsVo.getGoodsStatus().equals("2")) {
                        auctionDto.setCurrentTime(DateUtil.getDate(new Date()));
                        auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                    }
                    if (auctionGoodsVo.getGoodsStatus().equals("3")) {
                        auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                        auctionDto.setOrderNo(isBuyerOrder ? auctionOrder.getOrderNo() : null);
                        auctionDto.setOrderTime(auctionOrder.getExpireDate());
                    }
                    if (auctionOrder != null) {
                        auctionDto.setBuyerName(auctionOrder.getBuyerName());
                    }
                    /**
                     * 是否超时 判断是否超时
                     */
                    if (isBuyerOrder) {
                        auctionDto.setIsOverTime(auctionOrder.getOrderStatus().equals("0") ? true : false);
                    }
                    //当前商品如果是一口价，取拍品的一口价格
                    setPrice(auctionGoodsVo, auctionProcess, auctionDto, true, mobile);
                    aucitonGoodsListDto.add(auctionDto);
                }
            }
        }
        aucitonGoodsListDto = aucitonGoodsListDto.stream().sorted(Comparator.comparing(AucitonGoodsListDto::getCreateTime).reversed()).collect(Collectors.toList());
        Map<String, Object> result = new HashMap();
        result.put("list", aucitonGoodsListDto);
        result.put("currentTime", new Date());
        return result;
    }


    /**
     * 拍卖订单列表
     */
    @Override
    public Map<String, Object> getAuctionOrderList(Map<String, Object> params) throws ParseException {
        //6:已参拍,1:待支付,2:待发货,3:待收货,0:全部
        String mobile = (String) params.get("mobile");
        String type = (String) params.get("type");
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(pageNum, pageSize);
        List<AucitonGoodsListDto> aucitonGoodsListDto = new ArrayList<>();

        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(mobile)) {
            if (type.equals("6")) {
                List<AuctionProcess> auctionProcessList = auctionProcessMapper.selectProxyLimitByUser(mobile);
                //当auctionProcessList > 0 的时候 代表这个用户有参拍过的商品
                if (auctionProcessList != null && auctionProcessList.size() > 0) {
                    for (int i = 0; i < auctionProcessList.size(); i++) {
                        AucitonGoodsListDto auctionDto = new AucitonGoodsListDto();
                        AuctionProcess auctionProcess = auctionProcessList.get(i);
                        AuctionGoodsVo auctionGoodsVo = selectAuctionGoodsVo(auctionProcess.getGoodsId());
                        //商品图片
                        auctionDto.setShowPic(auctionGoodsVo.getShowPic());
                        //商品Id
                        auctionDto.setGoodsId(auctionGoodsVo.getId());
                        //商品名称
                        auctionDto.setGoodsName(auctionGoodsVo.getGoodsName());
                        //加价方式
                        auctionDto.setAddPriceType(auctionProcess.getAddPriceType());
                        //商品状态
                        auctionDto.setGoodsStatus(auctionGoodsVo.getGoodsStatus());
                        //商品运费
                        auctionDto.setTransportAmount(auctionGoodsVo.getTransportAmount());
                        auctionDto.setCreateTime(auctionGoodsVo.getCreateTime());
                        AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
                        auctionOrderReq.setGoodsId(auctionProcess.getGoodsId());
                        List<AuctionOrder> auctionOrders = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
                        AuctionOrder auctionOrder = (auctionOrders != null && auctionOrders.size() > 0) ? auctionOrders.get(0) : null;
                        boolean isBuyerOrder = auctionOrder != null && auctionOrder.getBuyerName().equals(mobile) ? true : false;
                        AuctionProcess currentPrice = auctionProcessService.getAuctionProcess(auctionProcess.getGoodsId());
                        auctionDto.setProcessStatus(auctionProcess.getProcessStatus());
                        if (auctionGoodsVo.getGoodsStatus().equals("0")) {
                            auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                            auctionDto.setOrderNo(isBuyerOrder?auctionOrder.getOrderNo():null);
                            auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                        }
                        if (auctionGoodsVo.getGoodsStatus().equals("4")) {
                            //如果商品状价格是  出售， 或者流拍的话， 商品价格取成交价格
                            auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                            auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                            auctionDto.setOrderNo(isBuyerOrder?auctionOrder.getOrderNo():null);
                        }
                        if (auctionGoodsVo.getGoodsStatus().equals("2")) {
                            auctionDto.setStartDate(auctionGoodsVo.getStartDate());
                            auctionDto.setEndDate(auctionGoodsVo.getEndDate());
                            auctionDto.setCurrentTime(DateUtil.getDate(new Date()));
                            //auctionDto.setGoodsPrice(auctionProcess.getCurrentPrice().add(auctionGoodsVo.getTransportAmount()==null ? new BigDecimal(0) :auctionGoodsVo.getTransportAmount()));
                            auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                        }
                        if (auctionGoodsVo.getGoodsStatus().equals("3")) {
                            //auctionDto.setGoodsPrice(auctionProcess.getCurrentPrice().add(auctionGoodsVo.getTransportAmount()==null ? new BigDecimal(0) :auctionGoodsVo.getTransportAmount()));
                            auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                            auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                            auctionDto.setOrderNo(isBuyerOrder?auctionOrder.getOrderNo():null);
                            auctionDto.setOrderTime(auctionOrder.getExpireDate());
                        }
                        if(auctionOrder != null){
                            auctionDto.setBuyerName(auctionOrder.getBuyerName());
                        }
                        /**
                         * 是否超时 判断是否超时
                         */
                        if(isBuyerOrder){
                            auctionDto.setIsOverTime(auctionOrder.getOrderStatus().equals("0") ? true :false);
                        }
                        //当前商品如果是一口价，取拍品的一口价格
                        setPrice(auctionGoodsVo, auctionProcess, auctionDto, true, mobile);
                        aucitonGoodsListDto.add(auctionDto);
                    }
                }
                aucitonGoodsListDto = aucitonGoodsListDto.stream().sorted(Comparator.comparing(AucitonGoodsListDto::getCreateTime).reversed()).collect(Collectors.toList());
                PageInfo<AuctionProcess> pageInfo = new PageInfo<>(auctionProcessList);
                int pages = Page.getPages(pageInfo.getTotal(), pageSize);
                Map<String, Object> result = new HashMap();
                result.put("total", pageInfo.getTotal());
                result.put("pages", pages);
                result.put("list", aucitonGoodsListDto);
                result.put("currentTime", new Date());
                return result;
            } else if (type.equals("0")) {
                //通过手机号码查该用户下的订单
                List<AuctionOrder> orderList = auctionOrderMapper.getAuctionOrderMobileList(mobile, null);
                for (int i = 0; i < orderList.size(); i++) {
                    AucitonGoodsListDto auctionDto = new AucitonGoodsListDto();
                    AuctionOrder auctionOrder = orderList.get(i);
                    AuctionGoodsVo auctionGoodsVo = selectAuctionGoodsVo(auctionOrder.getGoodsId());
                    AuctionProcess currentPrice = auctionProcessService.getAuctionProcess(auctionOrder.getGoodsId());
                    auctionDto.setOrderNo(auctionOrder.getOrderNo());

                    auctionDto.setShowPic(auctionGoodsVo.getShowPic());
                    auctionDto.setGoodsName(auctionGoodsVo.getGoodsName());
                    auctionDto.setGoodsId(auctionGoodsVo.getId());
                    auctionDto.setCreateTime(auctionGoodsVo.getCreateTime());
                    //商品状态
                    auctionDto.setGoodsStatus(auctionGoodsVo.getGoodsStatus());
                    auctionDto.setCurrentPrice(currentPrice == null ? null :currentPrice.getCurrentPrice());
                    if(auctionGoodsVo.getGoodsStatus().equals("1")){
                        continue;
                    }
                    if (auctionGoodsVo.getGoodsStatus().equals("2")) {
                        auctionDto.setStartDate(auctionGoodsVo.getStartDate());
                        auctionDto.setEndDate(auctionGoodsVo.getEndDate());
                        auctionDto.setCurrentTime(DateUtil.getDate(new Date()));
                        auctionDto.setProcessStatus(currentPrice.getProcessStatus());
                        //auctionDto.setGoodsPrice(currentPrice.getCurrentPrice().add(auctionGoodsVo.getTransportAmount()==null ? new BigDecimal(0) :auctionGoodsVo.getTransportAmount()));
                        auctionDto.setGoodsPrice(currentPrice.getCurrentPrice());
                    } else if (auctionGoodsVo.getGoodsStatus().equals("0")) {
                        auctionDto.setGoodsPrice(auctionGoodsVo.getStartPrice());
                        auctionDto.setOrderNo(auctionOrder.getOrderNo());
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                    } else if (auctionGoodsVo.getGoodsStatus().equals("4")) {
                        //如果商品状价格是  出售， 或者流拍的话， 商品价格取成交价格
                        auctionDto.setGoodsPrice(auctionOrder.getDealPrice());
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                    } else if (auctionGoodsVo.getGoodsStatus().equals("3")) {
                        //auctionDto.setGoodsPrice(currentPrice.getCurrentPrice().add(auctionGoodsVo.getTransportAmount()==null ? new BigDecimal(0) :auctionGoodsVo.getTransportAmount()));
                        auctionDto.setGoodsPrice(auctionOrder.getDealPrice());
                        auctionDto.setOrderTime(auctionOrder.getExpireDate());
                        auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                    }
                    //商品运费
                    auctionDto.setTransportAmount(auctionGoodsVo.getTransportAmount());
                    //设置价格
                    setPrice(auctionGoodsVo, currentPrice, auctionDto, false, mobile);
                    aucitonGoodsListDto.add(auctionDto);
                }
                aucitonGoodsListDto = aucitonGoodsListDto.stream().sorted(Comparator.comparing(AucitonGoodsListDto::getCreateTime).reversed()).collect(Collectors.toList());
                PageInfo<AuctionOrder> pageInfo = new PageInfo<>(orderList);
                int pages = Page.getPages(pageInfo.getTotal(), pageSize);
                Map<String, Object> result = new HashMap();
                result.put("total", pageInfo.getTotal());
                result.put("pages", pages);
                result.put("list", aucitonGoodsListDto);
                result.put("currentTime", new Date());
                return result;
            } else if (type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4")) {
                List<AuctionOrder> orderList = auctionOrderMapper.getAuctionOrderMobileList(mobile, type);
                for (int i = 0; i < orderList.size(); i++) {
                    AucitonGoodsListDto auctionDto = new AucitonGoodsListDto();
                    AuctionOrder auctionOrder = orderList.get(i);
                    AuctionGoodsVo auctionGoodsVo = selectAuctionGoodsVo(auctionOrder.getGoodsId());
                    AuctionProcess currentPrice = auctionProcessService.getAuctionProcess(auctionOrder.getGoodsId());
                    auctionDto.setOrderNo(auctionOrder.getOrderNo());
                    auctionDto.setOrderStatus(auctionOrder.getOrderStatus());
                    auctionDto.setShowPic(auctionGoodsVo.getShowPic());
                    auctionDto.setGoodsName(auctionGoodsVo.getGoodsName());
                    auctionDto.setGoodsId(auctionGoodsVo.getId());
                    auctionDto.setGoodsPrice(auctionOrder.getDealPrice());
                    auctionDto.setOrderTime(auctionOrder.getExpireDate());
                    auctionDto.setCreateTime(auctionGoodsVo.getCreateTime());
                    //商品状态
                    auctionDto.setGoodsStatus(auctionGoodsVo.getGoodsStatus());
                    if (auctionOrder.getExpireDate() != null) {
                        auctionDto.setOrderTime(auctionOrder.getExpireDate());
                    }
                    //设置价格
                    setPrice(auctionGoodsVo, currentPrice, auctionDto, false, mobile);
                    //商品运费
                    auctionDto.setTransportAmount(auctionGoodsVo.getTransportAmount());
                    aucitonGoodsListDto.add(auctionDto);
                }
                aucitonGoodsListDto = aucitonGoodsListDto.stream().sorted(Comparator.comparing(AucitonGoodsListDto::getCreateTime).reversed()).collect(Collectors.toList());
                PageInfo<AuctionOrder> pageInfo = new PageInfo<>(orderList);
                int pages = Page.getPages(pageInfo.getTotal(), pageSize);
                Map<String, Object> result = new HashMap();
                result.put("total", pageInfo.getTotal());
                result.put("pages", pages);
                result.put("list", aucitonGoodsListDto);
                result.put("currentTime", new Date());
                return result;
            }
        }
        Map<String, Object> result = new HashMap();
        result.put("total", 0);
        result.put("pages", 0);
        result.put("list", null);
        result.put("currentTime", new Date());
        return result;
    }

    @Override
    public int updateAuctionOrderStatus(Map<String, Object> params) {
        String orderNo = (String) params.get("orderNo");
        String goodsId = (String) params.get("goodsId");
        return auctionOrderMapper.updateAuctionOrderStatus(orderNo, goodsId);
    }

    private AuctionGoodsVo selectAuctionGoodsVo(Long goodsId){
        AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
        auctionGoodsreq.setId(goodsId);
        return auctionGoodsMapper.selectOneById(auctionGoodsreq);
    }

    /**
     * 设置价格：代理出价、我的出价、一口价
     */
    public void setPrice(AuctionGoodsVo auctionGoodsVo, AuctionProcess currentPrice, AucitonGoodsListDto auctionDto, Boolean isOwner, String username) {
        auctionDto.setAddPriceType(currentPrice.getAddPriceType());
        //一口价
        auctionDto.setOnePrice(auctionGoodsVo.getOnePrice() == null ? DEFAULT_VALUE_ZERO : auctionGoodsVo.getOnePrice());
        if (AddPriceTypeEnum.ONE.getCode().equals(currentPrice.getAddPriceType())) {
            auctionDto.setUserPrice(auctionGoodsVo.getOnePrice());
        }else if (AddPriceTypeEnum.PROXY.getCode().equals(currentPrice.getAddPriceType())) {
            auctionDto.setUserPrice(currentPrice.getCurrentPrice());
        }else if (AddPriceTypeEnum.IMMEDIATELY.getCode().equals(currentPrice.getAddPriceType())) {
            auctionDto.setUserPrice(currentPrice.getCurrentPrice());
        }
        //我的出价
        if(isOwner){
            auctionDto.setUserPrice(currentPrice.getCurrentPrice());
        }else{
            AuctionProcess process = new AuctionProcess();
            process.setGoodsId(auctionGoodsVo.getId());
            process.setUsername(username);
            AuctionProcess auctionProcess = auctionProcessMapper.selectUserGoodsPrice(process);
            auctionDto.setUserPrice(auctionProcess == null ? DEFAULT_VALUE_ZERO :auctionProcess.getCurrentPrice());
        }
        //我的代理出价
        AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
        auctionProxyLimit.setGoodsId(auctionGoodsVo.getId());
        auctionProxyLimit.setUsername(username);
        AuctionProxyLimit limit = auctionProxyLimitMapper.selectOneByUserAndGoodId(auctionProxyLimit);
        auctionDto.setProxyPrice(limit == null ? DEFAULT_VALUE_ZERO :limit.getPriceLimit());
    }

}




