package com.art.manager.controller;

import com.art.manager.config.upload.UploadConfig;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Recommend;
import com.art.manager.pojo.SysUser;
import com.art.manager.request.RecommendReq;
import com.art.manager.service.FileService;
import com.art.manager.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadConfig uploadConfig;
    /**
     * 推荐位广告列表
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Msg getList(@RequestBody Map<String, Object> params, HttpServletRequest request){
        /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            Map<String, Object> recommend = recommendService.getRecommendList(params);
            return new Msg(Msg.SUCCESS_CODE, recommend);
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
    @RequestMapping(value = "/uploaRecommend", method = RequestMethod.POST)
    public Msg uploaAadvertisy(@RequestParam("file") MultipartFile file, HttpServletRequest request){
       /* SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            String filePath = fileService.upload(file, uploadConfig.getRecommend());
            return new Msg(Msg.SUCCESS_CODE, filePath);
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 上下线
     * @param req
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public Msg updateStatus(@RequestBody RecommendReq req, HttpServletRequest request){
         /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
           if (user == null) {
                return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
             }*/
        try {
            recommendService.updateStatus(req);
            return new Msg(Msg.SUCCESS_CODE, "更新成功");
        }catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 推荐位新增
     * @param recommend
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Msg insert(@RequestBody Recommend recommend, HttpServletRequest request){
         /*SysUser user = (SysUser) request.getSession(false).getAttribute(token);
        if (user == null) {
            return new Msg(Msg.FAILURE_CODE, "token验证失败，请重新登陆");
        }*/
        try {
            recommendService.insertRecommend(recommend);
            return new Msg(Msg.SUCCESS_CODE, "新增成功");
        } catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Msg delete(@RequestBody RecommendReq req, HttpServletRequest request){
        try {
            recommendService.delete(req.getIds());
            return new Msg(Msg.SUCCESS_CODE, "删除成功");
        } catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Msg update(@RequestBody Recommend recommend, /*@RequestHeader(value = "token") String token, */HttpServletRequest request){
        try {
            recommendService.update(recommend);
            return new Msg(Msg.SUCCESS_CODE, "修改成功");
        } catch (Exception e){
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

}
