package com.art.manager.service.impl;

import com.art.manager.config.wechat.WechatConfig;
import com.art.manager.enums.GoodsEnum;
import com.art.manager.enums.OrderNoPrefixEnum;
import com.art.manager.enums.PayTypeEnum;
import com.art.manager.enums.PaymentTypeEnum;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.OrderMapper;
import com.art.manager.mapper.auction.AuctionBaseAmountMapper;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.wechat.PaymentResultMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.wechat.PaymentResult;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.PaymentResultReq;
import com.art.manager.request.WechatUserReq;
import com.art.manager.service.JSAPIPayService;
import com.art.manager.service.OrderService;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.service.auction.AuctionOrderService;
import com.art.manager.util.HttpUtil;
import com.art.manager.util.IdUtils;
import com.art.manager.util.JSAPIPayUtil;
import com.art.manager.vo.AuctionGoodsVo;
import com.art.manager.vo.WeChatUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JSAPIPayServiceImpl implements JSAPIPayService {

    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private PaymentResultMapper paymentResultMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CommonCommodityMapper commodityMapper;
    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;
    @Autowired
    private AuctionOrderService auctionOrderService;
    @Autowired
    private AuctionGoodsService auctionGoodsService;

    private static final Long TOKEN_TIMEOUT = 1L;
    @Override
    public Msg pay(WechatUserReq wechatUserReq) throws Exception {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String flag = operations.get(String.valueOf(wechatUserReq.getId()));
        log.info("flag:{}"+ flag);
        boolean isBuy = true;
        if(StringUtils.isNotBlank(flag) && flag.equals("1")){ // 1:说明这个商品正在被支付
            isBuy = false;
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("isBuy", isBuy);
            return new Msg(Msg.FAILURE_CODE, objectMap);
        }
        operations.set(String.valueOf(wechatUserReq.getId()), "1", TOKEN_TIMEOUT, TimeUnit.MINUTES);
        if(StringUtils.isBlank(wechatUserReq.getMobile())){
            throw new RuntimeException("手机号为空");
        }
        WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(wechatUserReq.getMobile(), null);
        if(weChatUserVo == null){
            throw new RuntimeException("用户信息为空");
        }
        if(StringUtils.isBlank(weChatUserVo.getOpenid())){
            return new Msg(Msg.OPENID_NULL, "微信未登陆");
        }
        CommonCommodity commonCommodity = commodityMapper.getCommonCommodityById(wechatUserReq.getId());
        if(commonCommodity == null){
            AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
            auctionGoodsreq.setId(wechatUserReq.getId());
            AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectOneById(auctionGoodsreq);
            if(auctionGoodsVo == null){
                throw new RuntimeException("无此商品信息");
            }
        }else{
            Integer stock = commonCommodity.getStock();
            //commodityMapper.updateStock(wechatUserReq.getId(), stock-1, new Date());
            if(commonCommodity.getStock() == 0){
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("stock", commonCommodity.getStock());
                return new Msg(Msg.FAILURE_CODE, objectMap);
            }
        }
        String body = "";
        Map<String, String> treeMap = new HashMap<>();
        treeMap.put("appid", wechatConfig.getAppid());
        treeMap.put("mch_id", wechatConfig.getMchId());
        treeMap.put("device_info", wechatConfig.getDeviceInfo());
        treeMap.put("nonce_str", JSAPIPayUtil.generateNonceStr());
        treeMap.put("trade_type", "JSAPI");
        treeMap.put("notify_url", wechatConfig.getNotifyUrl());// 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
        String orderNo = "";
        if(wechatUserReq.getType() == 1){
            orderNo = wechatUserReq.getOrderNo();
            body = "瀚华艺术-" + wechatUserReq.getSpecialName();
        } else if (wechatUserReq.getType() == 2){
            orderNo = IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.CZ);
            body = "瀚华艺术-充值";
        }
        treeMap.put("body", body);
        treeMap.put("out_trade_no", orderNo); // 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
        treeMap.put("total_fee", String.valueOf(wechatUserReq.getAmount()));// 订单总金额，单位为分
        treeMap.put("openid", weChatUserVo.getOpenid());
        String xml = JSAPIPayUtil.generateSignedXml(treeMap, wechatConfig.getMerchantKey());
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        log.info("mobile:{},requestXml:{}", wechatUserReq.getMobile(), xml);
        String result = restTemplate.postForObject(wechatConfig.getJSPAPIUrl(), xml, String.class);
        log.info("mobile:{},responseXml:{}", wechatUserReq.getMobile(), result);
        Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(result);
        log.info("mobile:{},xmlToMap:{}", wechatUserReq.getMobile(), resultMap);
        //Map<String, String> resultMap = JSAPIPayUtil.webserviceRequest(xml, wechatConfig.getJSPAPIUrl());
       /* PaymentResult paymentResult = assemblePaymentResult(resultMap);
        paymentResultMapper.savePaymentResult(paymentResult);*/
        String result_code = resultMap.get("result_code");
        String return_code = resultMap.get("return_code");
        operations.set(orderNo, wechatUserReq.getMobile());
        operations.set(treeMap.get("nonce_str"), String.valueOf(wechatUserReq.getId()));
        if(return_code.equals("SUCCESS") && result_code.equals("SUCCESS")){
            Map<String, String> map = JSAPIPayUtil.xmlToMap(xml);
            String prepay_id = resultMap.get("prepay_id");
            //返回给APP端的参数，APP端再调起支付接口
            Map<String,String> repData = new HashMap<>();
            repData.put("appId",wechatConfig.getAppid());
            // repData.put("mch_id",wechatConfig.getMchId());
            repData.put("package","prepay_id=" + prepay_id);
            repData.put("nonceStr",resultMap.get("nonce_str"));
            repData.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
            repData.put("signType", "MD5");
            String sign = JSAPIPayUtil.generateSignature(repData, wechatConfig.getMerchantKey()); //签名
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("paySign",sign);
            reqMap.put("timeStamp",repData.get("timeStamp"));
            reqMap.put("package","prepay_id=" + prepay_id);
            reqMap.put("nonceStr", resultMap.get("nonce_str"));
            reqMap.put("appId", wechatConfig.getAppid());
            reqMap.put("signType", "MD5");
            reqMap.put("stock", commonCommodity == null  ? 0: commonCommodity.getStock());
            reqMap.put("isBuy", isBuy);
            log.info("mobile:{},resultMap:{}", wechatUserReq.getMobile(), reqMap);
            return new Msg(Msg.SUCCESS_CODE,reqMap);
        } else {
            String err_code_des = resultMap.get("err_code_des");
            return new Msg(Msg.FAILURE_CODE, err_code_des);
        }
    }

    /**
     * 微信退款
     * @param req
     * @return
     */
    @Override
    public Msg refund(WechatUserReq req) throws Exception {
        String orderNo = req.getOrderNo();
        PaymentResult paymentResult = paymentResultMapper.selectPayResultByOrderNo(orderNo);
        HashMap<String, String> refundMap = new HashMap<>();
        refundMap.put("appid", wechatConfig.getAppid());
        refundMap.put("mch_id", wechatConfig.getMchId());
        refundMap.put("nonce_str", JSAPIPayUtil.generateNonceStr());
        refundMap.put("transaction_id",paymentResult.getTransactionId());
        refundMap.put("out_refund_no", orderNo);
        refundMap.put("total_fee", String.valueOf(req.getAmount()));
        refundMap.put("refund_fee", String.valueOf(req.getRefundAmount()));
        refundMap.put("notify_url", wechatConfig.getRefundNotifyUrl());

        String xml = JSAPIPayUtil.generateSignedXml(refundMap, wechatConfig.getMerchantKey());
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        String result = restTemplate.postForObject(wechatConfig.getRefund(), xml, String.class);
        Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(result);
        PaymentResult refundResult = new PaymentResult();
        refundResult.setReturnCode(resultMap.get("return_code"));
        refundResult.setReturnMsg(resultMap.get("return_msg"));
        refundResult.setResultCode(resultMap.get("result_code"));
        refundResult.setErrCode(resultMap.get("err_code"));
        refundResult.setErrCodeDes(resultMap.get("err_code_des"));
        refundResult.setMchId(resultMap.get("mch_id"));
        refundResult.setTransactionId(resultMap.get("transaction_id"));
        refundResult.setOutRefundNo(resultMap.get("out_refund_no"));
        refundResult.setOutTradeNo(resultMap.get("out_trade_no"));
        refundResult.setRefundId(resultMap.get("refund_id"));
        refundResult.setTotalFee(new BigDecimal(resultMap.get("settlement_refund_fee")));
        refundResult.setPaymentType(PaymentTypeEnum.REFUND.getCode());
        refundResult.setRefundStatus(1);
        paymentResultMapper.savePaymentResult(refundResult);
        if(resultMap.get("return_code").equals("FAIL")){
            return new Msg(Msg.FAILURE_CODE, resultMap.get("return_msg"));
        }
        return new Msg(Msg.SUCCESS_CODE, "退款中");
    }

    @Override
    public int savePayment(Map<String, String> resultMap) {
        PaymentResult paymentResult = assemblePaymentResult(resultMap);
        return paymentResultMapper.savePaymentResult(paymentResult);
    }

    @Override
    public Map<String, Object> getTransactionDetails(PaymentResultReq req) {
        if(req == null || StringUtils.isBlank(req.getMobile())){
            throw new RuntimeException("手机号为空");
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        String status = req.getStatus();
        if(StringUtils.isNotBlank(status)){
            if(status.equals("1")){
                //收入
                req.setPaymentTypes(Arrays.asList(PaymentTypeEnum.DEPOSIT.getCode(), PaymentTypeEnum.BASE_AMOUNT_BACK.getCode()));
            }else if(status.equals("2")){
                //支出
                req.setPaymentTypes(Arrays.asList(PaymentTypeEnum.WX_PAY_ORDER.getCode(), PaymentTypeEnum.BALANCE_PAY_ORDER.getCode(),
                        PaymentTypeEnum.WX_PAY_BASE_AMOUNT.getCode(),PaymentTypeEnum.BALANCE_PAY_BASE_AMOUNT.getCode()));
            }
        }
        List<PaymentResult> paymentResultList = paymentResultMapper.selectPayResult(req);
        PageInfo<PaymentResult> pageInfo = new PageInfo<>(paymentResultList);
        int pages = Page.getPages(pageInfo.getTotal(), req.getPageSize());
        Map<String, Object> result = new HashMap();
        result.put("total", pageInfo.getTotal());
        result.put("pages", pages);
        result.put("list", paymentResultList);
        return result;
    }

    @Transactional
    @Override
    public Msg balancePay(WechatUserReq req) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String flag = operations.get(String.valueOf(req.getId()));
        log.info("flag:{}"+ flag);
        boolean isBuy = true;
        if(StringUtils.isNotBlank(flag) && flag.equals("1")){ // 1:说明这个商品正在被支付
            isBuy = false;
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("isBuy", isBuy);
            return new Msg(Msg.FAILURE_CODE, objectMap);
        }
        if(StringUtils.isBlank(req.getMobile())){
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }
        if(req.getAmount() == null){
            return new Msg(Msg.FAILURE_CODE, "支付金额为空");
        }
        WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(req.getMobile(), null);
        if(weChatUserVo == null){
            return new Msg(Msg.FAILURE_CODE, "无此用户信息");
        }
        BigDecimal balance = weChatUserVo.getBalance();
        if(req.getAmount().compareTo(balance) == 1){
            return new Msg(Msg.FAILURE_CODE, "支付金额大于余额");
        }

        if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PT.getCode())){ // 普通订单
            CommonCommodity commonCommodity = commodityMapper.getCommonCommodityById(req.getId());
            if(commonCommodity == null){
                throw new RuntimeException("无此商品信息");
            }
            Integer stock = commonCommodity.getStock();
            if(stock == 0){
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("stock", stock);
                return new Msg(Msg.FAILURE_CODE, objectMap);
            }
            commodityMapper.updateStock(req.getId(), stock-1, new Date());
        }else if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PP.getCode())){
            AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
            auctionGoodsreq.setId(req.getId());
            AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectOneById(auctionGoodsreq);
            if(auctionGoodsVo == null){
                throw new RuntimeException("无此商品信息");
            }
        }else{
            throw new RuntimeException("订单号不合法！");
        }
        BigDecimal subtract = balance.subtract(req.getAmount());
        weChatUserMapper.updateBalanceToPayOrder(req.getMobile(), subtract);
        if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PT.getCode())){ // 普通订单
            orderService.updateStatusByOrderNo(req.getOrderNo(), req.getMobile());
        }else if(req.getOrderNo().substring(0,2).equals(OrderNoPrefixEnum.PP.getCode())){
            auctionOrderService.updateStatusByOrderNo(req.getOrderNo(), req.getId(), req.getMobile());
            //退还支付支付成功参与者保证金
            auctionGoodsService.backBaseAmount(req.getId(), null);
        }else{
            throw new RuntimeException("订单号不合法！");
        }
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setMobile(req.getMobile());
        paymentResult.setTotalFee(req.getAmount());
        paymentResult.setReturnMsg("余额支付订单成功");
        paymentResult.setResultCode("SUCCESS");
        paymentResult.setReturnCode("SUCCESS");
        paymentResult.setOutTradeNo(req.getOrderNo());
        paymentResult.setPaymentType(PaymentTypeEnum.BALANCE_PAY_ORDER.getCode());
        paymentResult.setTransactionId(req.getOrderNo());
        paymentResultMapper.savePaymentResult(paymentResult);
        return new Msg(Msg.SUCCESS_CODE, "支付成功", new Date());
    }

    @Override
    public Msg recharge(WechatUserReq wechatUserReq) throws Exception {
        return payPublicMethod(wechatUserReq, "1");
    }

    @Transactional
    @Override
    public Msg bondPay(WechatUserReq wechatUserReq) throws Exception {
        return payPublicMethod(wechatUserReq, "2");
    }

    @Transactional
    @Override
    public Msg balancePayBond(WechatUserReq req) {
        if(StringUtils.isBlank(req.getMobile())){
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }
        if(req.getAmount() == null){
            return new Msg(Msg.FAILURE_CODE, "支付金额为空");
        }
        WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(req.getMobile(), null);
        if(weChatUserVo == null){
            return new Msg(Msg.FAILURE_CODE, "无此用户信息");
        }
        BigDecimal balance = weChatUserVo.getBalance();
        if(req.getAmount().compareTo(balance) == 1){
            return new Msg(Msg.FAILURE_CODE, "支付金额大于余额");
        }
        BigDecimal subtract = balance.subtract(req.getAmount());

        AuctionBaseAmount auctionBaseAmount = new AuctionBaseAmount();
        auctionBaseAmount.setBaseAmount(req.getAmount());
        auctionBaseAmount.setGoodsId(req.getId());
        auctionBaseAmount.setUsername(req.getMobile());
        auctionBaseAmount.setPayType(PayTypeEnum.BALANCE.getCode());
        auctionBaseAmount.setBaseAmountStatus(1);
        insertBaseAmount(auctionBaseAmount);
        weChatUserMapper.updateBoneAndBalance(req.getMobile(), req.getAmount(), subtract);
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setMobile(req.getMobile());
        paymentResult.setTotalFee(req.getAmount());
        paymentResult.setReturnMsg("余额支付保证金成功");
        paymentResult.setResultCode("SUCCESS");
        paymentResult.setReturnCode("SUCCESS");
        paymentResult.setOutTradeNo(req.getMobile());
        paymentResult.setTransactionId(""+req.getId());
        paymentResult.setPaymentType(PaymentTypeEnum.BALANCE_PAY_BASE_AMOUNT.getCode());
        paymentResultMapper.savePaymentResult(paymentResult);
        return new Msg(Msg.SUCCESS_CODE, "支付成功", new Date());
    }

    @Override
    public void insertBaseAmount(AuctionBaseAmount auctionBaseAmount){
        List<AuctionBaseAmount> auctionBaseAmounts = auctionBaseAmountMapper.selectByUserAndGoodIdWithoutBaseAmountStatus(auctionBaseAmount);
        if(auctionBaseAmounts != null && auctionBaseAmounts.size() > 0){
            auctionBaseAmountMapper.updateBaseAmountByUserAndGoodId(auctionBaseAmount);
        }else{
            auctionBaseAmountMapper.insert(auctionBaseAmount);
        }
    }

    @Override
    public Msg transfers(WechatUserReq req) throws Exception {

        // 提现金额校验
        if(req.getAmount().compareTo(new BigDecimal(300)) < 1 || req.getAmount().compareTo(new BigDecimal(10000000)) == 1){
            return new Msg(Msg.FAILURE_CODE, "提现金额不能小于等于3元且不能大于10万");
        }

        String balanceStr = weChatUserMapper.selectBalance(req.getOpenid());
        BigDecimal balance = new BigDecimal(balanceStr);
        BigDecimal branchBalance = balance.multiply(new BigDecimal(100)); // 转换成分
        if(branchBalance.compareTo(new BigDecimal(0)) == 0){
            return new Msg(Msg.FAILURE_CODE, "当前余额为0不能提现");
        }
        if(branchBalance.compareTo(req.getAmount().add(new BigDecimal(3))) == -1){
            return new Msg(Msg.FAILURE_CODE, "当前余额为:" + balance + ",不足以扣除手续费");
        }
        boolean flag = false;
        if(req.getAmount().compareTo(new BigDecimal(300000)) == -1){
            flag = true;
        }

        // 提现参数封装
        HashMap<String, String> map = new HashMap<>();
        map.put("mch_appid", wechatConfig.getAppid());
        map.put("mchid", wechatConfig.getMchId());
        map.put("nonce_str", JSAPIPayUtil.generateNonceStr());
        map.put("partner_trade_no",IdUtils.generate(GoodsEnum.TRANSFERS, OrderNoPrefixEnum.TX));
        map.put("openid", req.getOpenid());
        map.put("check_name", "NO_CHECK");  // NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
        map.put("amount", String.valueOf(req.getAmount()));
        map.put("desc", "提现");
        map.put("spbill_create_ip", "121.40.83.63");

        String xml = JSAPIPayUtil.generateSignedXml(map, wechatConfig.getMerchantKey());
        log.info("request wechatTransfers data:" + xml);
        String res = HttpUtil.posts(wechatConfig.getTransfers(), xml);
        log.info("response wechatTransfers data:" + res);
        if(StringUtils.isBlank(res)){
            return new Msg(Msg.FAILURE_CODE, "请求失败");
        }
        Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(res);
        if(resultMap.get("return_code").equals("FAIL")){
            log.info("微信提现通信失败：" + resultMap.get("return_msg"));
            return new Msg(Msg.FAILURE_CODE, resultMap.get("return_msg"));
        }

        setPaymentResult(resultMap, flag, req);
        if(resultMap.get("result_code").equals("FAIL")){
            log.info("微信提现失败:" + resultMap.get("err_code"));
            return new Msg(Msg.FAILURE_CODE, resultMap.get("err_code"));
        }
        BigDecimal newBalance;
        if(flag){
            newBalance = balance.subtract(req.getAmount().divide(new BigDecimal(100)));
        } else {
            newBalance = balance.subtract(req.getAmount().divide(new BigDecimal(100)).subtract(new BigDecimal(300)));
        }
        weChatUserMapper.updateBalanceByOpenid(req.getOpenid(), newBalance);
        return new Msg(Msg.SUCCESS_CODE, "提现成功");
    }


    public void setPaymentResult(Map<String, String> resultMap, boolean flag, WechatUserReq req){
        PaymentResult refundResult = new PaymentResult();
        refundResult.setReturnCode(resultMap.get("return_code"));
        refundResult.setReturnMsg(resultMap.get("return_msg"));
        refundResult.setResultCode(resultMap.get("result_code"));
        refundResult.setErrCode(resultMap.get("err_code"));
        refundResult.setErrCodeDes(resultMap.get("err_code_des"));
        refundResult.setMchId(resultMap.get("mchid"));
        refundResult.setOutRefundNo(resultMap.get("payment_no"));
        refundResult.setOutTradeNo(resultMap.get("partner_trade_no"));
        refundResult.setTotalFee(req.getAmount());
        refundResult.setPaymentType(PaymentTypeEnum.TRANSFERS.getCode());
        paymentResultMapper.savePaymentResult(refundResult);
        if(flag){
            PaymentResult refundResult1 = new PaymentResult();
            refundResult1.setReturnCode("success");
            refundResult1.setReturnMsg("提现手续费");
            refundResult1.setOutRefundNo(resultMap.get("payment_no"));
            refundResult1.setOutTradeNo(resultMap.get("partner_trade_no"));
            refundResult1.setTotalFee(new BigDecimal(300));
            refundResult1.setPaymentType(PaymentTypeEnum.TRANSFERS.getCode());
            paymentResultMapper.savePaymentResult(refundResult1);
        }
    }

    public Msg payPublicMethod(WechatUserReq wechatUserReq, String type) throws Exception {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if(StringUtils.isBlank(wechatUserReq.getMobile())){
            throw new RuntimeException("手机号为空");
        }
        WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(wechatUserReq.getMobile(), null);
        if(weChatUserVo == null){
            throw new RuntimeException("用户信息为空");
        }
        if(StringUtils.isBlank(weChatUserVo.getOpenid())){
            return new Msg(Msg.OPENID_NULL, "微信未登陆");
        }
        String body = "";
        Map<String, String> treeMap = new HashMap<>();
        treeMap.put("appid", wechatConfig.getAppid());
        treeMap.put("mch_id", wechatConfig.getMchId());
        treeMap.put("device_info", wechatConfig.getDeviceInfo());
        treeMap.put("nonce_str", JSAPIPayUtil.generateNonceStr());
        treeMap.put("trade_type", "JSAPI");
        treeMap.put("notify_url", wechatConfig.getNotifyUrl());// 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
        String orderNo = "";
        if(type.equals("1")){
            orderNo = IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.CZ);
            body = "瀚华艺术-充值";
        } else if(type.equals("2")){
            orderNo = IdUtils.generate(GoodsEnum.AUCTION, OrderNoPrefixEnum.BZJ);
            body = "瀚华艺术-保证金";
        }

        treeMap.put("body", body);
        treeMap.put("out_trade_no", orderNo); // 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
        treeMap.put("total_fee", String.valueOf(wechatUserReq.getAmount()));// 订单总金额，单位为分
        treeMap.put("openid", weChatUserVo.getOpenid());
        String xml = JSAPIPayUtil.generateSignedXml(treeMap, wechatConfig.getMerchantKey());
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        log.info("mobile:{},requestXml:{}", wechatUserReq.getMobile(), xml);
        String result = restTemplate.postForObject(wechatConfig.getJSPAPIUrl(), xml, String.class);
        log.info("mobile:{},responseXml:{}", wechatUserReq.getMobile(), result);
        Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(result);
        log.info("mobile:{},xmlToMap:{}", wechatUserReq.getMobile(), resultMap);
        String result_code = resultMap.get("result_code");
        String return_code = resultMap.get("return_code");
        operations.set(orderNo, wechatUserReq.getMobile());
        operations.set(treeMap.get("nonce_str"), String.valueOf(wechatUserReq.getId()));
        if(return_code.equals("SUCCESS") && result_code.equals("SUCCESS")){
            Map<String, String> map = JSAPIPayUtil.xmlToMap(xml);
            String prepay_id = resultMap.get("prepay_id");
            //返回给APP端的参数，APP端再调起支付接口
            Map<String,String> repData = new HashMap<>();
            repData.put("appId",wechatConfig.getAppid());
            // repData.put("mch_id",wechatConfig.getMchId());
            repData.put("package","prepay_id=" + prepay_id);
            repData.put("nonceStr",resultMap.get("nonce_str"));
            repData.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
            repData.put("signType", "MD5");
            String sign = JSAPIPayUtil.generateSignature(repData, wechatConfig.getMerchantKey()); //签名
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("paySign",sign);
            reqMap.put("timeStamp",repData.get("timeStamp"));
            reqMap.put("package","prepay_id=" + prepay_id);
            reqMap.put("nonceStr", resultMap.get("nonce_str"));
            reqMap.put("appId", wechatConfig.getAppid());
            reqMap.put("signType", "MD5");
            reqMap.put("isBuy", true);
            log.info("mobile:{},resultMap:{}", wechatUserReq.getMobile(), reqMap);
            return new Msg(Msg.SUCCESS_CODE,reqMap);
        } else {
            String err_code_des = resultMap.get("err_code_des");
            return new Msg(Msg.FAILURE_CODE, err_code_des);
        }
    }
    public PaymentResult assemblePaymentResult(Map<String, String> resultMap){
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setReturnCode(resultMap.get("return_code"));
        paymentResult.setErrCode(resultMap.get("err_code"));
        paymentResult.setErrCodeDes(resultMap.get("err_code_des"));
        paymentResult.setResultCode(resultMap.get("result_code"));
        paymentResult.setMchId(resultMap.get("mch_id"));
        paymentResult.setTotalFee(new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal(100)));
        paymentResult.setReturnMsg(resultMap.get("return_msg"));
        paymentResult.setOutTradeNo(resultMap.get("out_trade_no"));
        paymentResult.setTransactionId(resultMap.get("transaction_id"));
        paymentResult.setMobile(resultMap.get("mobile"));
        paymentResult.setPaymentType(Integer.valueOf(resultMap.get("paymentType")));
        paymentResult.setOrderNo(resultMap.get("orderNo"));
        return paymentResult;
    }
}
