package com.art.manager.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NewsVo {
    private Integer id;
    private String newsTitle;
    private String titleId;
    private String operator;
    private String status;
    private Date createTime;
    private Date updateTime;
}
