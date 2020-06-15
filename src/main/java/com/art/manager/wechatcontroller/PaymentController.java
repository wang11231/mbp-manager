package com.art.manager.wechatcontroller;

import com.art.manager.config.wechat.WechatConfig;
import com.art.manager.enums.PaymentTypeEnum;
import com.art.manager.mapper.CommonCommodityMapper;
import com.art.manager.mapper.auction.AuctionBaseAmountMapper;
import com.art.manager.mapper.wechat.PaymentResultMapper;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.auction.AuctionBaseAmount;
import com.art.manager.pojo.wechat.PaymentResult;
import com.art.manager.request.PaymentResultReq;
import com.art.manager.request.WechatUserReq;
import com.art.manager.service.JSAPIPayService;
import com.art.manager.service.OrderService;
import com.art.manager.service.auction.AuctionGoodsService;
import com.art.manager.service.auction.AuctionOrderService;
import com.art.manager.thread.SyncNotifyService;
import com.art.manager.util.JSAPIPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private JSAPIPayService jsapiPayService;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private AuctionOrderService auctionOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentResultMapper paymentResultMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private CommonCommodityMapper commodityMapper;
    @Autowired
    private AuctionBaseAmountMapper auctionBaseAmountMapper;
    @Autowired
    private SyncNotifyService syncNotifyService;
    @Autowired
    private AuctionGoodsService auctionGoodsService;
    /**
     * 微信支付接口
     * @param token
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Msg payment(@RequestHeader(value = "Authorization") String token, @RequestBody WechatUserReq req, HttpServletRequest request){
        try {
            return jsapiPayService.pay(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            redisTemplate.delete(String.valueOf(req.getId()));
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 充值
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public Msg recharge(@RequestBody WechatUserReq req, HttpServletRequest request){
        try {
            return jsapiPayService.recharge(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 返回成功xml
     */
    private String resSuccessXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 返回失败xml
     */
    private String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    /**
     * 链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知
     * @param request
     * @param response
     */
    @Transactional
    @RequestMapping(value = "/wxnotify", method = RequestMethod.POST)
    public void wxnotify(HttpServletRequest request, HttpServletResponse response){
        String resXml = "";
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outputStream.toByteArray(), "utf-8");
            log.info("微信支付结果===>" + result);
            outputStream.close();
            inputStream.close();
            Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(result);
            log.info("支付通知resultMap==>" + resultMap);
            String total_fee = resultMap.get("total_fee"); // 订单金额
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String orderNo = resultMap.get("out_trade_no");
            String mobile = operations.get(orderNo);
            Long id = 0l;
            log.info("nonce_str===>" + resultMap.get("nonce_str"));
            log.info("id===>{}，mobile===》{}", operations.get(resultMap.get("nonce_str")), mobile);
            if(StringUtils.isNotBlank(operations.get(resultMap.get("nonce_str"))) && !"null".equals(operations.get(resultMap.get("nonce_str")))){
                id = Long.valueOf(operations.get(resultMap.get("nonce_str")));
            }
            //paymentResultMapper.updatePaymentResult(resultMap);
            if ("SUCCESS".equalsIgnoreCase(resultMap.get("result_code"))){
                log.info("微信支付-----返回成功");
                if(JSAPIPayUtil.isSignatureValid(resultMap, wechatConfig.getMerchantKey())){
                    log.info("微信支付----验证签名成功");
                if(operations.get(String.valueOf(id)) != null){
                    redisTemplate.delete(String.valueOf(id));
                }
                    resXml = resSuccessXml;
                    resultMap.put("mobile", mobile);
                    resultMap.put("orderNo", orderNo);

                    if(orderNo.substring(0,2).equals("PT")){ // 普通订单
                        orderService.updateStatusByOrderNo(orderNo, mobile);
                        resultMap.put("paymentType", String.valueOf(PaymentTypeEnum.WX_PAY_ORDER.getCode()));
                        resultMap.put("transaction_id", orderNo);
                        CommonCommodity commonCommodity = commodityMapper.getCommonCommodityById(id);
                        if(commonCommodity != null && commonCommodity.getStock() > 0){
                            Integer stock = commonCommodity.getStock();
                            commodityMapper.updateStock(id, stock-1, new Date());
                        }
                    } else if(orderNo.substring(0,2).equals("CZ")){
                        resultMap.put("paymentType", String.valueOf(PaymentTypeEnum.DEPOSIT.getCode()));
                        BigDecimal balance = new BigDecimal(weChatUserMapper.getBalance(mobile)).add(new BigDecimal(total_fee).divide(new BigDecimal(100)));
                        resultMap.put("transaction_id", mobile);
                        weChatUserMapper.updateBalanceByMobile(mobile, balance);
                    } else if(orderNo.substring(0,3).equals("BZJ")){
                        resultMap.put("paymentType", String.valueOf(PaymentTypeEnum.WX_PAY_BASE_AMOUNT.getCode()));
                        resultMap.put("transaction_id", ""+id);
                        syncNotifyService.updateBone(mobile, total_fee, id);
                    } else if(orderNo.substring(0,2).equals("PP")){
                        resultMap.put("paymentType", String.valueOf(PaymentTypeEnum.WX_PAY_ORDER.getCode()));
                        resultMap.put("transaction_id", orderNo);
                        auctionOrderService.updateStatusByOrderNo(orderNo, id, mobile);
                        syncNotifyService.updateBoneStatus(id, mobile);
                        //退还支付支付成功参与者保证金
                        auctionGoodsService.backBaseAmount(id, null);
                    }

                } else {
                    log.info("微信支付----判断签名错误");
                    resXml = resFailXml;
                }
            } else {
                log.info("wxnotify:支付失败,错误信息：" + resultMap.get("err_code_des"));
                resXml = resFailXml;
            }
            jsapiPayService.savePayment(resultMap);
        } catch (Exception e){
            log.error("wxnotify:支付回调发布异常：", e);
        }finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error("wxnotify:支付回调发布异常:out：", e);
            }
        }
    }


    @Transactional
    @RequestMapping(value = "/refundNotifyUrl", method = RequestMethod.POST)
    public void refundNotifyUrl(HttpServletRequest request, HttpServletResponse response){
        String resXml="";
        InputStream inputStream;
        try{
            inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outputStream.toByteArray(), "utf-8");
            log.info("微信退款结果===>" + result);
            outputStream.close();
            inputStream.close();
            Map<String, String> resultMap = JSAPIPayUtil.xmlToMap(result);
            log.info("退款通知resultMap==>" + resultMap);
            String totalFee = resultMap.get("settlement_total_fee"); // 订单金额
            String transactionId = resultMap.get("transaction_id");
            //PaymentResult paymentResult = paymentResultMapper.selectPayResultByTransactionId(transactionId);
            Integer refundStatus = 0;
            log.info("nonce_str===>" + resultMap.get("nonce_str"));
            if ("SUCCESS".equalsIgnoreCase(resultMap.get("return_code"))){
                if("SUCCESS".equalsIgnoreCase(resultMap.get("refund_status"))){ // 退款成功
                    refundStatus = 2;
                } else if("CHANGE".equalsIgnoreCase(resultMap.get("refund_status"))){ // 退款异常
                    refundStatus = 3;
                } else if("REFUNDCLOSE".equalsIgnoreCase(resultMap.get("refund_status"))){ // 退款关闭
                    refundStatus = 4;
                }
                paymentResultMapper.updatePaymentResultByTransactionId(refundStatus, transactionId);
                log.info("微信退款-----返回成功");
                resXml = resSuccessXml;
            } else {
                log.info("wxnotify:退款失败,错误信息：" + resultMap.get("return_msg"));
                resXml = resFailXml;
            }

        }catch (Exception e){

            log.error("wxnotify:退款回调发布异常：", e);
        }finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error("wxnotify:退款回调发布异常:out：", e);
            }
        }
    }


    /**
     * 微信申请退款
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public Msg refund(@RequestHeader(value = "Authorization") String token, @RequestBody WechatUserReq req, HttpServletRequest request){
        /* SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            return jsapiPayService.refund(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, "退款失败");
        }
    }

    /**
     * 交易明细
     * @param req
     * @return
     */
    @RequestMapping(value = "/getTransactionDetails", method = RequestMethod.POST)
    public Msg getTransactionDetails( @RequestBody PaymentResultReq req){
        try {
            return new Msg(Msg.SUCCESS_CODE,jsapiPayService.getTransactionDetails(req));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 余额支付
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/balancePay", method = RequestMethod.POST)
    public Msg balancePay(@RequestBody WechatUserReq req, HttpServletRequest request){
        try {
            return jsapiPayService.balancePay(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 支付保证金
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/bondPay", method = RequestMethod.POST)
    public Msg bondPay(@RequestBody WechatUserReq req, HttpServletRequest request){
        try {
            return jsapiPayService.bondPay(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 余额支付保证金
     * @param req
     * @param request
     * @return
     */
    @RequestMapping(value = "/balancePayBond", method = RequestMethod.POST)
    public Msg balancePayBond(@RequestHeader(value = "Authorization") String token,@RequestBody WechatUserReq req, HttpServletRequest request){
        try {
            return jsapiPayService.balancePayBond(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/transfers", method = RequestMethod.POST)
    public Msg transfers(@RequestHeader(value = "Authorization") String token, @RequestBody WechatUserReq req){
        try {
            return jsapiPayService.transfers(req);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


}
