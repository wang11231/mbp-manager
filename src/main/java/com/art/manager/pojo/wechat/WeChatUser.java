package com.art.manager.pojo.wechat;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WeChatUser implements Serializable {
    private Long id;
    private String openid; // 用户的唯一标识
    private String nickname; // 用户昵称
    private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String language; //
    private String city; // 普通用户个人资料填写的城市
    private String province; // 用户个人资料填写的省份
    private String country; // 国家，如中国为CN
    private String headimgurl; // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效
    private String privileges; // 用户特权信息，json 数组
    private String unionid; // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
    private String mobile;
    private BigDecimal bond; // 保证金
    private BigDecimal balance; // 余额
    private String status = "0";  //0:可用
    private Date createTime;
    private Date updateTime;
    private Date lastLoginTime; // 最后登录时间
    private JSONObject wechatJson; // 微信用户信息
    private String code; // 短信验证码

}
