package com.art.manager.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewsDto {
    private Integer id;
    private String newsTitle;
    private Date createTime;
    private String newsContent;
}
