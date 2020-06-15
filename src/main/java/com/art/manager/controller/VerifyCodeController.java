package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.service.VerifyCodeService;
import com.art.manager.util.IpUtils;
import com.art.manager.util.PhotoUtil;
import com.art.manager.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("code")
public class VerifyCodeController {

    private static final Logger LOOGER = LoggerFactory.getLogger(VerifyCodeController.class);

    @Autowired
    private VerifyCodeService verifyCodeService;

    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
    public Msg getVerifyCode(HttpServletRequest request, @RequestBody Map<String, Object> map) {
       /* //获取ip
        String ip = IpUtils.getIpAddress(request);
        if (StringUtils.isBlank(ip)) {
            return new Msg(Msg.FAILURE_CODE, "ip为空");
        }*/
        Object phoneNoObj = map.get("phoneNo");
        if(phoneNoObj == null){
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }
        String phoneNo = String.valueOf(phoneNoObj);
        if (StringUtils.isBlank(phoneNo)) {
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }
        if (!RegexUtil.isTel(phoneNo)) {
            return new Msg(Msg.FAILURE_CODE, "请输入正确的手机号");
        }
        /*//验证图片验证码
        Object photoCodeObj = map.get("photoCode");
        if(photoCodeObj == null){
            return new Msg(Msg.FAILURE_CODE, "图片验证码为空");
        }
        String photoCode = String.valueOf(photoCodeObj);
        if (StringUtils.isBlank(photoCode)) {
            return new Msg(Msg.FAILURE_CODE, "图片验证码为空");
        }
        Object obj = request.getSession().getAttribute("photoCode");
        if(obj == null){
            return new Msg(Msg.FAILURE_CODE, "图片验证码过期");
        }
        String sessionPhotoCode = (String)obj;
        if(!sessionPhotoCode.equalsIgnoreCase(photoCode)){
            return new Msg(Msg.FAILURE_CODE, "图片验证码不匹配");
        }*/
        try {
            /*//验证ip
            verifyCodeService.validateIp(ip, phoneNo);*/
            //发送验证码
            return new Msg(Msg.SUCCESS_CODE, verifyCodeService.sendVerifyCode(phoneNo));
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 图片验证码
     * @return
     */
    @RequestMapping(value="/photoCode")
    public String photoCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        HttpSession session = request.getSession(true);
        PhotoUtil photo = new PhotoUtil(100, 30, 4, 40);
        session.removeAttribute("photoCode");
        session.setAttribute("photoCode", photo.getCode());
        photo.write(response.getOutputStream());
        return null;
    }


    @RequestMapping(value = "/validateCode", method = RequestMethod.POST)
    public Msg validateCode(HttpServletRequest request, @RequestBody Map<String, String> map){
        String phoneNo = map.get("phoneNo");
        String code = map.get("code");
        if(StringUtils.isEmpty(phoneNo)){
            return new Msg(Msg.FAILURE_CODE, "手机号为空");
        }
        if(StringUtils.isEmpty(code)){
            return new Msg(Msg.FAILURE_CODE, "验证码为空");
        }
        Msg msg = verifyCodeService.validateVerifyCode(phoneNo, code);
        return msg;
    }


//    /**
//     * 阿里云短信服务
//     * @param
//     * @throws IOException
//     */
//    @RequestMapping(value = "/aliyunSend", method = RequestMethod.GET)
//    public Msg aliyunSend(@RequestHeader(value = "token") String token, @RequestParam("phone") String phone, HttpServletRequest request){
//        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
//        if (user == null) {
//            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
//        }*/
//        if(StringUtils.isEmpty(phone)){
//            return new Msg(Msg.FAILURE_CODE, "手机号不能为空");
//        }
//        Msg msg = verifyCodeService.sendVerifyCode(phone);
//        return msg;
//    }

}
