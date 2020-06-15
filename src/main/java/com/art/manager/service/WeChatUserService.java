package com.art.manager.service;

import com.alibaba.fastjson.JSONObject;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.request.WechatUserReq;
import com.art.manager.vo.WeChatUserVo;

import java.util.Map;

public interface WeChatUserService {

    int inserWeChatUser(JSONObject wechatJson);

    int updateWeChatUser(JSONObject wechatJson);
    int updateWeChatUserByMobile(JSONObject wechatJson);

    int saveByMobile(WeChatUser weChatUser);

    WeChatUserVo isRegister(String mobile, String openid);

    int updateOpenid(WeChatUser weChatUser);

    int updateMobile(WeChatUser weChatUser);

    Msg wechatUserList(WechatUserReq req);

    Msg orderNoList(WechatUserReq req);

    Msg orderList(WechatUserReq req);

    Msg getUserBalance(Map<String, Object> params);


    Map<String, Object>  getAuctionStartPriceList(Map<String, Object> params);

    Map<String, Object> getUserPaseAmountList(Map<String, Object> params);
}
