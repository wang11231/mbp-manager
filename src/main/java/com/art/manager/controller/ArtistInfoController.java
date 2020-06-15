package com.art.manager.controller;

import com.art.manager.config.upload.UploadConfig;
import com.art.manager.pojo.Msg;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.SysUser;
import com.art.manager.pojo.artist.ArtistInfo;
import com.art.manager.request.ArtistInfoReq;
import com.art.manager.service.ArtistInfoService;
import com.art.manager.service.FileService;
import com.art.manager.vo.ArtistInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *  艺术家控制类
 */
@RestController
@RequestMapping("artist")
@Slf4j
public class ArtistInfoController {

    @Autowired
    private ArtistInfoService artistInfoService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadConfig uploadConfig;

    @ResponseBody
    @PostMapping("/getList")
    public Msg getList() {
        try {
            List<ArtistInfoVo> list = artistInfoService.getList(null);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getArtistInfo")
    public Msg getArtistInfo(@RequestBody ArtistInfoReq req) {
        try {
            ArtistInfo artistInfo = artistInfoService.getArtistInfo(req);
            return new Msg(Msg.SUCCESS_CODE, artistInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectById")
    public Msg selectById(@RequestBody ArtistInfoReq req) {
        try {
            ArtistInfo info = artistInfoService.selectById(req);
            return new Msg(Msg.SUCCESS_CODE, info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/selectByIdH5")
    public Msg selectByIdH5(@RequestBody ArtistInfoReq req) {
        try {
            ArtistInfo info = artistInfoService.selectById(req);
            return new Msg(Msg.SUCCESS_CODE, info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/getListByPage")
    public Msg getListByPage(@RequestBody ArtistInfoReq req) {
        try {
            Page page = new Page();
            page.setPageSize(req.getPageSize());
            page.setPageNum(req.getPageNum());
            Map<String, Object> list = artistInfoService.getListByPage(req, page);
            return new Msg(Msg.SUCCESS_CODE, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/deleteById")
    public Msg deleteById(@RequestBody ArtistInfoReq req) {
        try {
            artistInfoService.deleteByIds(req);
            return new Msg(Msg.SUCCESS_CODE, "删除成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/updateById")
    public Msg updateById(@RequestBody ArtistInfo info) {
        try {
            artistInfoService.updateById(info);
            return new Msg(Msg.SUCCESS_CODE, "修改成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/insert")
    public Msg insert(@RequestBody @Valid ArtistInfo info, HttpServletRequest request) {
        try {
            /*HttpSession session = request.getSession(false);
            Object o = session.getAttribute(token);
            if (o == null) {
                return new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
            }
            SysUser user = (SysUser) o;
            info.setUserId(user.getId());
            info.setUsername(user.getUsername());*/
            artistInfoService.insert(info);
            return new Msg(Msg.SUCCESS_CODE, "插入成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 获取所有艺术家列表
     * @param req
     * @return
     */
    @ResponseBody
    @PostMapping("/getArtist")
    public Msg getArtist(@RequestBody ArtistInfoReq req){
        try {
            return new Msg(Msg.SUCCESS_CODE, artistInfoService.getArtistList(req));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    /**
     * 获取所有艺术家列表-首字母
     * @return
     */
    @ResponseBody
    @PostMapping("/getArtistListNoPage")
    public Msg getArtistListNoPage(){
        try {
            return new Msg(Msg.SUCCESS_CODE, artistInfoService.getArtistListNoPage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }



}
