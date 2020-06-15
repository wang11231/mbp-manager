package com.art.manager.controller;

import com.art.manager.config.upload.UploadConfig;
import com.art.manager.pojo.Msg;
import com.art.manager.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("upload")
@Slf4j
public class CommonController {

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private FileService fileService;

    @PostMapping("/{type}")
    public Msg upload(@RequestParam("file") MultipartFile file, @PathVariable("type") String type) {
        try {

            String filePath = fileService.upload(file, getPath(type));
            filePath = File.separator + filePath;
            return new Msg(Msg.SUCCESS_CODE, "上传成功", filePath.replaceAll("\\\\", "/"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
    }

    private String getPath(String type){
        if(StringUtils.isBlank(type)){
            throw new RuntimeException("上传类型不存在");
        }
        switch (type){
            case "artist":
                return uploadConfig.getArtist();
            case "auction":
                return uploadConfig.getAuction();
            case "show":
                return uploadConfig.getShow();
            case "works":
                return uploadConfig.getWorks();
            case "recommend":
                return uploadConfig.getRecommend();
            case "advertisy":
                return uploadConfig.getAdvertisy();
            case "special":
                return uploadConfig.getSpecial();
            case "news":
                return uploadConfig.getNews();
            default:
                throw new RuntimeException("上传类型非法，type:" +type);
        }
    }

}
