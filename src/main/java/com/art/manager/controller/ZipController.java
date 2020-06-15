package com.art.manager.controller;

import com.art.manager.pojo.Msg;
import com.art.manager.util.UploadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

/**
 * zip控制类
 */
@RestController
@RequestMapping("/zip")
@Slf4j
public class ZipController {

    /**
     * 上传zip文件
     * @param zipFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadZip", method = RequestMethod.POST)
    public Msg uploadZip(@RequestParam("file") MultipartFile zipFile, HttpServletRequest request){
        log.info("文件上传开始-----------------");
        String zipFileName = zipFile.getOriginalFilename();
        String newFileName = UploadFileUtil.convertFileName(zipFileName);
        String path = "/opt/";
        try {
            String format = DateFormatUtils.format(new Date(), "yyyyMMdd");
            File file = new File(path, format);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            zipFile.transferTo(file);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
        return new Msg(Msg.SUCCESS_CODE, "上传成功");
    }
}
