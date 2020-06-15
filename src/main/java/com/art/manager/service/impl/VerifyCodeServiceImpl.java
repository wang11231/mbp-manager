package com.art.manager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.art.manager.enums.StatusEnum;
import com.art.manager.exception.SendVerifyCodeException;
import com.art.manager.mapper.verifycode.VerifyCodeMapper;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.verifycode.VerifyCode;
import com.art.manager.pojo.verifycode.VerifyCodeIp;
import com.art.manager.pojo.verifycode.VerifyCodeIpWhiteList;
import com.art.manager.service.VerifyCodeService;
import com.art.manager.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private static final Logger LOOGER = LoggerFactory.getLogger(VerifyCodeServiceImpl.class);

    @Autowired
    private VerifyCodeMapper verifyCodeMapper;

    @Autowired
    private RestTemplate restTemplate;

    //同一ip访问上限
    private static final int IP_ACCESS_COUNT = 10;

    private static final String appid = "10947";
    // 用户密钥
    private static final String signature = "9d4644811fcf9fa5a0a9f54690baf6e7";

    private static final String SEND_MESSAGE_URL = "http://api.submail.cn/message/xsend.json";
    // 模版代码
    private static final String project = "AeKAs3";

    @Override
    public boolean validateIp(String ip, String phoneNo) {
        //查询白名单
        VerifyCodeIpWhiteList whiteList = new VerifyCodeIpWhiteList();
        whiteList.setIp(ip);
        whiteList.setStatus(StatusEnum.VALID.getCode());
        List<VerifyCodeIpWhiteList> list = verifyCodeMapper.selectWhiteListByIp(whiteList);
        if(list == null || list.size() == 0){//不在白名单中
            Date date = new Date();
            String dayTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
            VerifyCodeIp verifyCodeIp = new VerifyCodeIp();
            verifyCodeIp.setIp(ip);
            verifyCodeIp.setPhoneNo(phoneNo);
            verifyCodeIp.setDayTime(dayTime);
            verifyCodeIp.setUpdateTime(date);
            VerifyCodeIp verifyCode = verifyCodeMapper.selectVerifyCodeIpByIp(verifyCodeIp);
            if(verifyCode == null){//插入新纪录
                verifyCodeIp.setCount(1);
                verifyCodeIp.setCreateTime(date);
                verifyCodeMapper.insertVerifyCodeIp(verifyCodeIp);
            }else{
                int num = verifyCode.getCount();
                LOOGER.info("ip：" + ip +",访问次数:" + num);
                if(num >= IP_ACCESS_COUNT){
                    LOOGER.info("ip：" + ip +",访问次数达到" + IP_ACCESS_COUNT + "次及以上，请确认是否恶意攻击");
                    throw new SendVerifyCodeException("ip：" + ip +",访问次数达到" + IP_ACCESS_COUNT + "次及以上，请确认是否恶意攻击");
                }else{
                    verifyCodeMapper.updateVerifyCodeIpByIp(verifyCodeIp);
                }
            }
        }
        return true;
    }

    @Override
    public boolean getVerifyCode(String phoneno){
        String code = generateVerifyCode();
        try {
            sendVerifyCode(phoneno, code);
        } catch (Exception e) {
            LOOGER.error(e.getMessage(), e);
            throw new SendVerifyCodeException(e.getMessage(), e);
        }
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setPhoneNo(phoneno);
        verifyCode.setVerifyCode(code);
        Date date = new Date();
        verifyCode.setExpiredTime(getExpiredTime(date));
        verifyCode.setCreateTime(date);
        verifyCode.setUpdateTime(date);
        verifyCodeMapper.insertVerifyCode(verifyCode);
        return true;
    }

    @Override
    public Msg validateVerifyCode(String phoneNo, String code) {
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setPhoneNo(phoneNo);
        verifyCode.setVerifyCode(code);
        verifyCode = verifyCodeMapper.selectVerifyCode(verifyCode);
        if(verifyCode == null){
            return new Msg(Msg.FAILURE_CODE, "验证码：" + code +"不存在");
        }
        Date expiredTime = verifyCode.getExpiredTime();
        if(expiredTime.before(new Date())){
            return new Msg(Msg.FAILURE_CODE, "验证码：" + code +"已过期");
        }
        return new Msg(Msg.SUCCESS_CODE, "验证码验证通过");
    }

    @Override
    public Msg sendVerifyCode(String phone) {
        DefaultProfile profile = DefaultProfile.getProfile("default","LTAIisKGUmnqg7UX", "uQM8Dwt2JVwmG9MUs3dOgburHeCGwN");
        IAcsClient client = new DefaultAcsClient(profile);
        String verifyCode = String.valueOf(new Random().nextInt(89999) + 100000);
        log.info("verifyCode=" + verifyCode);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setMethod(MethodType.POST);
        commonRequest.setDomain("dysmsapi.aliyuncs.com");
        commonRequest.setVersion("2017-05-25");
        commonRequest.setAction("SendSms");
        commonRequest.putQueryParameter("PhoneNumbers", phone);
        commonRequest.putQueryParameter("SignName", "瀚华艺术网 "); // 注册/登陆平台验证
        commonRequest.putQueryParameter("TemplateCode", "SMS_172600717"); //SMS_172600717
        commonRequest.putQueryParameter("TemplateParam", "{'code':"+verifyCode+"}");
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(commonRequest);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        String data = response.getData();
        JSONObject jsonObject = JSONObject.parseObject(data);
        String message = String.valueOf(jsonObject.get("Message"));
        String code = String.valueOf(jsonObject.get("Code"));
        log.info("message===" + message);
        log.info("code == " + code);
        log.info("response.getData():" + response.getData());
        if(code.equals("OK")){
            VerifyCode verCode = new VerifyCode();
            verCode.setPhoneNo(phone);
            verCode.setVerifyCode(verifyCode);
            Date date = new Date();
            verCode.setExpiredTime(getExpiredTime(date));
            verCode.setCreateTime(date);
            verCode.setUpdateTime(date);
            verifyCodeMapper.insert(verCode);
            return new Msg(Msg.SUCCESS_CODE, "验证码发送成功");
        } else {
            return new Msg(Msg.FAILURE_CODE, message);
        }

    }


    private String generateVerifyCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    private boolean sendVerifyCode(String phoneno, String verifyCode) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        sb.append("{\"code\":\"").append(verifyCode).append("\"}");
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("appid", appid);
        params.add("signature", signature);
        params.add("project", project);
        params.add("to", phoneno);
        params.add("vars", URLEncoder.encode(sb.toString(), "UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> formEntity = new HttpEntity(params, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(SEND_MESSAGE_URL, formEntity, String.class);
        Map map = JsonUtil.jsonToObject(result.getBody(), Map.class);
        String status = (String)map.get("status");
        if (!"success".equals(status)) {
            throw new UnsupportedEncodingException("发送短信失败！\t code: " + map.get("code") + "\t msg: " + map.get("msg"));
        }
        return true;
    }

    private Date getExpiredTime(Date date) {
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       cal.add(Calendar.MINUTE, 1);
       return new Date(cal.getTimeInMillis());
    }

}
