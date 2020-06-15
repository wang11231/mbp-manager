package com.art.manager.controller;

import com.art.manager.config.upload.UploadConfig;
import com.art.manager.dto.SpecialDto;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Special;
import com.art.manager.pojo.SpecialType;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.SpecialReq;
import com.art.manager.service.FileService;
import com.art.manager.service.RecommendService;
import com.art.manager.service.SpecialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/special")
public class SpecialController {

    @Autowired
    private SpecialService specialService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadConfig uploadConfig;
    @Autowired
    private RecommendService recommendService;


    /**
     * 查询专场类型列表
     * @param
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Msg getList(/*@RequestHeader(value = "token") String token, */@RequestBody Map<String, Object> params, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return specialService.getList(params);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 查询专场列表
     * @param
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getSpecialList", method = RequestMethod.POST)
    public Msg getSpecialList(/*@RequestHeader(value = "token") String token, */@RequestBody Map<String, Object> params, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return specialService.getSpecialList(params);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 新增专场类型
     * @param
     * @param specialType
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveSpecialType", method = RequestMethod.POST)
    public Msg saveSpecialType(/*@RequestHeader(value = "token") String token, */@RequestBody SpecialType specialType, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            specialService.insertSpecialType(specialType);
            return new Msg(Msg.SUCCESS_CODE, "新增成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 新增专场
     * @param
     * @param special
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveSpecial", method = RequestMethod.POST)
    public Msg saveSpecial(/*@RequestHeader(value = "token") String token,*/ @RequestBody Special special, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            specialService.insertSpecial(special);
            return new Msg(Msg.SUCCESS_CODE, "新增成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 移除商品
     * @param
     * @param commodityId
     * @param request
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public Msg remove(/*@RequestHeader(value = "token") String token,*/ @RequestParam("commodityId") Long commodityId, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            specialService.removeCommondity(commodityId);
            return new Msg(Msg.SUCCESS_CODE, "移除成功");
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上传专场封面图
     * @param file
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadSpecialCover", method = RequestMethod.POST)
    public Msg uploadSpecialCover(@RequestParam("file") MultipartFile file, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
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
     * 修改专场类型
     * @param
     * @param specialType
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateSpecialType", method = RequestMethod.POST)
    public Msg updateSpecialType(/*@RequestHeader(value = "token") String token,*/ @RequestBody SpecialType specialType, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            specialService.updateSpecialType(specialType);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }


    /**
     * 修改专场
     * @param
     * @param special
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateSpecial", method = RequestMethod.POST)
    public Msg updateSpecial(/*@RequestHeader(value = "token") String token,*/ @RequestBody Special special, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            specialService.updateSpecial(special);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteSpecialType", method = RequestMethod.POST)
    public Msg deleteSpecialType(/*@RequestHeader(value = "token") String token, */@RequestBody SpecialReq req, HttpServletRequest request){

        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            specialService.deleteSpecialType(req);
            return new Msg(Msg.SUCCESS_CODE, "删除成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteSpecial", method = RequestMethod.POST)
    public Msg deleteSpecial(/*@RequestHeader(value = "token") String token, */@RequestBody SpecialReq req, HttpServletRequest request){

        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            specialService.deleteSpecial(req.getIds());
            return new Msg(Msg.SUCCESS_CODE, "删除成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/selectSpecial", method = RequestMethod.POST)
    public Msg selectSpecial(/*@RequestHeader(value = "token") String token,*/ HttpServletRequest request){

        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            List<SpecialDto> specialDtos = specialService.selectSpecial();
            return new Msg(Msg.SUCCESS_CODE, specialDtos);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }

    }

    @RequestMapping(value = "/getSpecialById", method = RequestMethod.POST)
    public Msg getSpecialById(/*@RequestHeader(value = "token") String token, */@RequestBody SpecialReq req, HttpServletRequest request){

        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/

        try {
            return new Msg(Msg.SUCCESS_CODE, specialService.getSpecialById(req));
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }
}
