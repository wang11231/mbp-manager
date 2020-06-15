package com.art.manager.wechatcontroller;

import com.alibaba.fastjson.JSONObject;
import com.art.manager.config.wechat.WechatConfig;
import com.art.manager.mapper.wechat.WeChatUserMapper;
import com.art.manager.pojo.wechat.WeChatUser;
import com.art.manager.service.WeChatUserService;
import com.art.manager.util.ShalUtil;
import com.art.manager.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/wx")
public class WechatOauthController {

    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private WeChatUserService weChatUserService;
    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Value("${web.host}")
    private String webHost;

    @Value("${web.redirectUrl}")
    private String redirectUrl;

    @Value("${web.indexUrl}")
    private String indexUrl;
    /**
     * 验证服务器地址的有效性
     * @param req
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/requestVerification")
    public void requestVerification(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        /**
         * 接收微信服务器发送请求时传递过来的参数
         */
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce"); //随机数
        String echostr = req.getParameter("echostr");//随机字符串
        /**
         * 将token、timestamp、nonce三个参数进行字典序排序
         * 并拼接为一个字符串
         */
        String sortStr = sort(wechatConfig.getToken(),timestamp,nonce);
        /**
         * 字符串进行shal加密
         */

        String mySignature = ShalUtil.shal(sortStr);
        /**
         * 校验微信服务器传递过来的签名 和  加密后的字符串是否一致, 若一致则签名通过
         */
        if(!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)){
            System.out.println("-----签名校验通过-----");
            resp.getWriter().write(echostr);
        }else {
            System.out.println("-----校验签名失败-----");
        }

    }

    @RequestMapping(value = "/getToken")
    public String getToken(HttpServletRequest request){
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wechatConfig.getAppid() + "&secret=" + wechatConfig.getSecret();
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("forObject===>" + forObject);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        String access_token = jsonObject.getString("access_token");
        RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("access_token", access_token, 7200l, TimeUnit.SECONDS);
        return access_token;
    }

    @RequestMapping(value = "/wxLogin")
    public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //请求地址
        //  用户同意授权，获取code
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + wechatConfig.getAppid() +
                "&redirect_uri=" + URLEncoder.encode(wechatConfig.getCallBack()) +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        // 重定向
        response.sendRedirect(url);
    }


    @RequestMapping("/callBack/{mobile}")
    public void callBack(@PathVariable(name = "mobile") String mobile, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");
        log.info("code===>" + code);
        //获取access_token
        // 通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + wechatConfig.getAppid() +
                "&secret=" + wechatConfig.getSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        log.info("url===>" + url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("forObject===>" + forObject);
        JSONObject json = JSONObject.parseObject(forObject);
        //刷新access_token
        String refreshUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+ wechatConfig.getAppid()+"&grant_type=refresh_token&refresh_token=" + json.getString("refresh_token");
        String forObject1 = restTemplate.getForObject(refreshUrl, String.class);
        log.info("forObject1===>" + forObject1);
        JSONObject jsonObject = JSONObject.parseObject(forObject1);
        //请求获取userInfo
        // 拉取用户信息
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + jsonObject.getString("access_token") +
                "&openid=" + jsonObject.getString("openid") +
                "&lang=zh_CN";

        String wechatInfo = restTemplate.getForObject(infoUrl, String.class);
        JSONObject wechatJson = JSONObject.parseObject(wechatInfo);
        log.info("wechatInfo===>" + wechatInfo);
        String status = weChatUserMapper.selectStatus(mobile);
        log.info("重定向页面地址===>" + webHost + "/login?openid=" + wechatJson.getString("openid"));
        if(StringUtils.isNotBlank(status) && status.equals("1")){ // status=1 说明已经登录了
            wechatJson.put("mobile", mobile);
            weChatUserService.updateWeChatUserByMobile(wechatJson);
            response.sendRedirect(webHost + "/me/userCenter");
            return;
        }
        WeChatUser weChatUser = weChatUserMapper.selectMobile(wechatJson.getString("openid"));
        if(weChatUser != null){
            weChatUserService.updateWeChatUser(wechatJson);
            // 跳转到手机登录页
            response.sendRedirect(redirectUrl + "openid*" + wechatJson.getString("openid"));
            /*if(StringUtils.isNotEmpty(weChatUser.getMobile())){
                // 已绑定手机号 跳转到首页
                response.sendRedirect(webHost + "/login?openid=" + wechatJson.getString("openid"));
            } else {
                // 未绑定手机号 跳转到手机登录页
                response.sendRedirect(webHost + "/login?openid=" + wechatJson.getString("openid"));
            }*/
        } else {
            // 未绑定手机号 跳转到手机登录页
            weChatUserService.inserWeChatUser(wechatJson);
            response.sendRedirect(redirectUrl + "openid*" + wechatJson.getString("openid"));
        }

    }

    /**
     * 参数排序
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 获取cookie
     * @param request
     * @param cookieName
     * @return
     */
    public String cookie(HttpServletRequest request, String cookieName){
        Cookie[]cookies=request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
