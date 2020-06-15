package com.art.manager.config.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "upload.path", ignoreUnknownFields = false)
@PropertySource("classpath:config/upload.properties")
public class UploadConfig {
    //艺术家上传路径
    private String artist;

    //拍品上传路径
    private String auction;

     // 展示图上传路径
    private String show;
    // 作品图上传路径
    private String works;
    // 广告图
    private String advertisy;
    // 推荐位
    private String recommend;
    private String special;
    //新闻图片
    private String news;

}
