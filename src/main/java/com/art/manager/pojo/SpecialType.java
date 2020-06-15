package com.art.manager.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SpecialType {

    private Long id;
    private String name; // 专场类型名
    private String title; // 专场类型副标题
    private String operator; // 操作者
    private Long userId;
    private Integer status; // 默认 0， 0:可用  1：删除
    private Date createTime;
    private Date updateTime;
}
