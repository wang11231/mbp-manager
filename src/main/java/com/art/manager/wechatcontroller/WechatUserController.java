package com.art.manager.wechatcontroller;

import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.request.WechatUserReq;
import com.art.manager.service.VerifyCodeService;
import com.art.manager.service.WeChatUserService;
import com.art.manager.util.MD5Utils;
import com.art.manager.vo.WeChatUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信用户控制类
 */
@Slf4j
@RestController
@RequestMapping("/wechatuser")
public class WechatUserController {

    private static final Long TOKEN_TIMEOUT = 30L;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private WeChatUserService weChatUserService;
    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 微信用户 没有注册即注册 注册过的用户直接登录
     *
     * @param weChatUser
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Msg login(@RequestBody WeChatUser weChatUser, HttpServletRequest request) {
        log.info("打印登录信息weChatUser===》" + weChatUser);
        if (StringUtils.isBlank(weChatUser.getMobile())) {
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }

        if(StringUtils.isBlank(weChatUser.getCode())){
            return new Msg(Msg.FAILURE_CODE, "验证码为空");
        }
        Msg msg = verifyCodeService.validateVerifyCode(weChatUser.getMobile(), weChatUser.getCode());
        if(msg.getCode() == 1){
            return msg;
        }
        try {
            /**
             * 将登陆的用户信息存放到redis中
             */
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String encode = passwordEncoder.encode(weChatUser.getMobile());
            String token = MD5Utils.md5(encode);
            WeChatUserVo weChatUserVo = null;
            // 判断用户是否注册
            weChatUserVo = weChatUserService.isRegister(weChatUser.getMobile(), null);
            if (weChatUserVo != null) { // 手机已注册   判断是否带有openid
                if (StringUtils.isNotEmpty(weChatUser.getOpenid())) { // openid不为空，则根据手机号跟新openid
                    WeChatUserVo user = weChatUserMapper.selectWechatUser(null, weChatUser.getOpenid());
                    // 校验此mobile是否已绑定有openid 如已绑定 则校验绑定openid是否与传入openid一致
                    log.info("weChatUser.getMobile()====>" + weChatUser.getMobile());
                    log.info("user.getMobile()====>" + user.getMobile());
                    if(!weChatUser.getMobile().equals(user.getMobile())){
                        //StringUtils.isNotBlank(weChatUserVo.getOpenid()) && !weChatUser.getOpenid().equals(weChatUserVo.getOpenid())){
                        return new Msg(Msg.MOBILE_CODE, "授权的不是同一个手机号");

                    } else if (StringUtils.isBlank(weChatUserVo.getOpenid())){
                        if(StringUtils.isBlank(user.getMobile())){
                            weChatUserMapper.updateMobile(weChatUser);
                            weChatUserMapper.delWechatUser(weChatUserVo.getId());
                        }

                    } else {
                        weChatUserService.updateOpenid(weChatUser);
                    }
                } else {
                    weChatUserMapper.updateStatus("1", weChatUser.getMobile(), new Date());
                }
            } else { // 手机号未注册， 先验证是否带有openid
                if (StringUtils.isEmpty(weChatUser.getOpenid())) { // openid为空，则直接注册
                    weChatUserService.saveByMobile(weChatUser);
                    weChatUserVo = new WeChatUserVo();
                    weChatUserVo.setId(weChatUser.getId());
                } else { // openid不为空， 说明已经微信授权，根据openid  update mobile即可
                    weChatUserVo = weChatUserService.isRegister(null, weChatUser.getOpenid());
                    if (weChatUserVo == null) {
                        return new Msg(Msg.FAILURE_CODE, "数据异常");
                    }
                    // 校验此openid是否已绑定有mobile  如果已绑定  则校验绑定手机号是否与传入手机号一致
                    if(StringUtils.isNotBlank(weChatUserVo.getMobile()) &&
                            !weChatUser.getMobile().equals(weChatUserVo.getMobile())){
                        return new Msg(Msg.MOBILE_CODE, "授权的不是同一个手机号");
                    }
                    weChatUserService.updateMobile(weChatUser);
                }
            }
            log.info("token:{}" + token);
            weChatUserVo.setRedisToken(token);
            weChatUserVo.setMobile(weChatUser.getMobile());
            HttpSession session = request.getSession();
            session.setAttribute(token, weChatUser);
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(token, weChatUser.getMobile(), TOKEN_TIMEOUT, TimeUnit.DAYS);
            Map<String, Object> map = new HashMap<>();
            map.put("user", weChatUserVo);
            return new Msg(Msg.SUCCESS_CODE, map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Msg logout(@RequestHeader(value = "Authorization") String token, HttpServletRequest request) {

        try {
            /*HttpSession session = request.getSession(false);
            if (session == null) {
                return new Msg(Msg.FAILURE_CODE, "用户未登录", new Date());
            }*/
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String mobile = operations.get(token);
            if (StringUtils.isEmpty(mobile)) {
                return new Msg(Msg.FAILURE_CODE, "用户未登录", new Date());
            }
            weChatUserMapper.updateStatus("0", mobile, null);
            // session.invalidate();
            redisTemplate.delete(token);
            Msg msg = new Msg(Msg.SUCCESS_CODE, "成功退出", new Date());
            return msg;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 获取微信用户列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/wechatUserList", method = RequestMethod.POST)
    public Msg wechatUserList(@RequestBody WechatUserReq req) {
        try {
            return weChatUserService.wechatUserList(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    /**
     * 通过账号获取订单号列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/orderNoList", method = RequestMethod.POST)
    public Msg orderNoList(@RequestBody WechatUserReq req) {
        try {
            return weChatUserService.orderNoList(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }


    /**
     * 个人中心页获取用户信息
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/userCenter", method = RequestMethod.POST)
    public Msg userCenter(@RequestBody WechatUserReq req) {
         /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            WeChatUserVo weChatUserVo = weChatUserMapper.selectWechatUser(req.getMobile(), null);
            if(weChatUserVo == null){
                weChatUserVo = new WeChatUserVo();
            }
            if(StringUtils.isBlank(weChatUserVo.getOpenid())){
                weChatUserVo.setNickname(weChatUserVo.getMobile());
            }
            return new Msg(Msg.SUCCESS_CODE, weChatUserVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }

    @RequestMapping(value = "/auctionGoodsList", method = RequestMethod.POST)
    public Msg auctionGoodsList(@RequestHeader(value = "Authorization") String token, @RequestBody WechatUserReq req, HttpServletRequest request) {
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            return new Msg(Msg.SUCCESS_CODE, weChatUserMapper.selectWechatUser(req.getMobile(), null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }
    }


    /**
     * 个人中心 代付款、代发货、待收货、全部订单、牌品订单
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/orderListNum", method = RequestMethod.POST)
    public Msg orderListNum(@RequestBody WechatUserReq req) {
        try {
            return weChatUserService.orderList(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }

    }

    /**
     * 个人中心获取账户余额
     * @param params
     * @return
     */
    @RequestMapping(value = "/getUserBalance", method = RequestMethod.POST)
    public Msg getUserBalance( @RequestBody Map<String, Object> params){
        try {
            return  new Msg(Msg.SUCCESS_CODE, weChatUserService.getUserBalance(params));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 个人中心拍卖保证金列表历史明细
     * @param params
     * @return
     */
    @RequestMapping(value = "/getAuctionStartPriceList", method = RequestMethod.POST)
    public Msg getAuctionStartPriceList( @RequestBody Map<String, Object> params){
        try {
            Map<String, Object> auctionStartPriceList = weChatUserService.getAuctionStartPriceList(params);
            return  new Msg(Msg.SUCCESS_CODE,"成功" ,auctionStartPriceList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 个人中心拍卖保证金列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/getUserPaseAmountList", method = RequestMethod.POST)
    public Msg getUserPaseAmountList( @RequestBody Map<String, Object> params){
        try {
            Map<String, Object> userPaseAmountList = weChatUserService.getUserPaseAmountList(params);
            return  new Msg(Msg.SUCCESS_CODE,"成功" ,userPaseAmountList);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}



