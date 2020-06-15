package com.art.manager.service.impl.auction;

import com.art.manager.constants.LockConstants;
import com.art.manager.enums.AddPriceTypeEnum;
import com.art.manager.enums.GoodsEnum;
import com.art.manager.enums.GoodsStatusEnum;
import com.art.manager.enums.OrderNoPrefixEnum;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.auction.*;
import com.art.manager.mapper.wechat.PaymentResultMapper;
import com.art.manager.mapper.wechat.ReceiveAddressMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.*;
import com.art.manager.pojo.config.CategoryConfig;
import com.art.manager.pojo.schedule.AuctionGoodsShedule;
import com.art.manager.pojo.wechat.ReceiveAddress;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.AuctionOrderReq;
import com.art.manager.request.H5AuctionGoodsReq;
import com.art.manager.service.CategoryConfigService;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.service.auction.AuctionOrderService;
import com.art.manager.service.auction.AuctionProcessService;
import com.art.manager.service.base.BaseService;
import com.art.manager.util.IdUtils;
import com.art.manager.util.RandomUtil;
import com.art.manager.vo.AuctionGoodsVo;
import com.art.manager.vo.AuctionPicVo;
import com.art.manager.vo.H5AuctionGoodsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuctionGoodsServiceImpl extends BaseService implements AuctionGoodsService {

    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Autowired
    private AuctionPicMapper auctionPicMapper;

    @Autowired
    private AuctionOrderMapper auctionOrderMapper;

    @Autowired
    private AuctionProcessMapper auctionProcessMapper;

    @Autowired
    private AuctionProxyLimitMapper auctionProxyLimitMapper;
    @Autowired
    private CategoryConfigService categoryConfigService;

    @Autowired
    private AuctionProcessService auctionProcessService;

    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    @Autowired
    private PaymentResultMapper paymentResultMapper;
    @Autowired
    private AuctionOrderService auctionOrderService;

    private static Object lockOne = new Object();
    private static Object lockTwo = new Object();

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean insert(AuctionGoods auctionGoods) {
        if(auctionGoods == null || StringUtils.isBlank(auctionGoods.getGoodsName())){
            throw new AuctionGoodsException("拍卖商品为空");
        }
        if(auctionGoods.getBaseAmount() == null || auctionGoods.getBaseAmount().compareTo(new BigDecimal(0)) == 0){
            throw new AuctionGoodsException("拍卖商品保证金需大于0");
        }
        if(StringUtils.isBlank(auctionGoods.getAuctionGoodsId())){
            throw new AuctionGoodsException("拍品id为空！");
        }
        //插入拍卖商品
        transPic(auctionGoods);
        auctionGoods.setGoodsStatusRemark("init goods status is 1");
        //auctionGoods.setAuctionGoodsId(OrderNoPrefixEnum.PP + RandomUtil.getPwd());
        auctionGoods.setInterestCount(/*(long)new Random().nextInt(90) +10*/3L);
        Long id = auctionGoodsMapper.insert(auctionGoods);
        //插入作品图
        insertAuctionPics(auctionGoods);
        return id>0 ? true :false ;
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        if(ids == null || ids.size() == 0){
            throw new AuctionGoodsException("需要删除拍品商品id为空");
        }
        //删除商品信息
        int count = auctionGoodsMapper.deleteByIds(ids);
        //删除作品图
        auctionPicMapper.deleteByAuctionId(ids);
        return count>0 ? true :false;
    }

    @Override
    public boolean updateById(AuctionGoods auctionGoods) {
        if(auctionGoods == null || auctionGoods.getId() == null){
            throw new AuctionGoodsException("商品id为空");
        }
        if(auctionGoods.getBaseAmount() == null || auctionGoods.getBaseAmount().compareTo(new BigDecimal(0)) == 0){
            throw new AuctionGoodsException("拍卖商品保证金需大于0");
        }
        transPic(auctionGoods);
        /**
         * 只有未开始1和流拍0才能修改拍品的开始时间和结束时间
         */
        String startDate = auctionGoods.getStartDate();
        String endDate = auctionGoods.getEndDate();
        String goodsStatus= null;
        log.info("updateById auctionGoods:{}", auctionGoods);
        if(StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(auctionGoods.getEndDate())){
            AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
            auctionGoodsreq.setId(auctionGoods.getId());
            AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectOneById(auctionGoodsreq);
            goodsStatus = auctionGoodsVo.getGoodsStatus();
            if(!auctionGoodsVo.getGoodsStatus().equals(GoodsStatusEnum.AUCTION_FAIL.getCode()) && !auctionGoodsVo.getGoodsStatus().equals(GoodsStatusEnum.AUCTION_pre.getCode())){
                throw new AuctionGoodsException("商品状态为：" +auctionGoodsVo.getGoodsStatus()+"不能修改拍卖开始时间和结束时间");
            }

            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = sdf.parse(startDate);
                endTime = sdf.parse(endDate);
            } catch (ParseException e) {
                log.error("updateById startTime&:endTime parse error:{}",e);
            }
            if(currentDate.compareTo(endTime) >= 0){
                throw new AuctionGoodsException("拍品结束时间应大于当前时间");
            }
            if(currentDate.compareTo(startTime) > 0 ){
                //未开始
                auctionGoods.setGoodsStatus(GoodsStatusEnum.AUCTION_IN.getCode());
            }else{
                //进行中
                auctionGoods.setGoodsStatus(GoodsStatusEnum.AUCTION_pre.getCode());
            }
            //更新代理出价信息表
            AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
            auctionProxyLimit.setGoodsId(auctionGoods.getId());
            auctionProxyLimit.setStartDate(auctionGoods.getStartDate());
            auctionProxyLimit.setEndDate(auctionGoods.getEndDate());
            if(GoodsStatusEnum.AUCTION_pre.getCode().equals(goodsStatus)){
                auctionProxyLimitMapper.updateProxyLimitStartAndEndDate(auctionProxyLimit);
            }
            /**
             * 商品流拍，之前商品置为无效，新生成一个商品
             */
            if(GoodsStatusEnum.AUCTION_FAIL.getCode().equals(goodsStatus)){
                Long id = auctionGoods.getId();
                //插入商品
                auctionGoodsMapper.insertById(auctionGoods);
                //插入作品图
                AuctionPic auctionPic = new AuctionPic();
                auctionPic.setAuctionId(id);
                auctionPic.setNextAuctionId(auctionGoods.getId());
                auctionPicMapper.insertByGoodsId(auctionPic);
                //设置之前的商品为 重置商品
                AuctionGoods ag = new AuctionGoods();
                ag.setId(id);
                ag.setResetStatus(0);
                auctionGoodsMapper.updateById(ag);
            }
        }
        //更新拍品
        int id = auctionGoodsMapper.updateById(auctionGoods);
        //更新作品图
        auctionPicMapper.deleteByAuctionId(Arrays.asList(auctionGoods.getId()));
        insertAuctionPics(auctionGoods);
        return id>0 ? true :false ;
    }

    @Override
    public AuctionGoodsVo selectById(AuctionGoodsReq auctionGoodsreq, boolean isH5) {
        if(auctionGoodsreq == null || auctionGoodsreq.getId() == null){
            throw new AuctionGoodsException("查询拍品商品id为空");
        }
        AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectById(auctionGoodsreq);
        if(auctionGoodsVo == null){
            return null;
        }
        auctionGoodsVo.setShowPics(Arrays.asList(auctionGoodsVo.getShowPic()));
        auctionGoodsVo.setCredentialUrls(Arrays.asList(auctionGoodsVo.getCredentialUrl()));
        auctionGoodsVo.setWarrantUrls(Arrays.asList(auctionGoodsVo.getWarrantUrl()));
        AuctionPic auctionPic = new AuctionPic();
        auctionPic.setAuctionId(auctionGoodsreq.getId());
        List<AuctionPicVo> list = auctionPicMapper.selectByAuctionId(auctionPic);
        if(list != null && list.size() > 0){
            auctionGoodsVo.setAuctionPics(list.stream().map(auctionPicVo -> auctionPicVo.getWorksUrl()).collect(Collectors.toList()));
        }
        if(isH5){
            List<AuctionProcess> processes = auctionProcessMapper.selectByIds(Arrays.asList(auctionGoodsreq.getId()));
            if(processes != null && processes.size() > 0){
                AuctionProcess process = processes.get(0);
                auctionGoodsVo.setCurrentPrice(process.getCurrentPrice());
                auctionGoodsVo.setUsername(process.getUsername());
            }
            //商品状态为4返回订单购买人和支付时间
            if(GoodsStatusEnum.AUCTION_SALE.getCode().equals(auctionGoodsVo.getGoodsStatus())){
                AuctionOrderReq req = new AuctionOrderReq();
                req.setGoodsId(auctionGoodsVo.getId());
                AuctionOrder auctionOrder = auctionOrderService.selectByGoodsId(req);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                auctionGoodsVo.setBuyerName(auctionOrder.getBuyerName());
                auctionGoodsVo.setPayDate(sdf.format(auctionOrder.getPayDate()));
            }
        }
        String username = auctionGoodsreq.getMobile();
        if(StringUtils.isNotBlank(username)){
            AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
            auctionProxyLimit.setGoodsId(auctionGoodsreq.getId());
            auctionProxyLimit.setUsername(auctionGoodsreq.getMobile());
            AuctionProxyLimit limit = auctionProxyLimitMapper.selectOneByUserAndGoodId(auctionProxyLimit);
            auctionGoodsVo.setPriceLimit((limit != null) ? limit.getPriceLimit() : new BigDecimal(0));
        }
        auctionGoodsVo.setCurrentPrice(auctionGoodsVo.getCurrentPrice()==null?new BigDecimal(0):auctionGoodsVo.getCurrentPrice());
        auctionGoodsVo.setCurrentTime(new Date());
        setInterestCount(GoodsEnum.AUCTION, auctionGoodsreq.getId(), auctionGoodsreq.getMobile());
        return auctionGoodsVo;
    }

    @Override
    public Map<String, Object> getList(AuctionGoodsReq auctionGoodsreq) {
        Page page = new Page();
        setPrePageInfo(page, auctionGoodsreq);
        List<AuctionGoodsVo> list = auctionGoodsMapper.getList(auctionGoodsreq);
        return setAfterPageInfo(list, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsStatusToInAction() {
        try{
            AuctionGoodsShedule auctionGoodsShedule = new AuctionGoodsShedule();
            auctionGoodsShedule.setCurrentTime(new Date());
            auctionGoodsShedule.setPreGoodsStatus("1");
            auctionGoodsShedule.setCurrentGoodsStatus("2");
            auctionGoodsShedule.setGoodsStatusRemark("batch update goods status 1 to 2");
            //修改商品状态，未开始1->拍卖中2
            int count = auctionGoodsMapper.updateGoodsStatusToInAction(auctionGoodsShedule);
            //查询商品对应代理出价且有效（proxyStatus=1）的参与者

            log.info("updateGoodsStatusToInAction success count:{}", count);
        }catch(Exception e){
            log.error("updateGoodsStatusToInAction error,e:{}", e);
        }
    }

    @Override
    public AuctionGoodsVo getAuctionGoods(Long goodsId){
        AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
        auctionGoodsreq.setId(goodsId);
        AuctionGoodsVo auctionGoods = auctionGoodsMapper.selectOneById(auctionGoodsreq);
        if(auctionGoods == null){
            throw new AuctionGoodsException("商品不存在，id:" + goodsId);
        }
        return auctionGoods;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateGoodsStatusToActionSuccess() {
        try{
            synchronized(lockTwo){
                //查询拍卖中的商品ids
                AuctionGoodsShedule auctionGoodsShedule = new AuctionGoodsShedule();
                auctionGoodsShedule.setCurrentTime(new Date());
                auctionGoodsShedule.setPreGoodsStatus("2");
                List<Long> goodIds = auctionGoodsMapper.selectGoodsStatusInAction(auctionGoodsShedule);
                if(goodIds == null || goodIds.size() == 0){
                    log.info("updateGoodsStatusToActionSuccess ids count is 0");
                    return;
                }
                //查询拍品的流程信息
                List<Long> processIds = auctionProcessMapper.selectProcessByIds(goodIds);
                List<AuctionGoodsShedule> params = new ArrayList<>();
                Date currentTime = new Date();
                if(processIds == null || processIds.size() == 0){
                    //所有商品流拍0了，不生成订单
                    for(Long goodsId : goodIds){
                        AuctionGoodsShedule shedule = new AuctionGoodsShedule();
                        shedule.setId(goodsId);
                        shedule.setCurrentTime(currentTime);
                        shedule.setPreGoodsStatus("2");
                        shedule.setCurrentGoodsStatus("0");
                        shedule.setGoodsStatusRemark("batch update goods status 2 to 0");
                        params.add(shedule);
                        //退保证金
                        backBaseAmount(goodsId, null);
                    }
                    int count = auctionGoodsMapper.batchUpdateGoodsStatusToActionSuccess(params);
                    log.info("updateGoodsStatusToActionSuccess success count:{}", count);
                    return;
                }
                for(Long goodsId : goodIds){
                    AuctionGoodsShedule shedule = new AuctionGoodsShedule();
                    shedule.setId(goodsId);
                    shedule.setCurrentTime(currentTime);
                    shedule.setPreGoodsStatus("2");
                    if(processIds.contains(goodsId)){
                        //1、竞拍成功3
                        shedule.setCurrentGoodsStatus("3");
                        shedule.setGoodsStatusRemark("batch update goods status 2 to 3");
                        //2、订单表没有对应商品的订单，根据流程表生成订单
                        AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
                        auctionOrderReq.setGoodsId(goodsId);
                        List<AuctionOrder> orders = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
                        if(orders == null || orders.size() == 0){
                            //生成订单
                            auctionOrderReq.setOrderNo(IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.PP));
                            auctionOrderReq.setBuyerDate(new Date());
                            auctionOrderMapper.insertByProcess(auctionOrderReq);
                            //更新收货人信息
                            Long orderId = auctionOrderReq.getId();
                            if(orderId != null){
                                auctionOrderMapper.updateReceiveInfoByProcess(orderId);
                            }
                            //退其余参拍者保证金
                            backBaseAmount(goodsId, auctionProcessService.getAuctionProcess(goodsId).getUsername());
                        }
                    }else{
                        //流拍0，不生成订单
                        shedule.setCurrentGoodsStatus("0");
                        shedule.setGoodsStatusRemark("batch update goods status 2 to 0");
                        //退保证金
                        backBaseAmount(goodsId, null);
                    }
                    params.add(shedule);
                }
                int count = auctionGoodsMapper.batchUpdateGoodsStatusToActionSuccess(params);
                log.info("updateGoodsStatusToActionSuccess success count:{}", count);
                return;
            }
        }catch(Exception e){
            log.error("updateGoodsStatusToActionSuccess error,e:{}", e);
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateProxyPrice(Long goodsId, List<AuctionProxyLimit> proxys) {
        if(goodsId == null || proxys == null || proxys.size() == 0){
            return;
        }
        try{
            synchronized(LockConstants.getLock(goodsId)){
                //更新商品自动代理状态为已执行0
                AuctionGoods goods = new AuctionGoods();
                goods.setId(goodsId);
                goods.setAutoProxy(0);
                auctionGoodsMapper.updateById(goods);
                //查询商品信息
                AuctionGoodsVo auctionGoods = getAuctionGoods(goodsId);
                //加价幅度
                BigDecimal incrementStep = auctionGoods.getIncrementStep();
                //查询商品订单是否生成
                AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
                auctionOrderReq.setGoodsId(goodsId);
                List<AuctionOrder> list = auctionOrderMapper.selectByGoodsId(auctionOrderReq);
                if(list != null && list.size() > 0){
                    log.info("goods order already exists,goodId:{}", goodsId);
                    proxys = proxys.stream().filter(proxy -> {
                        proxy.setRemark("goods order already exists,goodId:" + goodsId);
                        return true;
                    }).collect(Collectors.toList());
                    proxys = proxys.stream().sorted(Comparator.comparing(AuctionProxyLimit::getPriceLimit).reversed()).collect(Collectors.toList());
                    auctionProxyLimitMapper.batchUpdateProxyStatusToValid(proxys.get(0));
                    return;
                }
                /**
                 * 判断是否已执行过代理出价批处理,通过字段判断 t_auction_goods字段auto_proxy,在查询的时候限制
                 */
                /*AuctionProcess auctionProcess = auctionProcessService.getAuctionProcess(goodsId);
                if(auctionProcess != null){
                    proxys = proxys.stream().filter(proxy -> {
                        proxy.setRemark("proxy price batch already execute,current processId:" + auctionProcess.getId());
                        return true;
                    }).collect(Collectors.toList());
                    //更新代理出价表状态为失效
                    auctionProxyLimitMapper.batchUpdateProxyStatus(proxys);
                    log.info("updateProxyPrice success goodsId:{}, proxy price batch already execute,current processId::{}", goodsId, auctionProcess.getId());
                    return;
                }*/

                /**
                 * 判断是否大于等于一口价
                 */
                //设置代理状态为失效 0
                proxys = proxys.stream().filter(proxy -> {
                    proxy.setProxyStatus(0);
                    return true;
                }).collect(Collectors.toList());
                BigDecimal onePrice = auctionGoods.getOnePrice();
                boolean geOnePriceFlag = false;
                // 代理出价不受一口价限制
                /*if(auctionGoods.getOnePrice() != null){
                    proxys = proxys.stream().sorted(Comparator.comparing(AuctionProxyLimit::getCreateTime)).collect(Collectors.toList());
                    for(AuctionProxyLimit proxy:proxys){
                        //按时间排序第一个代理价格大于一口价参拍者领先，当前价格为代理价格+加价幅度+运费，生成订单，其他参拍者均出局
                        if(proxy.getPriceLimit().compareTo(onePrice) >= 0){
                            //生成订单
                            AuctionOrder order = new AuctionOrder();
                            order.setOrderNo(IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.PP));
                            order.setGoodsId(goodsId);
                            order.setGoodsName(auctionGoods.getGoodsName());
                            order.setBuyerName(proxy.getUsername());
                            BigDecimal transportAmount = auctionGoods.getTransportAmount();
                            order.setDealPrice(proxy.getPriceLimit().add(incrementStep).add(transportAmount ==null ? new BigDecimal(0):transportAmount));
                            order.setBuyerDate(new Date());
                            order.setStyleCode(auctionGoods.getStyleCode());
                            order.setAddrId(proxy.getAddrId());
                            //设置用户收货信息
                            Long addrId = proxy.getAddrId();
                            if(addrId != null){
                                ReceiveAddress req = new ReceiveAddress();
                                req.setId(addrId);
                                ReceiveAddress receiveAddress = receiveAddressMapper.selectById(req);
                                order.setReceiveAddr(receiveAddress.getCnAddress()+receiveAddress.getDetailedAddress());
                                order.setReceiveContact(receiveAddress.getReceiverName());
                                order.setReceivePhone(receiveAddress.getReceiverMobile());
                            }
                            auctionOrderMapper.insertByProxy(order);
                            //修改商品状态 拍卖中2->拍卖成功3
                            updateGoodsStatusToActionSuccess(goodsId, "proxyPrice update goods status 2 to 3");
                            geOnePriceFlag = true;
                            //设置领先标志
                            proxy.setLeaderStatus(true);
                            break;
                        }
                    }
                }*/
                //代理出价没有超过一口价，代理出价高者最高者领先
                if(!geOnePriceFlag){
                    proxys = proxys.stream().sorted(Comparator.comparing(AuctionProxyLimit::getPriceLimit).reversed()).collect(Collectors.toList());
                    //设置领先标志
                    proxys.get(0).setLeaderStatus(true);
                }
                //插入流程表
                List<AuctionProcess> processList = new ArrayList<>();
                Long leaderId = null;
                for(AuctionProxyLimit proxy:proxys){
                    if(proxy.getLeaderStatus()){
                        leaderId = proxy.getId();
                    }
                }
                for(AuctionProxyLimit proxy:proxys){
                    AuctionProcess process = new AuctionProcess();
                    process.setGoodsId(auctionGoods.getId());
                    process.setGoodsName(auctionGoods.getGoodsName());
                    process.setUsername(proxy.getUsername());
                    process.setCurrentPrice(proxy.getLeaderStatus() ? (proxys.size() > 1 ? proxys.get(1).getPriceLimit().add(auctionGoods.getIncrementStep()) : proxy.getPriceLimit()) : proxy.getPriceLimit());
                    process.setAddPriceType(AddPriceTypeEnum.PROXY.getCode());
                    process.setRemark(proxy.getLeaderStatus() ? "proxy price is leader id="+leaderId : "proxy price is loser,leader id is:" + leaderId);
                    proxy.setRemark(proxy.getLeaderStatus() ? "proxy price is leader id=" +leaderId : "proxy price is loser,leader id is:" + leaderId);
                    //状态: 1、出局，2、领先
                    process.setProcessStatus(proxy.getLeaderStatus() ? "2" : "1");
                    processList.add(process);
                }
                //领先的最后插入来保证id最大
                processList = processList.stream().sorted(Comparator.comparing(AuctionProcess::getProcessStatus)).collect(Collectors.toList());

                auctionProcessMapper.batchInsert(processList);
                //更新代理出价表状态为失效
                AuctionProxyLimit auctionProxyLimit = new AuctionProxyLimit();
                auctionProxyLimit.setGoodsId(goodsId);
                auctionProxyLimit.setUsername(processList.get(processList.size() - 1).getUsername());
                int count = processList.size() > 0 ? auctionProxyLimitMapper.batchUpdateProxyStatusToValid(auctionProxyLimit) : 0;
                log.info("updateProxyPrice success goodsId:{}, count:{}", goodsId, count);
            }
        }catch(Exception e){
            log.error("updateProxyPrice error goodsId:{}, error:{}", goodsId, e);
        }
    }

    @Override

    public  Map<String, Object> getAuctionGoodList(Map<String, Object> params) {
        String name = (String) params.get("name");
        Integer pageNum = Integer.valueOf(String.valueOf(params.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        PageHelper.startPage(pageNum, pageSize);
        List<CategoryConfig> categoryConfigList = categoryConfigService.selectById(name);
        for (int i = 0; i < categoryConfigList.size(); i++) {
            CategoryConfig categoryConfig = categoryConfigList.get(0);
            params.put("styleCode", categoryConfig.getId());
        }
        List<AuctionGoods> auctionGoodList = auctionGoodsMapper.getAuctionGoodList(params);
        PageInfo<AuctionGoods> pageInfo = new PageInfo<>(auctionGoodList);
        int pages = Page.getPages(pageInfo.getTotal(), pageSize);
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", auctionGoodList);
        return result;
    }

    @Override
    public Map<String, Object> getH5Goods(H5AuctionGoodsReq req) {
        String dayType = req.getDayType();
        if(StringUtils.isBlank(dayType)){
            throw new AuctionGoodsException("日期类型为空");
        }
        String timeType = req.getTimeType();
        if(StringUtils.isBlank(timeType)){
            throw new AuctionGoodsException("时间类型为空");
        }

        List<H5AuctionGoodsVo> list;
        Page page = new Page();
        setPrePageInfo(page, req);
        //1、今日 2、明日 0、历史
        if(StringUtils.equalsAny(dayType, new String[]{"1","2"})){
            list = getTodayAndTomorrowTime(dayType, timeType);
        }else if(StringUtils.equals(dayType, "0")){
            list = getHistoryTime(timeType);
        }else{
            throw new AuctionGoodsException("非法的日期类型");
        }
        PageInfo<H5AuctionGoodsVo> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        if(list != null && list.size() > 0){
            List<Long> ids = list.stream().map(vo -> vo.getId()).collect(Collectors.toList());
            List<AuctionProcess> processes = auctionProcessMapper.selectByIds(ids);
            if(processes != null && processes.size() > 0){
              for(H5AuctionGoodsVo good: list){
                  for(AuctionProcess process: processes){
                      if(good.getId().equals(process.getGoodsId())){
                          good.setCurrentPrice(process.getCurrentPrice());
                          good.setUsername(process.getUsername());
                      }
                  }
                  //商品状态为4返回订单购买人和支付时间
                  if(GoodsStatusEnum.AUCTION_SALE.getCode().equals(good.getGoodsStatus())){
                      AuctionOrderReq auctionOrderReq = new AuctionOrderReq();
                      auctionOrderReq.setGoodsId(good.getId());
                      AuctionOrder auctionOrder = auctionOrderService.selectByGoodsId(auctionOrderReq);
                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                      good.setBuyerName(auctionOrder.getBuyerName());
                      good.setPayDate(sdf.format(auctionOrder.getPayDate()));
                  }
                  setInterestCount(GoodsEnum.AUCTION, good.getId(), req.getMobile());
              }
            }
        }
        //list = list.stream().sorted(Comparator.comparing(H5AuctionGoodsVo::getCreateTime).reversed()).collect(Collectors.toList());
        Date currenTime = new Date();
        list = list.stream().filter(vo->{
            if(vo.getEndDate().compareTo(currenTime)>0){
                vo.setFinished(1);
            }else{
                vo.setFinished(2);
            }
            return true;
        }).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(H5AuctionGoodsVo::getFinished).thenComparing(H5AuctionGoodsVo::getEndDate)).collect(Collectors.toList());
        Map<String, Object> result = setAfterPageInfo(list, page);
        result.put("total", total);
        result.put("pages", pages);
        return result;
    }

    @Override
    public Map<String, Object> getH5GoodsForToday(H5AuctionGoodsReq req) {
        String timeType = req.getTimeType();
        if(StringUtils.isBlank(timeType)){
            throw new AuctionGoodsException("时间类型为空");
        }
        List<H5AuctionGoodsVo> list = getTodayAndTomorrowTime("1", timeType);
        if(list != null && list.size() > 0){
            List<Long> ids = list.stream().map(vo -> vo.getId()).collect(Collectors.toList());
            List<AuctionProcess> processes = auctionProcessMapper.selectByIds(ids);
            if(processes != null && processes.size() > 0){
                for(H5AuctionGoodsVo good: list){
                    for(AuctionProcess process: processes){
                        if(good.getId().equals(process.getGoodsId())){
                            good.setCurrentPrice(process.getCurrentPrice());
                            good.setUsername(process.getUsername());
                        }
                    }
                    setInterestCount(GoodsEnum.AUCTION, good.getId(), req.getMobile());
                }
            }
        }
        Date currenTime = new Date();
        list = list.stream().filter(vo->{
            if(vo.getEndDate().compareTo(currenTime)>0){
                vo.setFinished(1);
            }else{
                vo.setFinished(2);
            }
            return true;
        }).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(H5AuctionGoodsVo::getFinished).thenComparing(H5AuctionGoodsVo::getEndDate)).collect(Collectors.toList());
        Map<String, Object> result = new HashMap();
        result.put("currentTime", new Date());
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> getH5GoodsCount() {
        // 今天
        Map<String, Date> params = new HashMap<>(1 << 4);
        Date currentTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        getTime("0", params, cal, 0,0);
        int todayCount = auctionGoodsMapper.todayCount(params);
        //明日
        Map<String, Date> tomorrowParams = new HashMap<>(1 << 4);
        cal.add(Calendar.DATE, 1);
        getTomorrowTime("0", tomorrowParams, cal, 0);
        int tomorrowCount = auctionGoodsMapper.preCount(tomorrowParams);
        // 历史所有
        Map<String, Object> allParams = new HashMap<>(1 << 4);
        allParams.put("currentTime", new Date());
        int historyCount = auctionGoodsMapper.historyCount(allParams);
        Map<String, Object> map = new HashMap<>();
        map.put("todayCount", todayCount);
        map.put("tomorrowCount", tomorrowCount);
        map.put("historyCount", historyCount);
        return map;
    }



    private List<H5AuctionGoodsVo> getHistoryTime(String timeType){
        Map<String, Object> params = new HashMap<>(1 << 4);
        switch(timeType){
            //全部
            case "0":
                params.put("currentTime", new Date());
                break;
            //T-3月
            case "1":
                getHistoryTime(timeType, -3 ,params);
                break;
            //T-2月
            case "2":
                getHistoryTime(timeType, -2, params);
                break;
            //T-1月
            case "3":
                getHistoryTime(timeType, -1, params);
                break;
            //T 当月
            case "4":
                getHistoryTime(timeType, 0, params);
                break;
            default:
                throw new AuctionGoodsException("非法的时间类型");
        }
        return auctionGoodsMapper.selectHistoryGoods(params);
    }

    private void getHistoryTime(String timeType, int minusTime, Map<String, Object> params){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, minusTime);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        params.put("startTime", new Date(calendar.getTimeInMillis()));
        calendar.add(Calendar.MONTH, 1);
        params.put("endTime", new Date(calendar.getTimeInMillis()));
        params.put("currentTime", date);
        params.put("timeType", timeType);
    }

    private List<H5AuctionGoodsVo> getTodayAndTomorrowTime(String dayType, String timeType){
        Date currentTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Map<String, Date> params = new HashMap<>(1 << 4);
        //拍品预展
        if(dayType.equals("2")){
            cal.add(Calendar.DATE, 1);
            switch(timeType){
                    //全部
                case "0":
                    getTomorrowTime(timeType, params, cal,0);
                    break;
                    //7天内
                case "1":
                    getTomorrowTime(timeType, params, cal, 7);
                    break;
                    //14天内
                case "2":
                    getTomorrowTime(timeType, params, cal,14);
                    break;
                    //21天内
                case "3":
                    getTomorrowTime(timeType, params, cal,21);
                    break;
                    //28天内
                case "4":
                    getTomorrowTime(timeType, params, cal,28);
                    break;
                default:
                    throw new AuctionGoodsException("非法的日期类型");
            }
            return auctionGoodsMapper.selectPreGoods(params);
        }else{
            switch(timeType){
                //全部
                case "0":
                    getTime(timeType, params, cal, 0,0);
                    break;
                //0-6
                case "1":
                    getTime(timeType, params, cal, 0,6);
                    break;
                //6-9
                case "2":
                    getTime(timeType, params, cal, 6,9);
                    break;
                //9-14
                case "3":
                    getTime(timeType, params, cal, 9,14);
                    break;
                //14-15
                case "4":
                    getTime(timeType, params, cal, 14,15);
                    break;
                //15-17
                case "5":
                    getTime(timeType, params, cal, 15,17);
                    break;
                //17-20
                case "6":
                    getTime(timeType, params, cal, 17,20);
                    break;
                //20-22
                case "7":
                    getTime(timeType, params, cal, 20,22);
                    break;
                //22-24
                case "8":
                    getTime(timeType, params, cal, 22,24);
                    break;
                default:
                    throw new AuctionGoodsException("非法的日期类型");
            }
            return auctionGoodsMapper.selectTodayAndTomorrowGoods(params);
        }

    }

    private void getTime(String timeType, Map<String, Date> params, Calendar cal, Integer startTime, Integer endTime){
        //全部
        if(StringUtils.equals(timeType,"0" )){
            //params.put("startTime", new Date(cal.getTimeInMillis()));
            params.put("startTime", null);
            cal.add(Calendar.DATE, 1);
            params.put("endTime", new Date(cal.getTimeInMillis()));
            return;
        }
        cal.set(Calendar.HOUR_OF_DAY, startTime);
        //params.put("startTime", new Date(cal.getTimeInMillis()));
        params.put("startTime", null);
        cal.set(Calendar.HOUR_OF_DAY, endTime);
        params.put("endTime", new Date(cal.getTimeInMillis()));
    }

    private void getTomorrowTime(String timeType, Map<String, Date> params, Calendar cal, Integer endTime){
        //全部
        if(StringUtils.equals(timeType,"0" )){
            params.put("startTime", new Date(cal.getTimeInMillis()));
            return;
        }
        params.put("startTime", new Date(cal.getTimeInMillis()));
        cal.add(Calendar.DAY_OF_MONTH, endTime);
        params.put("endTime", new Date(cal.getTimeInMillis()));
    }

    @Override
    public void updateGoodsStatusToActionSuccess(Long goodsId, String remark){
        AuctionGoodsShedule shedule = new AuctionGoodsShedule();
        shedule.setId(goodsId);
        shedule.setCurrentTime(new Date());
        //拍卖中2
        shedule.setPreGoodsStatus("2");
        //竞拍成功3
        shedule.setCurrentGoodsStatus("3");
        shedule.setGoodsStatusRemark(remark);
        auctionGoodsMapper.batchUpdateGoodsStatusToActionSuccess(Arrays.asList(shedule));
    }

    private void transPic(AuctionGoods auctionGoods){
        List showPics = auctionGoods.getShowPics();
        if(showPics!=null && showPics.size() > 0 ){
            Object obj = showPics.get(0);
            auctionGoods.setShowPic(obj == null ? null :String.valueOf(obj));
        }
    }

    private void insertAuctionPics(AuctionGoods auctionGoods){
        List<String> pics = auctionGoods.getAuctionPics();
        if(pics != null && pics.size() > 0){
            List<AuctionPic> list = new ArrayList<>();
            for(String pic : pics){
                AuctionPic auctionPic = new AuctionPic();
                auctionPic.setAuctionId(auctionGoods.getId());
                auctionPic.setWorksUrl(pic);
                list.add(auctionPic);
            }
            auctionPicMapper.insertByAuctionId(list);
        }
    }

    /**
     * 退还商品流拍保证金或退还除生成订单外参与者的保证金
     * @param goodsId 商品id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backBaseAmount(Long goodsId, String username){
        if(goodsId == null){
            return;
        }
        AuctionBaseAmount auctionBaseAmount = new AuctionBaseAmount();
        auctionBaseAmount.setBaseAmountStatus(0);
        auctionBaseAmount.setGoodsId(goodsId);
        auctionBaseAmount.setUpdateTime(new Date());
        if(StringUtils.isBlank(username)){
            //商品流拍：退还所有参与拍卖的保证金
            weChatUserMapper.backUserBaseAmount(goodsId);
            auctionBaseAmountMapper.updateBoneStatus(auctionBaseAmount);
        }else{
            //退还没有参拍成功参与者的保证金
            Map map = new HashMap();
            map.put("goodsId", goodsId);
            map.put("username", username);
            weChatUserMapper.backUserBaseAmountWithoutSuccess(map);
            auctionBaseAmount.setUsername(username);
            auctionBaseAmountMapper.updateBoneStatusWithoutSuccess(auctionBaseAmount);
        }
        //记录退还保证金记录
        paymentResultMapper.insertByBaseAmount(auctionBaseAmount);
    }

    /**
     * 获取商品保证金和用户余额
     */
    @Override
    public Map<String, Object> getBalanceAmount(Map<String, Object> params) {
                String id = (String) params.get("id");
                String mobile = (String) params.get("mobile");
                BigDecimal baseAmount = auctionGoodsMapper.getBaseAmount(id);
                String balance = weChatUserMapper.getBalance(mobile);
                HashMap<String, Object> map = new HashMap<>();
                map.put("baseAmount",baseAmount);
                map.put("balance",balance);
                return map;
    }
}
