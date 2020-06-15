package com.art.manager.filter;

import com.art.manager.pojo.Msg;
import com.art.manager.pojo.SysUser;
import com.art.manager.util.JsonUtil;
import com.art.manager.util.SpringUtils;
import com.art.manager.vo.SysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 校验session是否过期
 *
 * @author Administrator
 * @Description:
 * @Date: Created in 11:37 2018/6/26
 * @Modified By:
 * <p>
 * //跨域问题解决
 */

@Slf4j
public class SessionValidateFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(SessionValidateFilter.class);

    private static final String LOGIN = "/user/login";
    private static final String REGISTER = "/user/register";
    private static final String REGISTER_VERIFYCODE = "/code/getVerifyCode";//短信验证码
    private static final String PHOTO_CODE = "/code/photoCode";//图片验证码
    private static final String FIND_PASSWORD = "/user/findPassword";
    private static final String WECHAT = "/wx";
    private static final String FAVICON_ICO = "/favicon.ico";
    private static final String WECHATUSER_LOGIN = "/wechatuser/login";
    //private static final String WECHATUSER_LOGOUT = "/wechatuser/logout";
    private static final String HOMEPAGE = "/homepage";
    private static final String NOTIFY = "/pay/wxnotify";
    private static final String AUCTION_SELECTH5BYID= "/auction/selectH5ById";//H5商品详情
    private static final String AUCTION_H5GOODS= "/auction/getH5Goods";//H5拍品订单列表
    private static final String AUCTION_H5GOODS_TOAY= "/auction/getH5GoodsForToday";//H5拍品订单列表
    private static final String COMMODITY_INFO= "/commodity/commodityDetail";//普通商品详情
    private static final String COMMODITY_LIST= "/commodity/commodityList";//普通商品列表
    private static final String WECHAT_STYLELIST= "/wechatOrder/getStyleList";//H5所有风格
    private static final String CATEGORY= "/category";//分类
    private static final String PROCESS_GETLIST= "/auction/process/getList";//流程清单
    private static final String ARTIST_LIST= "/artist/getArtist";//获取全部艺术家列表
    private static final String ARTIST_LIST_NO_PAGE= "/artist/getArtistListNoPage";//获取全部艺术家列表
    private static final String ARTIST_INFO_H5= "/artist/selectByIdH5";//获取全部艺术家列表
    private static final String NEWS_DETAIL= "/newsInfo/newsDetail";//获取新闻信息
    private static final String SEARCH= "/wechatOrder/getSearchList";//搜索
    private static final String UPLOADZIP = "/zip/uploadZip";

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,Authorization,X-UA-Compatible");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        logger.debug("请求的uri为：" + uri);

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()){
            log.debug(request.getHeader(names.nextElement()));
        }
        log.info("请求的uri为：" + uri);
        log.info("uri.startsWith(UPLOADZIP)==" + uri.startsWith(UPLOADZIP));
        if (uri.startsWith(LOGIN) ||
                uri.startsWith(REGISTER) ||
                uri.startsWith(REGISTER_VERIFYCODE) ||
                uri.startsWith(FIND_PASSWORD) ||
                uri.startsWith(PHOTO_CODE) ||
                uri.startsWith(WECHAT) ||
                uri.startsWith(FAVICON_ICO) ||
                uri.startsWith(WECHATUSER_LOGIN)||
                uri.startsWith(HOMEPAGE)||
                uri.startsWith(NOTIFY)||
                uri.startsWith(AUCTION_SELECTH5BYID)||
                uri.startsWith(AUCTION_H5GOODS)||
                uri.startsWith(AUCTION_H5GOODS_TOAY)||
                uri.startsWith(COMMODITY_INFO)||
                uri.startsWith(WECHAT_STYLELIST)||
                uri.startsWith(CATEGORY)||
                uri.startsWith(PROCESS_GETLIST)||
                uri.startsWith(COMMODITY_LIST)||
                uri.startsWith(ARTIST_LIST_NO_PAGE)||
                uri.startsWith(ARTIST_INFO_H5)||
                uri.startsWith(NEWS_DETAIL)||
                uri.startsWith(ARTIST_LIST) ||
                uri.startsWith(SEARCH) ||
                uri.startsWith(UPLOADZIP)
        ) {
            log.debug("这里的请求路径都不拦截...");
        } else {
            //判断session是否存在
            /*HttpSession session = request.getSession(false);
            if (session == null) {
                Msg msg = new Msg(Msg.SESSSION_TIME_OUT, "超时，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }*/
            String token = request.getHeader("Authorization");
            log.info("Authorization:{}", token);
            if (token == null) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }
            RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String result = valueOperations.get(token);
            //判断用户是否发token
            if (StringUtils.isBlank(result)) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }
            request.setAttribute("username",result);
           /* //根据token获取用户，判断用户信息是否在session里面
            Object o = session.getAttribute(token);
            if (o == null) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }

            //判断session存储的对象信息是否为用户信息
            if (o.getClass() != SysUser.class) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }*/
        }
        chain.doFilter(request, response);
    }
}

