package com.art.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.art.manager.config.upload.UploadConfig;
import com.art.manager.pojo.CommonCommodity;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.CommodityReq;
import com.art.manager.service.CommonCommodityService;
import com.art.manager.service.FileService;
import com.art.manager.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommonCommodityService commonCommodityService;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadConfig uploadConfig;
    /**
     * 新增普通商品
     *@author wxf
     * @param token
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveCommonCommodity", method = RequestMethod.POST)
    public Msg saveOrUpdateCommonCommodity(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid CommonCommodity commodity, HttpServletRequest request){
       /* SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            int i = commonCommodityService.saveCommonCommodity(commodity);
            if(i == -1){
                return new Msg(Msg.FAILURE_CODE, "商品名称重复");
            }else{
                return new Msg(Msg.SUCCESS_CODE, "插入成功");
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, "插入失败");
        }
    }

    /**
     * 修改普通商品
     * @param token
     * @param commodity
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateCommonCommodity", method = RequestMethod.POST)
    public Msg updateCommonCommodity(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid CommonCommodity commodity, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            int i = commonCommodityService.updateCommonCommodity(commodity);
            if(i == -1){
                return new Msg(Msg.FAILURE_CODE, "商品名称重复");
            }else{
                return new Msg(Msg.SUCCESS_CODE, "插入成功");
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上传展示图
     * @author wxf
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadShowPic", method = RequestMethod.POST)
    public Msg uploadPic(@RequestParam("file") MultipartFile file){
        try {
            String filePath = fileService.upload(file, uploadConfig.getShow());
            return new Msg(Msg.SUCCESS_CODE, filePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上传作品图
     * @author wxf
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadWorksPic", method = RequestMethod.POST)
    public Msg uploadWorksPic(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileService.upload(file, uploadConfig.getWorks());
            return new Msg(Msg.SUCCESS_CODE, filePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 查询普通商品
     * @param token
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCommonCommodity", method = RequestMethod.POST)
    public Msg getCommonCommodity(@RequestHeader(value = "Authorization") String token, @RequestBody Map<String, Object> params, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        Msg msg = commonCommodityService.getCommonCommodityByCondition(params);
        return msg;
    }

    /**
     * 批量删除
     * @param token
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/delCommonCommodity", method = RequestMethod.POST)
    public Msg delCommonCommodity(@RequestHeader(value = "Authorization") String token, @RequestBody CommodityReq req, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        Msg msg = commonCommodityService.batchDelCommonCommodity(req.getIds());
        return msg;
    }


    /**
     * 上下线
     * @param token
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/upperOrDown", method = RequestMethod.POST)
    public Msg upperOrDown(@RequestHeader(value = "Authorization") String token, @RequestBody Map<String, Object> params, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        return commonCommodityService.updateStatusById(params);

    }

    @RequestMapping(value = "/getCommodityInfo", method = RequestMethod.POST)
    public Msg getCommodityInfo(@RequestHeader(value = "Authorization") String token, @RequestBody CommodityReq req, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        if(req.getId() == null){
            return new Msg(Msg.FAILURE_CODE, "id为空");
        }

        try {
            CommonCommodity commodityInfo = commonCommodityService.getCommodityInfo(req);
            return new Msg(Msg.SUCCESS_CODE, commodityInfo);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCommodityById", method = RequestMethod.POST)
    public Msg getCommodityById(@RequestHeader(value = "Authorization") String token, @RequestBody CommodityReq req, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        if(req.getSpecialId() == null){
            return new Msg(Msg.FAILURE_CODE, "specialId为空");
        }

        try {
           return commonCommodityService.getCommodityById(req.getSpecialId());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 公众号 商品详情
     * @param req
     * @return
     */
    @RequestMapping(value = "/commodityDetail", method = RequestMethod.POST)
    public Msg commodityDetail(@RequestBody CommodityReq req){

        try {
            return new Msg(Msg.SUCCESS_CODE, commonCommodityService.commodityDetail(req));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 公众号 全部商品列表
     * 包含三种场景 1：查询所有某类风格的商品
     * 2：查询某类别下某风格的商品
     * 3：查询价格区间内的商品
     * @param req
     * @return
     */
    @RequestMapping(value = "/commodityList", method = RequestMethod.POST)
    public Msg commodityList(@RequestBody CommodityReq req){
        try {
            return new Msg(Msg.SUCCESS_CODE, commonCommodityService.commodityList(req));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}
