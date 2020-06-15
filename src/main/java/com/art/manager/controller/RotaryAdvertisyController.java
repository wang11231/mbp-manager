package com.art.manager.controller;

import com.art.manager.config.upload.UploadConfig;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.RotaryAdvertisy;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.RotaryAdvertisyReq;
import com.art.manager.service.FileService;
import com.art.manager.service.RotaryAdvertisyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rotaryAdvertisy")
public class RotaryAdvertisyController {

    @Autowired
    private RotaryAdvertisyService rotaryAdvertisyService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadConfig uploadConfig;
    /**
     * 轮播广告列表
     * @param params
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Msg getList(@RequestBody Map<String, Object> params, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            Map<String, Object> rotaryAdvertisy = rotaryAdvertisyService.getRotaryAdvertisy(params);
            return new Msg(Msg.SUCCESS_CODE, rotaryAdvertisy);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     *上下移
     * @return
     */
    @RequestMapping(value = "/updateSort", method = RequestMethod.POST)
    public Msg updateSort(@RequestBody RotaryAdvertisyReq req, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        if(req.getOperation().equals("1")){
            return rotaryAdvertisyService.moveUp(req.getId());
        } else if(req.getOperation().equals("2")){
            return rotaryAdvertisyService.moveDown(req.getId());
        } else {
            return new Msg(Msg.FAILURE_CODE, "operation参数错误");
        }
    }

    /**
     * 上下线
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Msg uodateStatus(@RequestBody RotaryAdvertisyReq req, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            rotaryAdvertisyService.updateStatus(req.getStatus(), req.getId());
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上传图片
     * @param file
     * @param
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploaAadvertisy", method = RequestMethod.POST)
    public Msg uploaAadvertisy(@RequestParam("file") MultipartFile file, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            String filePath = fileService.upload(file, uploadConfig.getAdvertisy());
            //rotaryAdvertisyService.insert(advertisyUrl, filePath, user);
            return new Msg(Msg.SUCCESS_CODE, filePath);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 新增
     * @param rotaryAdvertisy
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Msg insert(@RequestBody RotaryAdvertisy rotaryAdvertisy, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {

            rotaryAdvertisyService.insert(rotaryAdvertisy);
            return new Msg(Msg.SUCCESS_CODE, "新增成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Msg update(@RequestBody RotaryAdvertisy rotaryAdvertisy, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {

            rotaryAdvertisyService.update(rotaryAdvertisy);
            return new Msg(Msg.SUCCESS_CODE, "修改成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Msg delete(@RequestBody RotaryAdvertisyReq req, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        if(req.getIds() == null){
            return new Msg(Msg.FAILURE_CODE, "id为空");
        }
        try {
            rotaryAdvertisyService.delete(req.getIds());
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}
